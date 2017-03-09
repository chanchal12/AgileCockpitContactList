package com.agilecockpit.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	static final String dbName = "contact_db";

	// common column

	public static final String COL_ID = "id";
	public static final String Row_ID = BaseColumns._ID;
	public static final String COL_DATE = "created_date";
	public static final String COL_NAME = "name";
	public static final String TABLE_CONTACT = "tbl_contact";
	public static final String TABLE_DEL_CONTACT = "tbl_del_contact";



	private static final int DATABASE_VERSION = 1;
	static Context context = null;

	public DatabaseHelper(Context context) {
		super(context, dbName, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("Database Helper:", "Database helper....");
		createTables(db);
	}

	void createTables(SQLiteDatabase db) {
		create_tbl_contact(db);
		create_tbl_del_contact(db);
	}

	private void create_tbl_contact(SQLiteDatabase db) {
		try {
			String str_xml = "CREATE TABLE " + TABLE_CONTACT + "("
					+ Row_ID + " integer primary key, "
					+ COL_ID+ " integer, "
					+ COL_NAME + " varchar,"
					+ COL_DATE + " varchar)";

			db.execSQL(str_xml);
			Log.d("Table: ", "Table creation query: " + str_xml);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	private void create_tbl_del_contact(SQLiteDatabase db) {
		try {
			String str_xml = "CREATE TABLE " + TABLE_DEL_CONTACT + "("
					+ Row_ID + " integer primary key, "
					+ COL_ID+ " integer, "
					+ COL_DATE + " varchar)";

			db.execSQL(str_xml);
			Log.d("Table: ", "Table creation query: " + str_xml);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("Tag", "OnUpgrade has been called....");
		if (newVersion > oldVersion) {
			createTables(db);
			return;
		}
	}

	public static void close(Cursor cursor) {
		if (cursor != null) {
			try {
				cursor.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void close(SQLiteDatabase db) {
		try {
			db.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void close(DatabaseHelper dbHelper) {
		if (dbHelper != null) {
			try {
				dbHelper.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
