package za.co.wethinkcode.toolshare;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class ToolShareApp {

    private final ToolCatalogue catalogue;
    private final LoanService loanService;

    public ToolShareApp(ToolCatalogue catalogue, LoanService loanService) {
        this.catalogue = catalogue;
        this.loanService = loanService;
    }

    /** Builds the application. The caller decides when and on which port to start it. */
    public Javalin create() {
        Javalin app = Javalin.create(config ->
                config.addStaticFiles("/public", Location.CLASSPATH));

        app.get("/health", ctx -> ctx.result("OK"));

        registerToolRoutes(app);
        registerLoanRoutes(app);
        return app;
    }

    private void registerToolRoutes(Javalin app) {
        // TODO (Q8.1): GET /api/tools
        // TODO (Q8.2): GET /api/tools/{id}
    }

    private void registerLoanRoutes(Javalin app) {
        // TODO (Q8.3): POST /api/loans
    }
}
