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
 * Created by Themener on 1/25/18.
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

    public EndpointsAsyncTask setListener(TaskListener listener) {
        this.mListener = listener;
        return this;
    }
    public static interface TaskListener {
        public void onComplete(String result);
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
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
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
            spinner.setVisibility(View.VISIBLE);
        }

        if (this.mListener != null)
            this.mListener.onComplete(result);

        Intent intent = new Intent(context, JokeDisplayActivity.class);
        intent.putExtra("joke", result);
        context.startActivity(intent);
    }

}
