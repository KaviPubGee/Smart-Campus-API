package com.smartcampus.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Root Discovery Endpoint yielding hypermedia navigation arrays.
 */
@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscoveryMap() {
        Map<String, Object> discoveryData = new HashMap<>();
        discoveryData.put("version", "1.0.0");
        discoveryData.put("description", "Smart Campus Sensor & Room Management API");
        discoveryData.put("adminContact", "admin@smartcampus.ac.uk");
        
        // Map of primary collections
        Map<String, String> resources = new HashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");
        
        discoveryData.put("collections", resources);
        
        return Response.ok(discoveryData).build();
    }
}
