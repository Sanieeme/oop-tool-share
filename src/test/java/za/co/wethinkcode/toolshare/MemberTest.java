package za.co.wethinkcode.toolshare;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @Test
    void allFields_arePrivate() {
        for (Field field : Member.class.getDeclaredFields()) {
            assertThat(Modifier.isPrivate(field.getModifiers()))
                    .as("field '" + field.getName() + "' should be private").isTrue();
        }
    }

    @Test
    void email_cannotBeReassigned() throws NoSuchFieldException {
        Field email = Member.class.getDeclaredField("email");
        assertThat(Modifier.isFinal(email.getModifiers())).as("email should be final").isTrue();
    }

    @Test
    void validMember_isCreated() {
        Member m = new Member("Ayanda Dlamini", "ayanda@example.com");
        assertThat(m.getFullName()).isEqualTo("Ayanda Dlamini");
        assertThat(m.getEmail()).isEqualTo("ayanda@example.com");
    }

    @Test
    void blankOrNullName_isRejected() {
        assertThatThrownBy(() -> new Member(null, "a@b.co")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Member("", "a@b.co")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Member("   ", "a@b.co")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void invalidEmail_isRejected() {
        assertThatThrownBy(() -> new Member("Ayanda", null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Member("Ayanda", "")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Member("Ayanda", "no-at-sign")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rename_changesTheName() {
        Member m = new Member("Ayanda Dlamini", "ayanda@example.com");
        m.rename("Ayanda Mokoena");
        assertThat(m.getFullName()).isEqualTo("Ayanda Mokoena");
    }

    @Test
    void rename_rejectsBlank_andLeavesNameUnchanged() {
        Member m = new Member("Ayanda Dlamini", "ayanda@example.com");
        assertThatThrownBy(() -> m.rename(" ")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> m.rename(null)).isInstanceOf(IllegalArgumentException.class);
        assertThat(m.getFullName()).isEqualTo("Ayanda Dlamini");
    }
}
