package hometask.validator;

import hometask.errors.RequestValidationException;
import hometask.errors.UntrustedUrlException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class CityValidator {

    private final static Set<String> TRUSTED_SOURCES = Set.of(
            "https://upload.wikimedia.org/wikipedia/commons/",
            "https://www.freeimages.com/photo/");

    public void validatePageParameters(int pageNumber, int pageSize) {
        List<String> errors = new LinkedList<>();
        if (pageNumber < 0) {
            errors.add("pageNumber must not be negative");
        }
        if (pageSize <= 0) {
            errors.add("pageSize must be positive");
        }
        if (!errors.isEmpty()) {
            throw new RequestValidationException("Invalid argument(s)", errors);
        }
    }

    /**
     * Verifies that provided url is not malicious.
     * For this demo simplified implementation is done: checking against set of trusted sites.
     *
     * @param url external URL
     * @throws UntrustedUrlException if url may be malicious
     */
    public void validateExternalUrl(String url) {
        if(!isTrustedSource(url)){
            throw new UntrustedUrlException("Untrusted external url", "Potentially malicious url parameter");
        }
    }

    private boolean isTrustedSource(String url){
        return TRUSTED_SOURCES.stream().anyMatch(ts -> url.toLowerCase().startsWith(ts));
    }

}
