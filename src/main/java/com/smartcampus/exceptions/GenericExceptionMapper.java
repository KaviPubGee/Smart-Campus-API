package com.smartcampus.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Global safety net. Catches any unhandled Throwable (like NullPointerException) 
 * preventing stack traces from leaking to the client.
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    
    @Override
    public Response toResponse(Throwable exception) {
        // We catch everything but purposely hide the actual exception message/trace
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("{\"error\": \"An unexpected internal server error occurred. Please try again later.\"}")
                       .type("application/json")
                       .build();
    }
}
