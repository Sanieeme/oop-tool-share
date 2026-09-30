package za.co.wethinkcode.toolshare;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.toolshare.LegacyCharges.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO (Q3.1): write characterization tests for LegacyCharges.calc(...) here.
 * They must PASS against the code exactly as it stands today.
 */
class LegacyChargesCharacterizationTest {
    @Test
    void hand_notOverdue() {
        LegacyCharges charges = new LegacyCharges();

        assertThat(charges.calc("HAND", 7, 7, false, 0))
                .isEqualTo(0.0);
    }
    @Test
    void hand_overdue() {
        LegacyCharges charges = new LegacyCharges();
        assertThat(charges.calc("HAND", 10, 7, false, 0))
                .isEqualTo(7.5);
    }
    @Test
    void power_notOverdue() {
        LegacyCharges charges = new LegacyCharges();

        assertThat(charges.calc("POWER", 7, 7, false, 0))
                .isEqualTo(0.0);
    }
    @Test
    void power_overdue() {
        LegacyCharges charges = new LegacyCharges();
        assertThat(charges.calc("POWER", 10, 7, false, 0))
                .isEqualTo(22.5);
    }
    @Test
    void garden_notOverdue() {
        LegacyCharges charges = new LegacyCharges();

        assertThat(charges.calc("GARDEN", 7, 7, false, 0))
                .isEqualTo(3.0);
    }
    @Test
    void garden_overdue() {
        LegacyCharges charges = new LegacyCharges();
        assertThat(charges.calc("GARDEN", 10, 7, false, 0))
                .isEqualTo(15.0);
    }
    @Test
    void premiumMember_getsTenPercentDiscount() {
        assertThat(LegacyCharges.calc("HAND", 10, 7, true, 0))
                .isEqualTo(6.75);
    }

    @Test
    void priorOverdueLoans_moreThanThree_addsFive() {
        assertThat(LegacyCharges.calc("HAND", 10, 7, false, 4))
                .isEqualTo(12.5);
    }

    @Test
    void charge_isCappedAt100() {
        assertThat(LegacyCharges.calc("POWER", 50, 7, false, 0))
                .isEqualTo(100.0);
    }

}
