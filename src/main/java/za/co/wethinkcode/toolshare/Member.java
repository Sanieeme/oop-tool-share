package za.co.wethinkcode.toolshare;

/**
 * A person who borrows tools.
 *
 * TODO (Q2.2): this class does not protect its own data. See the README.
 */
public class Member {

    private String fullName;
    private final String email;

    public Member(String fullName, String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Invalid full name");
        }
        this.fullName = fullName;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }

        this.fullName = newName;
    }
}
