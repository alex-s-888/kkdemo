package hometask.mapper;

import hometask.entity.CityEntity;
import hometask.model.City;
import hometask.model.PageResult;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

/**
 * Utility to do mappings between Entities and DTO items.
 * Trivial for this demo, for more complex cases would suggest using library like MapStruct.
 */
public class CityMapper {

    public static City mapFromEntity(CityEntity entity) {
        City result = new City();
        result.setId(entity.getId());
        result.setName(entity.getName());
        result.setPhoto(entity.getPhoto());
        return result;
    }

    public static PageResult<City> mapFromEntitiesPage(Page<CityEntity> entitiesPage) {
        PageResult<City> result = new PageResult<>();
        result.getMetadata().setTotalElements(entitiesPage.getTotalElements());
        result.getMetadata().setTotalPages(entitiesPage.getTotalPages());
        result.getMetadata().setPageSize(entitiesPage.getSize());
        result.getMetadata().setCurrentPage(entitiesPage.getNumber());
        result.getMetadata().setCurrentPageSize(entitiesPage.getNumberOfElements());

        result.setContent(entitiesPage.stream()
                .map(CityMapper::mapFromEntity)
                .collect(Collectors.toList()));

        return result;
    }
}
