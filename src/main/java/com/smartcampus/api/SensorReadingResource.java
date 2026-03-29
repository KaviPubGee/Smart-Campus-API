package com.smartcampus.api;

import com.smartcampus.data.DataStore;

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

    /* 
     * Historical telemetry operations (GET / POST) will be fully 
     * materialized into this Sub-Resource during Day 13.
     */
}
