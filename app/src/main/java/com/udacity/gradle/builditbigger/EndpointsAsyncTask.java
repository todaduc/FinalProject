package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.themener.jokedisplaylib.JokeDisplayActivity;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

/**
 * Async task in charge of connecting with the backend server and retrieving the available server resources.
 */

public  class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {
    private MyApi myApiService = null;
    private Context context;
    private TaskListener mListener;
    private ProgressBar spinner;

    public EndpointsAsyncTask(Context context, ProgressBar spinner){
        this.context = context;
        this.spinner = spinner;
    }

    /**
     * Sets the task listener.
     * @param listener
     * @return
     */
    public EndpointsAsyncTask setListener(TaskListener listener) {
        this.mListener = listener;
        return this;
    }

    // Listener to indicate when the task has complete his job.
    public interface TaskListener {
        void onComplete(String result);
    }

    @Override
    protected void onPreExecute() {
        if (spinner != null){
            spinner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl(context.getString(R.string.root_url))
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }


        try {
            return myApiService.requestJoke().execute().getData();

        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {

        if (spinner != null){
            spinner.setVisibility(View.GONE);
        }

        if (this.mListener != null)
            this.mListener.onComplete(result);

        Intent intent = new Intent(context, JokeDisplayActivity.class);
        intent.putExtra(context.getString(R.string.intent_id), result);
        context.startActivity(intent);
    }

}
