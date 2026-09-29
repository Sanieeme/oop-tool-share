package za.co.wethinkcode.toolshare;

import io.javalin.Javalin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;

/** Q8.4: only checks that the page is served and wired up. Your marker will open it in a browser. */
class StaticPageTest {

    private Javalin app;

    @BeforeEach
    void setUp() {
        LoanService service = new LoanService(new InMemoryLoanRepository(), Clock.systemUTC());
        app = new ToolShareApp(InMemoryToolCatalogue.seeded(), service).create().start(0);
    }

    @AfterEach
    void tearDown() {
        app.stop();
    }

    @Test
    void indexPage_isServed_andFetchesTheToolsApi() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + app.port() + "/")).GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body())
                .contains("id=\"tool-list\"")
                .contains("fetch(")
                .contains("/api/tools");
    }
}
