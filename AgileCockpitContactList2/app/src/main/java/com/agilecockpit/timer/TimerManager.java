package com.agilecockpit.timer;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.agilecockpit.ui.ContactListActivity;

import java.util.Date;
import java.util.TimerTask;

public class TimerManager extends TimerTask {
	
private Activity activity;
	
	public TimerManager(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public void run() {
		try{
		Log.i("Timer Task executed...", "Hello World" + new Date());
					Intent intent = new Intent(activity, ContactListActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("EXIT", true);
					activity.startActivity(intent);
					activity.finish();
		}
		catch(Exception e ){
			e.printStackTrace();
		}
}
}
