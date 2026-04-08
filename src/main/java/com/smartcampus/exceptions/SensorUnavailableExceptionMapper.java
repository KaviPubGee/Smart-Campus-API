package com.smartcampus.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps the SensorUnavailableException to an HTTP 403 Forbidden.
 */
@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {
    
    @Override
    public Response toResponse(SensorUnavailableException exception) {
        String jsonError = String.format("{\"error\": \"%s\"}", exception.getMessage());
        return Response.status(Response.Status.FORBIDDEN)
                       .entity(jsonError)
                       .type("application/json")
                       .build();
    }
}
