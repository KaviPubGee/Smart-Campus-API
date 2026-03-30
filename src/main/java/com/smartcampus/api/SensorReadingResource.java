package com.smartcampus.api;

import com.smartcampus.data.DataStore;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

/**
 * Sub-resource handling telemetric readings for a specific assigned Sensor.
 * 
 * Notice: This class deliberately lacks an @Path annotation. It is structurally 
 * unlinked to the root routing engine and instead dynamically delegated to by 
 * the Sub-Resource Locator method inside `SensorResource`.
 */
public class SensorReadingResource {

    private String sensorId;
    private DataStore dataStore = DataStore.getInstance();

    /**
     * Constructor specifically initialized by the parent SensorResource locator method.
     * @param sensorId The parent's physical hardware identifier extracted from the URI matrix.
     */
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    /**
     * Retrieves all historical telemetry readings for this specific sensor.
     * @return 200 OK with a JSON array of readings.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadings() {
        List<SensorReading> readings = dataStore.getSensorReadings(sensorId);
        return Response.ok(readings).build();
    }

    /**
     * Appends a new telemetry reading event to this sensor's history.
     * @param reading The JSON object containing the timestamp and metric value.
     * @return 201 Created with the persisted reading.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
        // Validate Parent Sensor Existence first
        if (dataStore.getSensor(sensorId) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Target parent sensor does not exist.\"}")
                    .build();
        }

        // Generate ID and default timestamp if missing
        if (reading.getId() == null || reading.getId().trim().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        }
        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        dataStore.addSensorReading(sensorId, reading);

        // Cross-resource state sync: Update the parent Sensor's currentValue
        Sensor parentSensor = dataStore.getSensor(sensorId);
        if (parentSensor != null) {
            parentSensor.setCurrentValue(reading.getValue());
        }

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}
