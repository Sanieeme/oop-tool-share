package za.co.wethinkcode.toolshare;

/**
 * A person who borrows tools.
 *
 * TODO (Q2.2): this class does not protect its own data. See the README.
 */
public class Member {

    public String fullName;
    public String email;

    public Member(String fullName, String email) {
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
        this.fullName = newName;
    }
}
