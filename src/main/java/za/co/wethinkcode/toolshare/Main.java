package za.co.wethinkcode.toolshare;

import java.time.Clock;

public class Main {

    public static void main(String[] args) {
        LoanService loanService = new LoanService(new InMemoryLoanRepository(), Clock.systemDefaultZone());
        new ToolShareApp(InMemoryToolCatalogue.seeded(), loanService)
                .create()
                .start(7070);
    }
}
