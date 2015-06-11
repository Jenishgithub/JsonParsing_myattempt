package com.example.jsonparsing_myattempt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	@SuppressWarnings("deprecation")
	DefaultHttpClient httpClient = new DefaultHttpClient();
	boolean isOnline;
	String getUrl = "http://djs-corner.appspot.com/getClosestClubs?lat=40.7600624&lon=-73.98558";
	// List<Club> ret = new ArrayList<>();
	@SuppressWarnings("deprecation")
	HttpResponse response = null;
	MySqliteHelper mahHelper;
	Button btncopy;
	Spinner spnViewName;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btncopy = (Button) findViewById(R.id.btncopy);
		spnViewName = (Spinner) findViewById(R.id.spnViewNames);

		mahHelper = new MySqliteHelper(getApplicationContext());
		isOnline = isOnline();
		if (checkPresenceOfDatabase() && isOnline()) {
			new NetworkOperation().execute();

		}

		// cur.moveToNext();
		int column;
		SQLiteDatabase db = mahHelper.getWritableDatabase();
		String query_names;
		query_names = "select name from " + MySqliteHelper.DATABASE_TABLE + ";";
		Cursor cur_names = db.rawQuery(query_names, null);
		column = cur_names.getCount();
		Log.d("crossover", "the no. of names of this query result is " + column);// 840
																					// columns
		List<String> names = new ArrayList<>();
		cur_names.moveToFirst();
		while (cur_names.isAfterLast() == false) {
			names.add(cur_names.getString(0));
			cur_names.moveToNext();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
				android.R.layout.simple_spinner_item, names);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnViewName.setAdapter(adapter);
	}

	private boolean isOnline() {
		// TODO Auto-generated method stub
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isConnectedOrConnecting();

	}

	public boolean checkPresenceOfDatabase() {
		SQLiteDatabase checkdb = null;
		try {
			String myPath = MySqliteHelper.DATABASE_PATH;
			File file = new File(myPath);
			if (file.exists() && !file.isDirectory()) {
				checkdb = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.OPEN_READONLY);
			}

		} catch (SQLiteException e) {
			// TODO: handle exception
		}
		if (checkdb != null)
			checkdb.close();

		return checkdb == null ? true : false;

	}

	public class NetworkOperation extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HttpGet getMethod = new HttpGet(getUrl);

			try {
				response = httpClient.execute(getMethod);
				// convert response to string
				String result = EntityUtils.toString(response.getEntity());
				JSONArray ja = new JSONArray(result);
				mahHelper.insertIntoDatabase(ja);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

	}

}
