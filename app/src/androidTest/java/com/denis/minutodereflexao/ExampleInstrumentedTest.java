package com.denis.minutodereflexao;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
<<<<<<< HEAD
 * Instrumentation test, which will execute on an Android device.
=======
 * Instrumented test, which will execute on an Android device.
>>>>>>> versao2
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
<<<<<<< HEAD
    public void useAppContext() throws Exception {
=======
    public void useAppContext() {
>>>>>>> versao2
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.denis.minutodereflexao", appContext.getPackageName());
    }
}
