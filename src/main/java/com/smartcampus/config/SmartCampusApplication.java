package com.smartcampus.config;

import com.smartcampus.api.DiscoveryResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * JAX-RS Application Configuration establishing the root entry point.
 */
@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        // Register API resources
        resources.add(DiscoveryResource.class);
        return resources;
    }
}
