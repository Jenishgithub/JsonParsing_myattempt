package com.example.jsonparsing_myattempt;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "jsondb";
	public static final String DATABASE_TABLE = "jsontable";
	public static int DATABASE_VERSION = 1;
	public static String DATABASE_PATH = null;

	public MySqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getPath();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table if not exists "
				+ DATABASE_TABLE
				+ "(id long, zip text, lon double, address text, name text, number text, url text, lat double, country text);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("delete from " + DATABASE_NAME);
		onCreate(db);
	}

	public void insertIntoDatabase(JSONArray ja) throws JSONException {
		// TODO Auto-generated method stub
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			Iterator<?> keys = jo.keys();
			ContentValues cv = new ContentValues();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = jo.getString(key);
				cv.put(key, value);
			}
			db.insert(DATABASE_TABLE, null, cv);
		}
	}

}
