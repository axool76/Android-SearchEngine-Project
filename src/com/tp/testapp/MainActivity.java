package com.tp.testapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	EditText e;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		e = (EditText) findViewById(R.id.barreRecherche);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void lancerecherche(View v) {
		String eValue = e.getText().toString();
		
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		
		imm.toggleSoftInput(0, 0);
		
		if(eValue.length() == 0)
			Toast.makeText(getApplicationContext(), "Tape une recherche ducon !", Toast.LENGTH_LONG).show();
		else
		{
			Intent resultatRecherche = new Intent(MainActivity.this,ResultatRecherche.class);
			resultatRecherche.putExtra("research", e.getText().toString());
			startActivity(resultatRecherche);
			overridePendingTransition(R.anim.slid_in, R.anim.slid_out);
		}
	}

}
