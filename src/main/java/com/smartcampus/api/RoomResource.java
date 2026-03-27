package com.smartcampus.api;

import com.smartcampus.data.DataStore;
import com.smartcampus.models.Room;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.UUID;

/**
 * Resource for managing Room entities.
 */
@Path("/rooms")
public class RoomResource {

    // Singleton DataStore usage
    private DataStore dataStore = DataStore.getInstance();

    /**
     * Retrieves all rooms in the campus.
     * @return 200 OK with a JSON array of Room objects.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRooms() {
        Collection<Room> rooms = dataStore.getRooms().values();
        return Response.ok(rooms).build();
    }

    /**
     * Creates a new room.
     * @param room The JSON payload mapped to a Room object.
     * @return 201 CREATED with the created Room object.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {
        // Validation & Defaulting
        if (room.getId() == null || room.getId().trim().isEmpty()) {
            room.setId(UUID.randomUUID().toString());
        }
        if (room.getSensorIds() == null) {
            room.setSensorIds(new java.util.ArrayList<>());
        }

        dataStore.addRoom(room);

        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }
}
