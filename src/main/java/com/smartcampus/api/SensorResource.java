package com.smartcampus.api;

import com.smartcampus.data.DataStore;
import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Resource for managing Sensor entities across the campus.
 */
@Path("/sensors")
public class SensorResource {

    private DataStore dataStore = DataStore.getInstance();

    /**
     * Registers a new sensor and securely links it to an existing Room.
     * @param sensor The JSON payload containing sensor details.
     * @return 201 CREATED if successful, or 422 Unprocessable Entity if the room is invalid.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor) {
        // 1. Initial Validation
        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"A valid roomId is explicitly required to register hardware.\"}")
                    .build();
        }

        // 2. Cross-Resource Integrity Validation
        Room parentRoom = dataStore.getRoom(sensor.getRoomId());
        if (parentRoom == null) {
            // Will be abstracted by LinkedResourceNotFoundException in Day 16
            return Response.status(422) 
                    .entity("{\"error\": \"The specified room does not exist in the system.\"}")
                    .build();
        }

        // 3. ID Generation & Persisting
        if (sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            sensor.setId(UUID.randomUUID().toString());
        }
        
        // Add default status if missing
        if (sensor.getStatus() == null) {
            sensor.setStatus("ACTIVE");
        }

        dataStore.addSensor(sensor);
        
        // 4. Link the new sensor to the parent Room automatically
        parentRoom.addSensorId(sensor.getId());

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }

    /**
     * Retrieves sensors across the campus.
     * If the "type" query parameter is provided (e.g., ?type=CO2), it returns only matching sensors.
     * @param type The optional hardware category query string.
     * @return 200 OK with the JSON array of matching hardware sensors.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensors(@QueryParam("type") String type) {
        Collection<Sensor> allSensors = dataStore.getSensors().values();

        // If no filter is applied, return everything
        if (type == null || type.trim().isEmpty()) {
            return Response.ok(allSensors).build();
        }

        // Apply stream filtering matching the provided type (case-insensitive)
        List<Sensor> filtered = allSensors.stream()
                .filter(sensor -> sensor.getType() != null && sensor.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());

        return Response.ok(filtered).build();
    }
}
