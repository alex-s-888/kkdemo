package hometask.controller;

import hometask.entity.CityEntity;
import hometask.repository.CitiesRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CityControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CitiesRepository citiesRepository;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    void should_ReturnResultPage_when_SearchingCities() throws Exception {
        mockMvc.perform(get("/hometask/v1/cities")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metadata.currentPage", Matchers.equalTo(2)))
                .andExpect(jsonPath("$.metadata.currentPageSize", Matchers.equalTo(10)))
                .andExpect(jsonPath("$.content", Matchers.hasSize(10)));
    }

    @Test
    void should_ReturnBadRequest_when_PageNuberIsNegative() throws Exception {
        mockMvc.perform(get("/hometask/v1/cities")
                        .queryParam("pageNumber", "-1")
                        .queryParam("pageSize", "8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.containsString("Invalid argument")))
                .andExpect(jsonPath("$.errors", Matchers.not(Matchers.empty())));
    }

    @Test
    void should_ReturnBadRequest_when_PageSizeIsNegative() throws Exception {
        mockMvc.perform(get("/hometask/v1/cities")
                        .queryParam("pageNumber", "1")
                        .queryParam("pageSize", "-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.containsString("Invalid argument")))
                .andExpect(jsonPath("$.errors", Matchers.not(Matchers.empty())));
    }

    @Test
    void should_ReturnCorrectCity_when_IdIsPresentInDatabase() throws Exception {
        mockMvc.perform(get("/hometask/v1/city/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.name", Matchers.equalTo("Delhi")))
                .andExpect(jsonPath("$.photo", Matchers.containsString("IN-DL.svg")));
    }

    @Test
    void should_ReturnStatusNotFound_when_IdIsOutOfBound() throws Exception {
        mockMvc.perform(get("/hometask/v1/city/5000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_SuccessfullyUpdateCity_when_ValidArguments() throws Exception {
        mockMvc.perform(put("/hometask/v1/city/21")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ " +
                                "  \"cityName\": \"  Saint   Tropez  \", " +
                                "  \"cityPhoto\": \"https://upload.wikimedia.org/wikipedia/commons/city.jpg\" " +
                                " }"))
                .andExpect(status().isNoContent());

        Optional<CityEntity> optional = citiesRepository.findById(21L);
        assertTrue(optional.isPresent());
        CityEntity updated = optional.get();
        assertEquals("Saint Tropez", updated.getName());
        assertEquals("https://upload.wikimedia.org/wikipedia/commons/city.jpg", updated.getPhoto());
    }

    @Test
    void should_NotUpdateCity_when_NewNameIsDuplicate() throws Exception {
        mockMvc.perform(put("/hometask/v1/city/21")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ " +
                                "  \"cityName\": \"London\", " +
                                "  \"cityPhoto\": \"https://upload.wikimedia.org/wikipedia/commons/city.jpg\" " +
                                " }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.containsString("Duplicate city name")))
                .andExpect(jsonPath("$.errors", Matchers.not(Matchers.empty())));
    }

    @Test
    void should_NotUpdateCity_when_PhotoUrlUntrusted() throws Exception {
        mockMvc.perform(put("/hometask/v1/city/25")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ " +
                                "  \"cityName\": \"Saint Tropez\", " +
                                "  \"cityPhoto\": \"https://wrong.io/suspicious.jpg\" " +
                                " }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.containsString("Untrusted external url")))
                .andExpect(jsonPath("$.errors", Matchers.not(Matchers.empty())));
    }
}
