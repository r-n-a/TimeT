package com.example.timet;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	Button get, read;
	EditText mg;
	DB dbHelper;
	final String LOG_TAG = "myLogs";
	CheckBox checkboxSelect;
	CheckBox checkboxCreate;
	Spinner mgList;
	boolean onload=false;
	SQLiteDatabase db;
	String[] mgData;
	String[] mgId;
	int count;
	Cursor outMg;
	ArrayAdapter<String> adapter;
	int mgColIndex;
	int idColIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		onload=true;
		dbHelper = new DB(this);
		get = (Button) this.findViewById(R.id.button1);
		get.setOnClickListener(this);
		mg = (EditText) this.findViewById(R.id.editText1);
		mgList = (Spinner) this.findViewById(R.id.spinner1);
		checkboxSelect = (CheckBox)findViewById(R.id.checkBox1);
		checkboxCreate = (CheckBox)findViewById(R.id.checkBox2);
		mg.setEnabled(false);
    	get.setEnabled(false);
    	checkboxSelect.setEnabled(false);
    	ContentValues cv = new ContentValues();
		db = dbHelper.getWritableDatabase();

		outMg = db.query(dbHelper.TABLE_MG, null, null, null, null, null, null);
        count = outMg.getCount();
        mgData = new String[count];
        mgId = new String[count];
	      if (outMg.moveToFirst()) {
	        mgColIndex = outMg.getColumnIndex(dbHelper.MG);
	        idColIndex = outMg.getColumnIndex(dbHelper.ID);
	        for(int i=0; i<count; i++) {
	        	mgData[i] = outMg.getString(mgColIndex);
	        	mgId[i] = outMg.getString(idColIndex);
	        	outMg.moveToNext();
	        }
	        outMg.close();
	      }     
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mgData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       
        mgList.setAdapter(adapter);
        mgList.setSelection(-1);
        mgList.setOnItemSelectedListener(new OnItemSelectedListener() { 
		      @Override
		      public void onItemSelected(AdapterView<?> parent, View view,
		          int position, long id) {
		    	  if(!onload)
		    	  {  
			    	  Intent intent = new Intent(MainActivity.this, ViewEm.class);
		              Bundle b = new Bundle();
		              b.putString("mgID", mgId[position]); 
		              intent.putExtras(b);  
		              startActivity(intent);
		    	  }
		    	  onload=false;
		      }
		      @Override
		      public void onNothingSelected(AdapterView<?> arg0) {
		    	  To("Nothing selected");
		      }
	      });
		checkboxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	        @Override
	            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	                // TODO Auto-generated method stub
	                if (buttonView.isChecked()) {
	                	checkboxCreate.setChecked(false);
	                	checkboxSelect.setEnabled(false);
	                	checkboxCreate.setEnabled(true);
	                    mg.setEnabled(false);
	                	get.setEnabled(false);
	                	mgList.setEnabled(true); 
	                	mg.setText("");
	                }
	            }
	        });    
		
		checkboxCreate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (buttonView.isChecked()) {
                	checkboxSelect.setChecked(false);
                	checkboxCreate.setEnabled(false);
                	mg.setEnabled(true);
                	get.setEnabled(true);
                	mgList.setEnabled(false);
                	checkboxSelect.setEnabled(true);
                }
            }
        });	
		
	}
	
	@Override
	public void onClick(View v) {
		String tostMess;
	    ContentValues cv = new ContentValues();
	    
	    String mgStr = mg.getText().toString();

	    SQLiteDatabase db = dbHelper.getWritableDatabase();

	    switch (v.getId()) {
		    case R.id.button1:
				Log.d(LOG_TAG, "--- Insert in mytable: ---");	      
				cv.put(dbHelper.MG, mgStr);
				long rowID = db.insert(dbHelper.TABLE_MG, null, cv);
				Log.d(LOG_TAG, "row inserted, ID = " + rowID);
				checkboxCreate.setChecked(false);
				checkboxSelect.setEnabled(false);
				checkboxCreate.setEnabled(true);
				mg.setEnabled(false);
				get.setEnabled(false);
				mgList.setEnabled(true); 
				outMg = db.query(dbHelper.TABLE_MG, null, null, null, null, null, null);
		        count = outMg.getCount();
		        mgData = new String[count];
		        mgId = new String[count];
			      if (outMg.moveToFirst()) {
			        mgColIndex = outMg.getColumnIndex(dbHelper.MG);
			        idColIndex = outMg.getColumnIndex(dbHelper.ID);
			        for(int i=0; i<count; i++) {
			        	mgData[i] = outMg.getString(mgColIndex);
			        	mgId[i] = outMg.getString(idColIndex);
			        	outMg.moveToNext();
			        }
			        outMg.close();
			      } 
			    dbHelper.close();
				adapter.notifyDataSetChanged(); 
				mg.setText("");
				reload();
		        break;
	    }
	    
	 }
	
	public void onSpinner(View v) {
        int selectedPosition = 0;
    	Intent intent = new Intent(MainActivity.this, ViewEm.class);
        Bundle b = new Bundle();
        b.putString("mgID", mgId[selectedPosition]); 
        intent.putExtras(b);  
        startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void To(String v) {
		Toast.makeText(this, v,
					Toast.LENGTH_LONG).show();
	}
	
	private void reload()
    {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

}


