package za.co.wethinkcode.toolshare;

/** Something a member can borrow. Every kind of tool has its own deposit and loan period. */
public abstract class Tool {

    private final long id;
    private final String name;

    protected Tool(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /** "HAND", "POWER" or "GARDEN". */
    public abstract String category();

    /** Refundable deposit, in rand. */
    public abstract double deposit();

    /** The number of days a member may keep the tool. */
    public abstract int maxLoanDays();

    @Override
    public String toString() {
        return String.format("%s[id=%d, name=%s, deposit=%.2f, maxLoanDays=%d]",
                getClass().getSimpleName(), id, name, deposit(), maxLoanDays());
    }
}
