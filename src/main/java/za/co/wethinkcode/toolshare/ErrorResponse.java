package za.co.wethinkcode.toolshare;

/** The JSON shape of every error the API returns: {"error": "..."} */
public record ErrorResponse(String error) {
}
