//@@author LowGinWee
package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class KpiTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Kpi(null));
    }

    @Test
    public void constructor_invalidKpi_throwsIllegalArgumentException() {
        String invalidKpi = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Kpi(invalidKpi));
    }

    @Test
    public void isValidKpi() {
        // null Kpi
        Assert.assertThrows(NullPointerException.class, () -> Kpi.isValidKpi(null));

        // invalid Kpi
        assertFalse(Kpi.isValidKpi("")); // empty string
        assertFalse(Kpi.isValidKpi(" ")); // spaces only
        assertFalse(Kpi.isValidKpi("6")); // larger than 5
        assertFalse(Kpi.isValidKpi("kpi")); // non-numeric
        assertFalse(Kpi.isValidKpi("0.000a0")); // alphabets within digits
        assertFalse(Kpi.isValidKpi("0. 0")); // spaces within digits
        assertFalse(Kpi.isValidKpi("-0")); // no negatives
        assertFalse(Kpi.isValidKpi("5.0001")); // float larger than 5

        // valid Kpi
        assertTrue(Kpi.isValidKpi("0"));
        assertTrue(Kpi.isValidKpi("5"));
        assertTrue(Kpi.isValidKpi("0.000000001")); // long floats
        assertTrue(Kpi.isValidKpi("4.999999999")); // long floats
        assertTrue(Kpi.isValidKpi("5.000000000")); // long floats
    }
}
