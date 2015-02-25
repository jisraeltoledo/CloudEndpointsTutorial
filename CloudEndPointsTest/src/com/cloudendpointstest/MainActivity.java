package com.cloudendpointstest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

import java.io.IOException;
import java.util.Date;

import android.os.AsyncTask;
import android.view.Menu;
import android.content.Context;

import com.cloudendpointstest.noteendpoint.Noteendpoint;
import com.cloudendpointstest.noteendpoint.model.Note;
//import com.cloudnotes.noteendpoint.Noteendpoint;
//import com.cloudnotes.noteendpoint.model.Note;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
//import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

/**
 * The Main Activity.
 * 
 * This activity starts up the RegisterActivity immediately, which communicates
 * with your App Engine backend using Cloud Endpoints. It also receives push
 * notifications from backend via Google Cloud Messaging (GCM).
 * 
 * Check out RegisterActivity.java for more details.
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new EndpointsTask().execute(getApplicationContext());
		// Start up RegisterActivity right away
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
		// Since this is just a wrapper to start the main activity,
		// finish it after launching RegisterActivity
		finish();
		new EndpointsTask().execute(getApplicationContext());
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.activity_main, menu);
	// return true;
	// }

	public class EndpointsTask extends AsyncTask<Context, Integer, Long> {
		protected Long doInBackground(Context... contexts) {

			Noteendpoint.Builder endpointBuilder = new Noteendpoint.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					new HttpRequestInitializer() {
						public void initialize(HttpRequest httpRequest) {
						}
					});
			Noteendpoint endpoint = CloudEndpointUtils.updateBuilder(
					endpointBuilder).build();
			try {
				Note note = new Note().setDescription("Note Description");
				String noteID = new Date().toString();
				note.setId(noteID);

				note.setEmailAddress("E-Mail Address");
				Note result = endpoint.insertNote(note).execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return (long) 0;
		}
	}
}
