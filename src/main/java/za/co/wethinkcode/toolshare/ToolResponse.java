package za.co.wethinkcode.toolshare;

/** The JSON shape of a tool in the API. */
public record ToolResponse(long id, String name, String category, double deposit, int maxLoanDays) {

    public static ToolResponse from(Tool tool) {
        return new ToolResponse(tool.getId(), tool.getName(), tool.category(), tool.deposit(), tool.maxLoanDays());
    }
}
