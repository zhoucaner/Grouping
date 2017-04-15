package com.example.zhou.grouping.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.zhou.grouping.R;

public class SplashActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Handler x = new Handler();
		x.postDelayed(new splashhandler(), 2000);

	}

	class splashhandler implements Runnable {

		public void run() {
			startActivity(new Intent(getApplication(), LoginActivity.class));
			SplashActivity.this.finish();
		}

	}

}
