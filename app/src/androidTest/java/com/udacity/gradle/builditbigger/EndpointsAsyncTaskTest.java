package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.text.TextUtils;
import java.util.concurrent.CountDownLatch;


//Class to test the backend task.
public class EndpointsAsyncTaskTest extends InstrumentationTestCase {

    private String mResult = null;
    private CountDownLatch signal = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        signal = new CountDownLatch(1);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        signal.countDown();
    }

    /**
     * this method test the whether the result coming from the server is null or an empty string.
     * @throws InterruptedException
     */
    public void testEndpointTask() throws InterruptedException {

        Context appContext = InstrumentationRegistry.getTargetContext();
        EndpointsAsyncTask task = new EndpointsAsyncTask(appContext, null);

        assertNotNull(task);

        task.setListener(new EndpointsAsyncTask.TaskListener() {
            @Override
            public void onComplete(String result) {
                mResult = result;
                signal.countDown();
            }
        }).execute();

        signal.await();
        assertNotNull(mResult);
        assertFalse(TextUtils.isEmpty(mResult));

    }

}
