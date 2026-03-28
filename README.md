# Smart Campus Sensor & Room Management API

**Module:** 5COSC022W Client-Server Architectures  
**Description:** A custom JAX-RS RESTful web API for managing university campus rooms and sensors without external database coupling.

## Coursework Report: Conceptual Questions

### Part 1: Service Architecture & Setup

**Q1: Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures.**

By default, the lifecycle of a JAX-RS Resource class is **per-request**. This means that the JAX-RS runtime (e.g., Jersey) creates a brand new instance of your resource class for every single incoming HTTP request it receives; the object is effectively destroyed and collected immediately after returning the response. 

Because of this stateless architectural behavior, developers cannot safely store application state (such as arrays containing stored sensors or rooms) inside local instance variables of the resource class—the variables would simply be reset to empty during the next request, resulting in total ongoing data loss. Consequently, to share data safely across requests without an external database, we are forced to manage a centralized, thread-safe memory store outside of the resource class. In this project, we achieved this by abstracting into a Singleton design pattern combined directly with `java.util.concurrent.ConcurrentHashMap`. This guarantees that the multiple, transient API resource instances can safely read and mutate our memory structures simultaneously during parallel traffic without causing race conditions or data corruption.

**Q2: Why is the provision of "Hypermedia" (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?**

Providing hypermedia (HATEOAS - Hypermedia As The Engine Of Application State) is widely considered a hallmark of the highest level of REST structural maturity (Richardson Maturity Model Level 3). It works by returning dynamic navigational links—essentially the semantic "next steps"—directly embedded within the API's JSON body responses.

This approach massively benefits client developers compared to relying entirely on static documentation. Instead of developers hardcoding static URL domains into frontend queries based on an offline PDF, the client simply invokes a single root "Discovery" entry point and dynamically extracts the links provided inside the responses to navigate sub-resources. If the backend engineering team decides to restructure the URL hierarchy or alter API domain routing later in the project lifecycle, a tightly coupled client application will likely break. However, a HATEOAS-compliant client will safely continue running, because it fluidly interprets the dynamically provided routing URLs served directly from the server's responses at runtime. It essentially turns basic web services into universally discoverable, self-documenting state machines.

### Part 2: Room Management

**Q3: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing.**

Returning only IDs drastically reduces the sheer size of the HTTP response payload, saving network bandwidth and memory—especially when scaling up to thousands of rooms containing nested arrays of sensor data. However, for a client application to actually display useful information to the user (like room names or capacities), it must subsequently perform a deluge of individual `GET /api/v1/rooms/{id}` requests for each downloaded ID. This results in the "N+1 query problem" over the network, drastically increasing HTTP overhead and latency. 

Conversely, returning the full room objects maximizes initial bandwidth utilization and data transfer bulk, but allows the client to immediately render the entire screen or dashboard in a single round-trip without making any supplementary API calls. For general API design, returning shallow, summarized objects (e.g., ID, Name, Capacity—excluding the heavy, deep relational arrays of sensors) is often a best-practice compromise to balance both network constraints and client processing speed.

**Q4: Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.**

Yes, the `DELETE /api/v1/rooms/{roomId}` operation in this implementation is absolutely **idempotent**, which aligns mathematically with strict REST specifications.

Idempotency guarantees that executing the identical HTTP request one time will leave the server in the exact same target state as executing it a hundred times sequentially. In our specific API, if a client successfully issues the first `DELETE` command, the targeted room is safely purged from the `DataStore` (assuming it passes safety constraints). If a network glitch causes the exact same client to mistakenly transmit the identical `DELETE` command a second time, the server will correctly fail to find the room in the `DataStore` and safely return an `HTTP 404 Not Found` response without mutating any system state. Since the ultimate end-state of the server (the room no longer existing) remains identically unmutated regardless of subsequent accidental calls, the operation is structurally idempotent.

### Part 3: Sensor Operations & Linking

**Q5: We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?**

When we strictly annotate an endpoint method with `@Consumes(MediaType.APPLICATION_JSON)`, we form a hard, declarative contract telling the underlying JAX-RS deployment environment (Jersey) exactly which HTTP `Content-Type` headers that specific Java method is mathematically capable of unmarshalling into a POJO. 

If a client sends an incompatible request formatting payload (like `text/xml` or `text/plain`), the JAX-RS runtime intercepts the request before it even reaches our internal `createSensor` method logic. It compares the `Content-Type` header of the incoming packet against our `@Consumes` annotation metadata rule. Detecting the mismatch, JAX-RS automatically blocks the execution and instantly returns an **`HTTP 415 Unsupported Media Type`** error response back to the client. This deeply enforces data safety and completely prevents raw XML strings or unsupported stream formats from accidentally triggering destructive `NullPointerException` errors inside our backend JSON processing pipelines.

**Q6: You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/v1/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?**

In sophisticated REST architectures natively reflecting HTTP specs, **Path Parameters** (`@PathParam`) are structurally intended exclusively for identifying unique, specific physical resources (e.g., `/api/v1/sensors/SENSOR-123`). Conversely, **Query Parameters** (`@QueryParam`) are designed for non-identifying operations on a collection, such as sorting, pagination, and attribute filtering (e.g., `?type=CO2&status=ACTIVE`).

If we embedded the filter as a path element (`/sensors/type/CO2`), we rigidly lock our API routing hierarchy, mistakenly elevating a temporary hardware attribute ("type") to the architectural status of a core sub-resource. This creates massive scaling problems: what if the client wants to filter sensors by type AND status AND capacity simultaneously? Designing paths like `/sensors/type/CO2/status/ACTIVE` becomes structurally brittle and impossible to scale dynamically. Using standard `?type=CO2` query parameters creates optional, dynamic filtering parameters that are loosely coupled, composable, and naturally align with standard internet architectural paradigms.
