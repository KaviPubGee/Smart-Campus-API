package com.smartcampus.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Global logging filter that structurally intercepts all incoming API requests 
 * and outgoing responses to create centralized audit trails without polluting 
 * business controller logic.
 */
@Provider
public class ApiLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        String uri = requestContext.getUriInfo().getAbsolutePath().toString();
        System.out.println("[API AUDIT - REQUEST] Method: " + method + " | Target URI: " + uri);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        int status = responseContext.getStatus();
        System.out.println("[API AUDIT - RESPONSE] HTTP Status Emitted: " + status);
        System.out.println("---------------------------------------------------------");
    }
}
