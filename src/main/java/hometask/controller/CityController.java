package hometask.controller;

import hometask.entity.CityEntity;
import hometask.errors.DuplicateNameException;
import hometask.errors.ErrorResponse;
import hometask.model.City;
import hometask.model.PageResult;
import hometask.model.UpdateCityRequest;
import hometask.repository.CitiesRepository;
import hometask.validator.CityValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import hometask.mapper.CityMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static java.lang.String.format;

@RestController
@Validated
@RequestMapping(value = "/hometask/v1/")
@Tag(name = "Cities Controller", description = "Search / update operations for cities")
public class CityController {

    private final CitiesRepository citiesRepository;

    private final CityValidator cityValidator;

    public CityController(@Autowired CitiesRepository citiesRepository,
                          @Autowired CityValidator cityValidator) {
        this.citiesRepository = citiesRepository;
        this.cityValidator = cityValidator;
    }

    @Operation(description = "Finds all cities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cities retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResult.class))}),
    })
    @GetMapping(path = "cities")
    public ResponseEntity<PageResult<City>> findAll(
            @Parameter(name = "pageNumber", required = true, description = "page to display, starts at 0")
            @RequestParam(defaultValue = "0") int pageNumber,
            @Parameter(name = "pageSize", required = false, description = "size of the page")
            @RequestParam(defaultValue = "20") int pageSize) {

        cityValidator.validatePageParameters(pageNumber, pageSize);

        Page<CityEntity> entitiesPage = citiesRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by("name")));
        return new ResponseEntity<>(CityMapper.mapFromEntitiesPage(entitiesPage), HttpStatus.OK);
    }

    @Operation(description = "Finds city by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "City retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = City.class))}),
            @ApiResponse(responseCode = "404", description = "City not found",
                    content = @Content)})
    @GetMapping(path = "city/{id}")
    public ResponseEntity<City> getById(@PathVariable Long id) {

        CityEntity cityEntity = citiesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, format("City with id %d not found", id)));
        return new ResponseEntity<>(CityMapper.mapFromEntity(cityEntity), HttpStatus.OK);
    }

    @Operation(description = "Updates City")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "City updated",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid argument(s)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "City not found",
                    content = @Content)})
    @PutMapping(path = "city/{id}")
    public ResponseEntity<Void> updateCity(@PathVariable Long id, @Valid @RequestBody UpdateCityRequest updateCityRequest) {

        cityValidator.validateExternalUrl(updateCityRequest.getCityPhoto());

        CityEntity cityEntity = citiesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, format("City with id %d not found", id)));

        String newCityName = normalizeCityName(updateCityRequest.getCityName());
        if (existsOtherCitySameName(id, newCityName)) {
            throw new DuplicateNameException("Duplicate city name", format("City with the name '%s' already exists.", newCityName));
        }

        cityEntity.setName(newCityName);
        cityEntity.setPhoto(updateCityRequest.getCityPhoto().trim());
        citiesRepository.save(cityEntity);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private String normalizeCityName(String cityName){
        return  cityName == null ? null : StringUtils.normalizeSpace(cityName);
    }

    private boolean existsOtherCitySameName(Long currentId, String newCityName) {
        List<CityEntity> sameNameCities = citiesRepository.findByNameIgnoreCase(newCityName);
        return sameNameCities.stream().anyMatch(city -> !currentId.equals(city.getId()));
    }
}
