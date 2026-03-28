package com.smartcampus.api;

import com.smartcampus.data.DataStore;
import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

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
}
