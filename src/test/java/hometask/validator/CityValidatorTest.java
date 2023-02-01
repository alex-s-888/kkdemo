package hometask.validator;

import hometask.errors.RequestValidationException;
import hometask.errors.UntrustedUrlException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CityValidatorTest {

    private CityValidator cityValidator = new CityValidator();

    @Test
    void should_ValidationFail_when_NegativePageNumber(){
        RequestValidationException thrown = assertThrows(RequestValidationException.class, () -> {
            cityValidator.validatePageParameters(-1, 10);
        });

        assertEquals("Invalid argument(s)", thrown.getMessage());
        assertNotNull(thrown.getErrors());
        assertFalse(thrown.getErrors().isEmpty());
    }

    @Test
    void should_ValidationFail_when_ZeroPageNumber(){
        RequestValidationException thrown = assertThrows(RequestValidationException.class, () -> {
            cityValidator.validatePageParameters(1, 0);
        });

        assertEquals("Invalid argument(s)", thrown.getMessage());
        assertNotNull(thrown.getErrors());
        assertFalse(thrown.getErrors().isEmpty());
    }

    @Test
    void should_SuccessfullyValidate_when_ValidPageParamneters(){
        assertDoesNotThrow(() -> {
            cityValidator.validatePageParameters(0, 10);
        });
    }

    @Test
    void should_ValidationFail_when_NotTrustedPhotoUrl(){
        UntrustedUrlException thrown = assertThrows(UntrustedUrlException.class, () -> {
            cityValidator.validateExternalUrl("https://bad.com/suspicious.jpg");
        });

        assertEquals("Untrusted external url", thrown.getMessage());
        assertNotNull(thrown.getErrors());
        assertFalse(thrown.getErrors().isEmpty());
    }

    @Test
    void should_SuccessfullyValidate_when_TrustedPhotoUrl(){
        assertDoesNotThrow(() -> {
            cityValidator.validateExternalUrl("https://upload.wikimedia.org/wikipedia/commons/city.jpg");
        });
    }
}