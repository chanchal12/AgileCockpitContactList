package com.agilecockpit.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


import com.agilecockpit.R;
import com.agilecockpit.timer.TimerManager;

import java.util.Timer;

public class SplashScreen extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		catch(Exception e ){
			e.printStackTrace();
		}
		new Timer().schedule(new TimerManager(this), 3000);
	}

	
	
	

}

