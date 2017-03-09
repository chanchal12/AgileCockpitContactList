package com.agilecockpit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.agilecockpit.network.models.ContactListDto;

public class ContactListDBHandler {
	static String TAG = "ContactListDBHandler";

		public static long insert(Context context, ContactListDto.ContactDto contactDto) {
			DatabaseHelper helper = null;
			long lid = 0;
			SQLiteDatabase db = null;
			try{
				helper = new DatabaseHelper(context);
				db = helper.getWritableDatabase();
				ContentValues contentValue = new ContentValues();
				contentValue.put(DatabaseHelper.COL_ID, contactDto.getUid());
				contentValue.put(DatabaseHelper.COL_NAME, contactDto.getName());
				lid = db.insert(DatabaseHelper.TABLE_CONTACT, null, contentValue);
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				DatabaseHelper.close(db);
				DatabaseHelper.close(helper);
			}
			return lid;
		}

	public static long saveOrUpdate(Context context,ContactListDto.ContactDto contactDto) {

			long rowid = 0;
			DatabaseHelper helper = null;
			SQLiteDatabase db = null;
			long lid = isExist(context, Integer.parseInt(contactDto.getUid()));
			if (lid < 0) {
				return insert(context, contactDto);
			} else {
				try {
					ContentValues contentValue = new ContentValues();
					contentValue.put(DatabaseHelper.COL_NAME, contactDto.getName());
				;

					helper = new DatabaseHelper(context);
					db = helper.getWritableDatabase();
					rowid = db.update(DatabaseHelper.TABLE_CONTACT, contentValue, DatabaseHelper.Row_ID + "=" + lid, null);
				} catch (Exception e) {
					e.printStackTrace();

				} finally {
					DatabaseHelper.close(db);
					DatabaseHelper.close(helper);
				}
				return rowid;
		}
	}


	public static long isExist(Context context, int id) {
		DatabaseHelper helper = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try{
			helper = new DatabaseHelper(context);
			String[] columns = {DatabaseHelper.Row_ID, DatabaseHelper.COL_ID,DatabaseHelper.COL_NAME};
			db = helper.getWritableDatabase();
			cursor = db.query(DatabaseHelper.TABLE_CONTACT, columns, DatabaseHelper.COL_ID + " = " + id, null, null, null, null);
			if(cursor != null && cursor.moveToFirst()) {
				String name = cursor.getString(2);
				if(name == null || name.length() == 0) {
					return -1;
				}
				return cursor.getInt(0);
			}
		}catch (Exception e) {
			e.printStackTrace();

		} finally {
			DatabaseHelper.close(cursor);
			DatabaseHelper.close(db);
			DatabaseHelper.close(helper);
		}
		return -1;
	}

	public static int deleteContact(Context context, int id) {
		DatabaseHelper helper = null;
		SQLiteDatabase db = null;
		try {
			helper = new DatabaseHelper(context);
			db = helper.getWritableDatabase();
			return db.delete(DatabaseHelper.TABLE_CONTACT, DatabaseHelper.COL_ID + "=" +id , null );
		} catch(Exception ex) {
			ex.printStackTrace();

		} finally {
			DatabaseHelper.close(db);
			DatabaseHelper.close(helper);
		}
		return -1;
	}
}
