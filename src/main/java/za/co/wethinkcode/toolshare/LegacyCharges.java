package za.co.wethinkcode.toolshare;

/**
 * Late-return charges. Written years ago by someone who has since left the company.
 * It is called by the nightly billing job, which is NOT in this repository, so the
 * signature of calc(...) must never change. There are no tests.
 */
public class LegacyCharges {

    public static double calc(String cat, int days, int allowed, boolean prem, int prior) {
        double r = 0;
        if (cat.equals("HAND")) {
            if (days > allowed) {
                r = (days - allowed) * 2.5;
            }
        } else if (cat.equals("POWER")) {
            if (days > allowed) {
                r = (days - allowed) * 7.5;
                if (days - allowed > 5) {
                    r = r + 20;
                }
            }
        } else if (cat.equals("GARDEN")) {
            if (days > allowed) {
                r = (days - allowed) * 4;
            }
            r = r + 3;
        } else {
            return -1;
        }
        if (prem) {
            r = r - r * 0.1;
        }
        if (prior > 3) {
            r = r + 5;
        }
        if (r > 100) {
            r = 100;
        }
        return Math.round(r * 100) / 100.0;
    }
}
