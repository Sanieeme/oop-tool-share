package za.co.wethinkcode.toolshare;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ToolTest {

    @Test
    void tool_isAbstract() {
        assertThat(Modifier.isAbstract(Tool.class.getModifiers())).isTrue();
    }

    @Test
    void category_deposit_andMaxLoanDays_areAbstract() throws NoSuchMethodException {
        for (String name : List.of("category", "deposit", "maxLoanDays")) {
            Method m = Tool.class.getDeclaredMethod(name);
            assertThat(Modifier.isAbstract(m.getModifiers())).as(name).isTrue();
        }
    }

    @Test
    void handTool_hasHandToolRules() {
        Tool hammer = new HandTool(1, "Claw Hammer");
        assertThat(hammer.category()).isEqualTo("HAND");
        assertThat(hammer.deposit()).isEqualTo(20.00);
        assertThat(hammer.maxLoanDays()).isEqualTo(7);
    }

    @Test
    void gardenTool_hasGardenToolRules() {
        Tool spade = new GardenTool(2, "Spade");
        assertThat(spade.category()).isEqualTo("GARDEN");
        assertThat(spade.deposit()).isEqualTo(15.00);
        assertThat(spade.maxLoanDays()).isEqualTo(5);
    }
}
