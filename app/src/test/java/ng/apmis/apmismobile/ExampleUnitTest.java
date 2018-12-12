package ng.apmis.apmismobile;

import org.junit.Test;

import ng.apmis.apmismobile.utilities.AppUtils;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void checkExpiryFormat () {
        System.out.print(AppUtils.formatExpiryWithSlash("080"));
    }
}