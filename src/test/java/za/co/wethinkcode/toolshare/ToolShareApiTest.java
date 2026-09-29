package za.co.wethinkcode.toolshare;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ToolShareApiTest {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper json = new ObjectMapper();

    private Javalin app;
    private String base;

    @BeforeEach
    void setUp() {
        ToolCatalogue catalogue = new InMemoryToolCatalogue(List.of(
                new HandTool(1, "Claw Hammer"),
                new GardenTool(2, "Spade"),
                new HandTool(3, "Step Ladder"),
                new GardenTool(4, "Wheelbarrow")));
        Clock clock = Clock.fixed(Instant.parse("2026-03-01T08:00:00Z"), ZoneOffset.UTC);
        LoanService service = new LoanService(new InMemoryLoanRepository(), clock);
        app = new ToolShareApp(catalogue, service).create().start(0);
        base = "http://localhost:" + app.port();
    }

    @AfterEach
    void tearDown() {
        app.stop();
    }

    // ---- provided ---------------------------------------------------------------

    @Test
    void health_isOk() throws Exception {
        HttpResponse<String> response = get("/health");
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo("OK");
    }

    // ---- Q8.1 GET /api/tools ----------------------------------------------------

    @Test
    void getTools_returnsAllToolsAsJson() throws Exception {
        HttpResponse<String> response = get("/api/tools");
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.headers().firstValue("Content-Type").orElse("")).contains("application/json");

        JsonNode body = json.readTree(response.body());
        assertThat(body.isArray()).isTrue();
        assertThat(body).hasSize(4);
        assertThat(body.get(0).get("name").asText()).isEqualTo("Claw Hammer");
        assertThat(body.get(0).get("category").asText()).isEqualTo("HAND");
        assertThat(body.get(1).get("maxLoanDays").asInt()).isEqualTo(5);
    }

    // ---- Q8.2 GET /api/tools/{id} -----------------------------------------------

    @Test
    void getToolById_returnsThatTool() throws Exception {
        HttpResponse<String> response = get("/api/tools/2");
        assertThat(response.statusCode()).isEqualTo(200);
        JsonNode body = json.readTree(response.body());
        assertThat(body.get("id").asInt()).isEqualTo(2);
        assertThat(body.get("name").asText()).isEqualTo("Spade");
        assertThat(body.get("deposit").asDouble()).isEqualTo(15.0);
    }

    @Test
    void getToolById_unknownId_is404WithErrorBody() throws Exception {
        HttpResponse<String> response = get("/api/tools/99");
        assertThat(response.statusCode()).isEqualTo(404);
        assertThat(json.readTree(response.body()).has("error")).isTrue();
    }

    @Test
    void getToolById_nonNumericId_is400WithErrorBody() throws Exception {
        HttpResponse<String> response = get("/api/tools/abc");
        assertThat(response.statusCode()).isEqualTo(400);
        assertThat(json.readTree(response.body()).has("error")).isTrue();
    }

    // ---- Q8.3 POST /api/loans ---------------------------------------------------

    @Test
    void postLoan_createsALoan_with201() throws Exception {
        HttpResponse<String> response = post("/api/loans", "{\"memberId\":1,\"toolId\":1}");
        assertThat(response.statusCode()).isEqualTo(201);
        JsonNode body = json.readTree(response.body());
        assertThat(body.get("id").asLong()).isPositive();
        assertThat(body.get("toolId").asInt()).isEqualTo(1);
        assertThat(body.get("memberId").asInt()).isEqualTo(1);
        assertThat(body.get("borrowedOn").asText()).isEqualTo("2026-03-01");
    }

    @Test
    void postLoan_toolAlreadyOnLoan_is409() throws Exception {
        post("/api/loans", "{\"memberId\":1,\"toolId\":1}");
        HttpResponse<String> response = post("/api/loans", "{\"memberId\":2,\"toolId\":1}");
        assertThat(response.statusCode()).isEqualTo(409);
        assertThat(json.readTree(response.body()).has("error")).isTrue();
    }

    @Test
    void postLoan_fourthLoanForSameMember_is409() throws Exception {
        assertThat(post("/api/loans", "{\"memberId\":1,\"toolId\":1}").statusCode()).isEqualTo(201);
        assertThat(post("/api/loans", "{\"memberId\":1,\"toolId\":2}").statusCode()).isEqualTo(201);
        assertThat(post("/api/loans", "{\"memberId\":1,\"toolId\":3}").statusCode()).isEqualTo(201);
        assertThat(post("/api/loans", "{\"memberId\":1,\"toolId\":4}").statusCode()).isEqualTo(409);
    }

    @Test
    void postLoan_missingField_is400() throws Exception {
        assertThat(post("/api/loans", "{\"memberId\":1}").statusCode()).isEqualTo(400);
        assertThat(post("/api/loans", "{\"toolId\":1}").statusCode()).isEqualTo(400);
    }

    @Test
    void postLoan_malformedJson_is400() throws Exception {
        assertThat(post("/api/loans", "this is not json").statusCode()).isEqualTo(400);
    }

    @Test
    void postLoan_unknownTool_is404() throws Exception {
        assertThat(post("/api/loans", "{\"memberId\":1,\"toolId\":99}").statusCode()).isEqualTo(404);
    }

    // ---- helpers ----------------------------------------------------------------

    private HttpResponse<String> get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(base + path)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(String path, String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(base + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
