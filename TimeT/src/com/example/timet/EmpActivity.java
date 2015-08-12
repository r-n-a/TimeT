package com.example.timet;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EmpActivity extends Activity {
	
	String mgId;
	TextView emp;
	EditText dateOf, timeArr, timeDep;
	Button addTime;
	DB dbHelper;
	SQLiteDatabase db;
	String selection = null;
    String[] selectionArgs = null;
	int TIME_L = 1;
	final String LOG_TAG = "myLogs";
	int myHour, myMinute;
	Cursor outMg;
	int emColIndex;
	int idColIndex;
	String mgData;
	String idData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emp_activity);
		
		emp = (TextView) this.findViewById(R.id.emp);
		dateOf = (EditText) this.findViewById(R.id.nowDate);
		timeArr = (EditText) this.findViewById(R.id.timeArr);
		timeArr.setInputType(InputType.TYPE_NULL);
		timeDep = (EditText) this.findViewById(R.id.timeDep);
		timeDep.setInputType(InputType.TYPE_NULL);
		addTime = (Button) this.findViewById(R.id.addTime);
		addTime.setInputType(InputType.TYPE_NULL);
		dbHelper = new DB(this);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		mgId = getIntent().getExtras().getString("mgID");
        int intCatId = Integer.parseInt(mgId); 
        
        dbHelper = new DB(this);
    	ContentValues cv = new ContentValues();
		db = dbHelper.getWritableDatabase();
		selection = "id = "+mgId;
		outMg = db.query(dbHelper.TABLE_EM, null, selection, selectionArgs, null, null, null);
		outMg.moveToFirst();
		emColIndex = outMg.getColumnIndex(dbHelper.NAME_SURNAME);
		idColIndex = outMg.getColumnIndex(dbHelper.ID);
		mgData = outMg.getString(emColIndex);
		idData = outMg.getString(emColIndex);
		outMg.close();      
		emp.setText(mgData);
	}
	
	protected void onResume() {
	    // TODO Auto-generated method stub
	    super.onResume();
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int minute = c.get(Calendar.MINUTE);
		int hour = c.get(Calendar.HOUR);
		dateOf.setText(day+"/"+month+"/"+day);
		timeArr.setText(hour+":"+minute);
		timeDep.setText(hour+":"+minute);
	}
	
   public void addEmTime(View v) {
		ContentValues cv = new ContentValues();
	    SQLiteDatabase db = dbHelper.getWritableDatabase();
	    Log.d(LOG_TAG, "--- Insert in mytable: ---");	      
		cv.put(dbHelper.ID_EM, mgId);
		cv.put(dbHelper.DATE, dateOf.getText().toString());
		cv.put(dbHelper.TIME_ARR, timeArr.getText().toString());
		cv.put(dbHelper.TIME_DEP, timeDep.getText().toString());
		long rowID = db.insert(dbHelper.TABLE_EM_DATA, null, cv);
		Log.d(LOG_TAG, "row inserted, ID = " + rowID);
		To("Wrote!");
   }
   
   public void onCancel(View v) {
	   finish();
   }
   
   public void onList(View v) {
	   Intent intent = new Intent(EmpActivity.this, ListTimeActivity.class);
       Bundle b = new Bundle();
       b.putString("mgID", mgId); 
       intent.putExtras(b);  
       startActivity(intent);
   }
   
   public void clickTimeArr(View v) {
	   TIME_L = 1;
	   showDialog(TIME_L);
   }
   
   public void clickTimeDep(View v) {
	   TIME_L = 2;
	   showDialog(TIME_L);
   }
   
   public void clickDate(View v) {
	   TIME_L = 3;
	   showDialog(TIME_L);
   }
   
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.emp, menu);
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
	
	 protected Dialog onCreateDialog(int id) {
		 Calendar c = Calendar.getInstance();
	      if (id == 1) {  
	    	  TimePickerDialog tpd = new TimePickerDialog(this, myCallBack1, 
	    			  c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true);
	    	  return tpd;
	      }
	      if (id == 2) {  
	    	  TimePickerDialog tpd = new TimePickerDialog(this, myCallBack2, 
	    			  c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true);
	    	  return tpd;
	      }
	      if (id == 3) {
	          DatePickerDialog tpd = new DatePickerDialog(this, myCallBack3, c.get(Calendar.YEAR),
	        		  c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
	          return tpd;
	        }
	      return super.onCreateDialog(id);
	}
	    
	    OnTimeSetListener myCallBack1 = new OnTimeSetListener() {
	    	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	    		if(minute < 10)
	    			timeArr.setText(hourOfDay+":0"+minute);
	    		else
	    			timeArr.setText(hourOfDay+":"+minute); 			
	    	}
	    };
	    
	    OnTimeSetListener myCallBack2 = new OnTimeSetListener() {
		    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		    	if(minute < 10)
		    		timeDep.setText(hourOfDay+":0"+minute);
	    		else
	    			timeDep.setText(hourOfDay+":"+minute);
		    }
		};
		
		 OnDateSetListener myCallBack3 = new OnDateSetListener() {
			    public void onDateSet(DatePicker view, int year, int monthOfYear,
			          int dayOfMonth) {			    	  
			    	  dateOf.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
			    }
		 };
	
}
