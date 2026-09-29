package za.co.wethinkcode.toolshare;

/** The JSON body of POST /api/loans. Either field may be null if the client left it out. */
public record LoanRequest(Long memberId, Long toolId) {
}
