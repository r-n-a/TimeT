package com.example.timet;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class ViewEm extends Activity {
	
	private ListView lv;

    EditText inputSearch;
    String mgId;
    DB dbHelper;
    String selection = null;
    String[] selectionArgs = null;
    String[] nsData;
    String[] pocData;
    String[] emId;
    private ArrayList<Product> mProductArrayList = new ArrayList<Product>();
    private MyAdapter adapter1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_em);
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		mgId = getIntent().getExtras().getString("mgID");
        int intCatId = Integer.parseInt(mgId);  
        
        inputSearch = (EditText) this.findViewById(R.id.inputSearch);
        lv = (ListView) this.findViewById(R.id.list_view);

		inputSearch.addTextChangedListener(new TextWatcher() {

	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	            // Call back the Adapter with current character to Filter
	            adapter1.getFilter().filter(s.toString());
	        }

	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count,int after) {
	        }

	        @Override
	        public void afterTextChanged(Editable s) {
	        }
	    });
        
	}
	
	protected void onResume() {
	    // TODO Auto-generated method stub
	    super.onResume();
	    
	    dbHelper = new DB(this);
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        selection = "idmg = "+mgId;
        Cursor outEmp = db.query(dbHelper.TABLE_EM, null, selection, selectionArgs, null, null, null);
        int countEmp = outEmp.getCount();
        
        if(countEmp > 0) {
	        nsData = new String[countEmp];
			pocData = new String[countEmp];                
			emId = new String[countEmp];        
			if (outEmp.moveToFirst()) {
				int nsColIndex = outEmp.getColumnIndex(dbHelper.NAME_SURNAME);
				int pocColIndex = outEmp.getColumnIndex(dbHelper.POCITON);
				int idColIndex = outEmp.getColumnIndex(dbHelper.ID);
				for(int i=0; i<countEmp; i++) {
					nsData[i] = outEmp.getString(nsColIndex);
					pocData[i] = outEmp.getString(pocColIndex);
					emId[i] = outEmp.getString(idColIndex);
					mProductArrayList.add(new Product(nsData[i], pocData[i]));
					outEmp.moveToNext();
				}
			} 
			outEmp.close();  			
			adapter1 = new MyAdapter(ViewEm.this, mProductArrayList);
			lv.setAdapter(adapter1); 
        } else 
        { 
        	To("No employers"); 
        }
  
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        
        menu.add(getString(R.string.add_emp));

        return super.onCreateOptionsMenu(menu);
     }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      // TODO Auto-generated method stub
      if(item.getTitle() == getString(R.string.add_emp))
    	  showSettings();
      return super.onOptionsItemSelected(item);
    }
	
	public void To(String v) {
		Toast.makeText(this, v,
					Toast.LENGTH_LONG).show();
	}
	
	 public void showSettings() {
	  	Intent i = new Intent(this, AddEmActivity.class);
	  	Bundle b = new Bundle();
        b.putString("mgID", mgId); 
        i.putExtras(b); 
	  	startActivity(i);
	 }
	 
	 public class MyAdapter extends BaseAdapter implements Filterable {

		 private ArrayList<Product> mOriginalValues; // Original Values
		 private ArrayList<Product> mDisplayedValues;    // Values to be displayed
		 LayoutInflater inflater;

		 public MyAdapter(Context context, ArrayList<Product> mProductArrayList) {
		     this.mOriginalValues = mProductArrayList;
		     this.mDisplayedValues = mProductArrayList;
		     inflater = LayoutInflater.from(context);
		 }

		 @Override
		 public int getCount() {
		     return mDisplayedValues.size();
		 }

		 @Override
		 public Object getItem(int position) {
		     return position;
		 }

		 @Override
		 public long getItemId(int position) {
		     return position;
		 }

		 private class ViewHolder {
		     LinearLayout llContainer;
		     TextView tvName,tvPrice;
		 }

		 @Override
		 public View getView(final int position, View convertView, ViewGroup parent) {

		     ViewHolder holder = null;

		     if (convertView == null) {
		         holder = new ViewHolder();
		         convertView = inflater.inflate(R.layout.list_item, null);
		         holder.llContainer = (LinearLayout)convertView.findViewById(R.id.llContainer);
		         holder.tvName = (TextView) convertView.findViewById(R.id.text1);
		         holder.tvPrice = (TextView) convertView.findViewById(R.id.text2);
		         convertView.setTag(holder);
		     } else {
		         holder = (ViewHolder) convertView.getTag();
		     }
		     holder.tvName.setText(mDisplayedValues.get(position).name);
		     holder.tvPrice.setText(mDisplayedValues.get(position).price+"");

		     holder.llContainer.setOnClickListener(new OnClickListener() {

		         public void onClick(View v) {
		            Intent intent = new Intent(ViewEm.this, EmpActivity.class);
		     	  	Bundle b = new Bundle();
		            b.putString("mgID", emId[position]); 
		            intent.putExtras(b); 
		     	  	startActivity(intent);
		         }
		     });

		     return convertView;
		 }

		 @Override
		 public Filter getFilter() {
		     Filter filter = new Filter() {

		         @SuppressWarnings("unchecked")
		         @Override
		         protected void publishResults(CharSequence constraint,FilterResults results) {

		             mDisplayedValues = (ArrayList<Product>) results.values; // has the filtered values
		             notifyDataSetChanged();  // notifies the data with new filtered values
		         }

		         @Override
		         protected FilterResults performFiltering(CharSequence constraint) {
		             FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
		             ArrayList<Product> FilteredArrList = new ArrayList<Product>();

		             if (mOriginalValues == null) {
		                 mOriginalValues = new ArrayList<Product>(mDisplayedValues); // saves the original data in mOriginalValues
		             }

		             /********
		              * 
		              *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
		              *  else does the Filtering and returns FilteredArrList(Filtered)  
		              *
		              ********/
		             if (constraint == null || constraint.length() == 0) {

		                 // set the Original result to return  
		                 results.count = mOriginalValues.size();
		                 results.values = mOriginalValues;
		             } else {
		                 constraint = constraint.toString().toLowerCase();
		                 for (int i = 0; i < mOriginalValues.size(); i++) {
		                     String data = mOriginalValues.get(i).name;
		                     if (data.toLowerCase().startsWith(constraint.toString())) {
		                         FilteredArrList.add(new Product(mOriginalValues.get(i).name,mOriginalValues.get(i).price));
		                     }
		                 }
		                 // set the Filtered result to return
		                 results.count = FilteredArrList.size();
		                 results.values = FilteredArrList;
		             }
		             return results;
		         }
		     };
		     return filter;
		 }
	}

}
