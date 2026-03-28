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

    /**
     * Retrieves a specific room by its ID.
     * @param roomId The room ID from the URL path.
     * @return 200 OK if found, or 404 Not Found.
     */
    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = dataStore.getRoom(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Room not found\"}")
                    .build();
        }
        return Response.ok(room).build();
    }

    /**
     * Deletes a specific room, provided it is empty of sensors.
     * @param roomId The room ID to delete.
     * @return 204 No Content if successful, 404 Not Found, or 409 Conflict.
     */
    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = dataStore.getRoom(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Room not found\"}")
                    .build();
        }

        // Business Logic Constraint: cannot delete room with active sensors
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\": \"Cannot delete room. Active sensors are currently assigned to it.\"}")
                    .build();
        }

        dataStore.removeRoom(roomId);
        return Response.noContent().build();
    }
}
