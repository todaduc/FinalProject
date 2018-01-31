package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.text.TextUtils;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Themener on 1/25/18.
 */


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


    public void testAlbumGetTask() throws InterruptedException {

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
