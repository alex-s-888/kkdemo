package hometask.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(description = "Defines payload for City update requests.")
public class UpdateCityRequest {

    @NotBlank
    @Size(max = 64)
    @Schema(example = "Saint-Tropez")
    private String cityName;

    @NotBlank
    @Size(max = 2048)
    @Schema(example = "https://upload.wikimedia.org/wikipedia/commons/thumb/example.jpg")
    private String cityPhoto;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityPhoto() {
        return cityPhoto;
    }

    public void setCityPhoto(String cityPhoto) {
        this.cityPhoto = cityPhoto;
    }
}
