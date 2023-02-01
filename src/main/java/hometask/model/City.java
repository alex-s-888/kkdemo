package hometask.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Defines DTO for City search results.")
public class City {

    @Schema(example = "123")
    private Long Id;

    @Schema(example = "Adelaide")
    private String name;

    @Schema(example = "https://upload.wikimedia.org/wikipedia/commons/picture.jpg")
    private String photo;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
