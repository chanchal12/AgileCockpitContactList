package com.agilecockpit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.agilecockpit.network.models.ContactListDto;

import java.util.ArrayList;

public class DeleteContactListDBHandler {
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
				lid = db.insert(DatabaseHelper.TABLE_DEL_CONTACT, null, contentValue);
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				DatabaseHelper.close(db);
				DatabaseHelper.close(helper);
			}
			return lid;
		}



	public static ArrayList<Integer> getDeleteContactIds(Context context) {
		DatabaseHelper dbHelper = null;
		SQLiteDatabase sqdb = null;
		Cursor cursor = null;

		ArrayList<Integer> list = null;
		try {
			dbHelper = new DatabaseHelper(context);
			sqdb = dbHelper.getWritableDatabase();
			cursor = sqdb.query(DatabaseHelper.TABLE_DEL_CONTACT, new String[]{DatabaseHelper.COL_ID}, null, null, null, null, null);
			list = new ArrayList<Integer>() ;
			while(cursor.moveToNext()) {
				list.add(cursor.getInt(0));
			}
		} catch(Exception ex) {
			ex.printStackTrace();

		} finally {
			DatabaseHelper.close(cursor);
			DatabaseHelper.close(sqdb);
			DatabaseHelper.close(dbHelper);
		}
		return list;
	}
}
