package com.example.timet;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEmActivity extends Activity {

	String mgId;
	Button ok, cancel;
	EditText ns;
	EditText poc;
	DB dbHelper;
	final String LOG_TAG = "myLogs";
	String tostMess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_em);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		mgId = getIntent().getExtras().getString("mgID");
        int intCatId = Integer.parseInt(mgId); 
        ns = (EditText) this.findViewById(R.id.ns);
        poc = (EditText) this.findViewById(R.id.poc);
        ok = (Button) this.findViewById(R.id.addEm);
        cancel = (Button) this.findViewById(R.id.cancelEm);
        dbHelper = new DB(this);
	}
	
	public void onAdd(View v) { 
		ContentValues cv = new ContentValues();
	    String nsStr = ns.getText().toString();
	    String pocStr = poc.getText().toString();
	    SQLiteDatabase db = dbHelper.getWritableDatabase();
	    Log.d(LOG_TAG, "--- Insert in mytable: ---");	      
	    cv.put(dbHelper.NAME_SURNAME, nsStr);
	    cv.put(dbHelper.POCITON, pocStr);
	    cv.put(dbHelper.ID_MG, mgId);
	    long rowID = db.insert(dbHelper.TABLE_EM, null, cv);
	    dbHelper.close();
	    Log.d(LOG_TAG, "row inserted, ID = " + rowID);
	    finish();
	}
	
	public void onCancel(View v) { 
		finish();
	}
	
	public void To(String v) {
		Toast.makeText(this, v,
					Toast.LENGTH_LONG).show();
	} 
	
}
