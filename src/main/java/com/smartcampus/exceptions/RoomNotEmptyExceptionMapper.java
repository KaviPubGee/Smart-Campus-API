package com.smartcampus.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps the RoomNotEmptyException explicitly to an HTTP 409 Conflict.
 */
@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {
    
    @Override
    public Response toResponse(RoomNotEmptyException exception) {
        String jsonError = String.format("{\"error\": \"%s\"}", exception.getMessage());
        
        return Response.status(Response.Status.CONFLICT)
                       .entity(jsonError)
                       .type("application/json")
                       .build();
    }
}
