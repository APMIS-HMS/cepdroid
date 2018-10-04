package ng.apmis.apmismobile;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UtilityTests {

    private static final String TAG = "UtilityTests";

    @Test
    public void camelHumpCharactersToUpperCase() {
        String testStr = "clinicalDocumentationOfTheWorld";
        int step = 0;

        String go = testStr;

        for (int count=0; count<testStr.length(); ++count) {
            if (Character.isUpperCase(testStr.charAt(count))) {
                go = go.replace("" + go.charAt(count+step), " " + go.charAt(count+step));
                ++step;
            }
        }

        go = go.replaceFirst(go.charAt(0)+"", (go.charAt(0)+"").toUpperCase() );

        Log.i(TAG, go);
        Log.i(TAG, testStr);
    }

}
