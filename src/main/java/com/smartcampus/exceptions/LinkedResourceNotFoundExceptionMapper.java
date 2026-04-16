package com.smartcampus.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps the LinkedResourceNotFoundException explicitly to an HTTP 422 Unprocessable Entity.
 */
@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    
    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        String jsonError = String.format("{\"error\": \"%s\"}", exception.getMessage());
        
        // 422 Unprocessable Entity is generally used for semantic validation failures inside payloads.
        return Response.status(422)
                       .entity(jsonError)
                       .type("application/json")
                       .build();
    }
}
