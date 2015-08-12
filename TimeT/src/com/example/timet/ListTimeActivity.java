package com.example.timet;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ListTimeActivity extends Activity {

	 private ArrayList<HashMap<String, Object>> timeList;
	 private static final String DATE = "date"; 
	 private static final String TIME_ARR = "arr"; 
	 private static final String TIME_DEP = "dep"; 
	 DB dbHelper;
	 SQLiteDatabase db;
	 String mgId;
	 String selection;
	 String[] selectionArgs;
	 Cursor outMg;
	 int dateColIndex;
	 int arrColIndex;
	 int depColIndex;
	 SimpleAdapter adapter;
	 int CM_DELETE_ID = 1;
	 String dateEm, arrEm, depEm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_time);
		mgId = getIntent().getExtras().getString("mgID");
        int intCatId = Integer.parseInt(mgId); 
        ListView listView = (ListView) findViewById(R.id.list_time);
        dbHelper = new DB(this);
    	ContentValues cv = new ContentValues();
		db = dbHelper.getWritableDatabase();
		selection = "idem = "+mgId;
		outMg = db.query(dbHelper.TABLE_EM_DATA, null, selection, selectionArgs, null, null, null);
		int count = outMg.getCount();
		timeList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> hm;
		if (outMg.moveToFirst()) {
			dateColIndex = outMg.getColumnIndex(dbHelper.DATE);
			arrColIndex = outMg.getColumnIndex(dbHelper.TIME_ARR);
			depColIndex = outMg.getColumnIndex(dbHelper.TIME_DEP);
			dateEm = getString(R.string.date);
			arrEm = getString(R.string.time_arr);
			depEm = getString(R.string.time_dep);
	        for(int i=0; i<count; i++) {
	        	hm = new HashMap<String, Object>();
	            hm.put(DATE, dateEm+": "+outMg.getString(dateColIndex));
	            hm.put(TIME_ARR, arrEm+": "+outMg.getString(arrColIndex));
	            hm.put(TIME_DEP, depEm+": "+outMg.getString(depColIndex));
	            timeList.add(hm);
	        	outMg.moveToNext();
	        }
	        outMg.close();
	    } 
	    dbHelper.close();

        adapter = new SimpleAdapter(this, timeList,
                R.layout.list_time, new String[]{DATE, TIME_ARR, TIME_DEP},
                new int[]{R.id.textDate, R.id.textTimeArr, R.id.textTimeDep});
        listView.setAdapter(adapter);
    }
    
    public void To(String v) {
		Toast.makeText(this, v,
					Toast.LENGTH_LONG).show();
	}

}
