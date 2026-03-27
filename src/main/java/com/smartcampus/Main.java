package com.smartcampus;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import com.smartcampus.config.SmartCampusApplication;

import java.io.IOException;
import java.net.URI;

/**
 * Main class to launch the embedded Grizzly HTTP server.
 */
public class Main {
    // We mount the Grizzly app to our desired root path.
    public static final String BASE_URI = "http://localhost:8080/api/v1/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // Initialize Jersey application configuration
        final ResourceConfig rc = ResourceConfig.forApplicationClass(SmartCampusApplication.class);
        
        // Start the server natively
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Smart Campus API started with endpoints available at %s\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.shutdownNow();
    }
}
