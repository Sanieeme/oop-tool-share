package za.co.wethinkcode.toolshare;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PowerToolTest {

    @Test
    void category_isPower() {
        assertThat(new PowerTool(1, "Cordless Drill", 800).category()).isEqualTo("POWER");
    }

    @Test
    void deposit_isOneHundredRand() {
        assertThat(new PowerTool(1, "Cordless Drill", 800).deposit()).isEqualTo(100.00);
    }

    @Test
    void maxLoanDays_isThree() {
        assertThat(new PowerTool(1, "Cordless Drill", 800).maxLoanDays()).isEqualTo(3);
    }

    @Test
    void wattage_isStored() {
        assertThat(new PowerTool(1, "Angle Grinder", 1200).getWattage()).isEqualTo(1200);
    }

    @Test
    void wattage_mustBePositive() {
        assertThatThrownBy(() -> new PowerTool(1, "Broken", 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new PowerTool(1, "Broken", -50)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void powerTool_isATool_soCallersNeedNotKnowTheDifference() {
        Tool tool = new PowerTool(7, "Jigsaw", 500);
        assertThat(tool.getId()).isEqualTo(7);
        assertThat(tool.getName()).isEqualTo("Jigsaw");
        assertThat(tool.toString()).contains("PowerTool").contains("Jigsaw");
    }
}
