package hometask.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.LinkedList;
import java.util.List;

@Schema(description = "Defines pageable search result.")
public class PageResult<T> {

    private PageMetadata metadata = new PageMetadata();
    private List<T> content;

    public PageMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(PageMetadata metadata) {
        this.metadata = metadata;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
