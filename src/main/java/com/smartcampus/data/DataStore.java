package com.smartcampus.data;

import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A thread-safe Singleton in-memory data store to act as the database
 * for the Smart Campus API coursework.
 */
public class DataStore {

    private static DataStore instance;

    // In-memory collections
    private Map<String, Room> rooms;
    private Map<String, Sensor> sensors;
    // Map of SensorID -> List of Readings
    private Map<String, List<SensorReading>> sensorReadings;

    // Private constructor for Singleton pattern
    private DataStore() {
        rooms = new ConcurrentHashMap<>();
        sensors = new ConcurrentHashMap<>();
        sensorReadings = new ConcurrentHashMap<>();
        
        // Optional: Pre-populate some dummy data if needed
    }

    // Thread-safe instance retrieval
    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // --- Room Operations ---
    public Map<String, Room> getRooms() {
        return rooms;
    }

    public Room getRoom(String id) {
        return rooms.get(id);
    }

    public void addRoom(Room room) {
        rooms.put(room.getId(), room);
    }

    public void removeRoom(String id) {
        rooms.remove(id);
    }

    // --- Sensor Operations ---
    public Map<String, Sensor> getSensors() {
        return sensors;
    }

    public Sensor getSensor(String id) {
        return sensors.get(id);
    }

    public void addSensor(Sensor sensor) {
        sensors.put(sensor.getId(), sensor);
    }

    public void removeSensor(String id) {
        sensors.remove(id);
    }

    // --- SensorReading Operations ---
    public List<SensorReading> getSensorReadings(String sensorId) {
        return sensorReadings.getOrDefault(sensorId, new ArrayList<>());
    }

    public void addSensorReading(String sensorId, SensorReading reading) {
        sensorReadings.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);
    }
}
