package dev.vivekanand.productservice.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("uri=/test/path");
    }

    @Test
    void handleNotFound_ShouldReturn404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        ResponseEntity<ApiErrorResponse> response = handler.handleNotFound(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().messages().size());
        assertEquals("Not found", response.getBody().messages().get(0));
        assertEquals("/test/path", response.getBody().path());
    }

    @Test
    void handleBadRequest_ShouldReturn409() {
        DuplicateSkuException ex = new DuplicateSkuException("SKU-123");
        ResponseEntity<ApiErrorResponse> response = handler.handleBadRequest(ex, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("SKU already exists: SKU-123", response.getBody().messages().get(0));
    }

    @Test
    void handleMethodArgumentNotValid_ShouldReturn400() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "message");
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        
        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(
                ex, new HttpHeaders(), HttpStatusCode.valueOf(400), webRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiErrorResponse body = (ApiErrorResponse) response.getBody();
        assertNotNull(body);
        assertEquals(1, body.messages().size());
        assertEquals("field: message", body.messages().get(0));
    }

    @Test
    void handleConstraintViolation_ShouldReturn400() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("property");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("violation message");
        
        ConstraintViolationException ex = new ConstraintViolationException("msg", Set.of(violation));
        
        ResponseEntity<ApiErrorResponse> response = handler.handleConstraintViolation(ex, webRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().messages().size());
        assertEquals("property: violation message", response.getBody().messages().get(0));
    }

    @Test
    void handleGeneralException_ShouldReturn500() {
        Exception ex = new Exception("General error");
        ResponseEntity<ApiErrorResponse> response = handler.handleGeneralException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().messages().get(0).contains("General error"));
    }
}
