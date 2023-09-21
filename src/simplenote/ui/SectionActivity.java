package simplenote.ui;

import java.util.ArrayList;
import java.util.List;

import simplenote.publics.record;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SectionActivity extends Activity {
	
	private AdvancedDragListView Section; 
	private AdvancedDragListAdapter SAdapter;
	
	private AlertDialog.Builder DeleteRecord;
	
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_section);
		name = getIntent().getExtras().getString("name");
		setTitle(name);
		simplenote.filesystem.read.Section(name);
		MainteinDisplay(0, simplenote.publics.datas.SectionCache.size());
		//-----------------------------------------------------------------------------------
		Section = (AdvancedDragListView) findViewById(R.id.Section);
		SAdapter = new AdvancedDragListAdapter(this, simplenote.publics.datas.SectionCache);
		registerForContextMenu(Section);
		Section.setAdapter(SAdapter);
		Section.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Intent EditorIntent=new Intent();
				EditorIntent.setClass(SectionActivity.this, EditorActivity.class );
				EditorIntent.putExtra("name", name);
				EditorIntent.putExtra("Record id", arg2);
				startActivity(EditorIntent);
			}
		});
		Section.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu conMenu, View view , ContextMenuInfo info) {       
				getMenuInflater().inflate(R.menu.section_context, conMenu);
		   }    
		});

	}

	protected void onPause() {
		super.onPause();
		simplenote.filesystem.write.Section(name);
	}
	
	protected void onStop() {
		super.onStop();
		simplenote.filesystem.write.Section(name);
	}
	
	protected void onDestroy() {
		super.onDestroy();
		simplenote.filesystem.write.Section(name);
	}
	
	protected void onRestart() {
		super.onRestart();
		MainteinDisplay(0, simplenote.publics.datas.SectionCache.size());
		Section = (AdvancedDragListView) findViewById(R.id.Section);
		SAdapter = new AdvancedDragListAdapter(this, simplenote.publics.datas.SectionCache);
		Section.setAdapter(SAdapter);
	}
	
	protected void onStart() {
		super.onStart();
		MainteinDisplay(0, simplenote.publics.datas.SectionCache.size());
		Section = (AdvancedDragListView) findViewById(R.id.Section);
		SAdapter = new AdvancedDragListAdapter(this, simplenote.publics.datas.SectionCache);
		Section.setAdapter(SAdapter);
	}
	
	protected void onResume() {
		super.onResume();
		MainteinDisplay(0, simplenote.publics.datas.SectionCache.size());
		Section = (AdvancedDragListView) findViewById(R.id.Section);
		SAdapter = new AdvancedDragListAdapter(this, simplenote.publics.datas.SectionCache);
		Section.setAdapter(SAdapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.section, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		//---------------Refresh: maintein itself--------------------
        case R.id.Refresh: {
        	MainteinDisplay(0, simplenote.publics.datas.SectionCache.size());
    		Section = (AdvancedDragListView) findViewById(R.id.Section);
    		SAdapter = new AdvancedDragListAdapter(this, simplenote.publics.datas.SectionCache);
    		Section.setAdapter(SAdapter);
			return true;
		}
		//---------------Create Record: easy. ----------------
        case R.id.CreateRecord: {
        	record singleRecord = new record();
        	singleRecord.level = 0;
        	singleRecord.fold = 0;
        	singleRecord.title = "Untitled Record";
        	singleRecord.content = "";
			simplenote.publics.datas.SectionCache.add(singleRecord); //dafault: add to the end of the records. 
			MainteinDisplay(0, simplenote.publics.datas.SectionCache.size());
			Section = (AdvancedDragListView) findViewById(R.id.Section);
			SAdapter = new AdvancedDragListAdapter(this, simplenote.publics.datas.SectionCache);
			Section.setAdapter(SAdapter);
			//---------------call for Editor Activity... it will explain. ------------------
			Intent EditorIntent=new Intent();
			EditorIntent.setClass(SectionActivity.this, EditorActivity.class );
			EditorIntent.putExtra("name", name);
			EditorIntent.putExtra("Record id", simplenote.publics.datas.SectionCache.size()-1);
			startActivity(EditorIntent);
			return true;
        }
      //---------------Save Manually: the ultimate failsafe. will be introduced to the EditorActivity--------------------
        case R.id.SaveManually: {
        	simplenote.filesystem.write.Section(name);
        }
        default:
        	break;
        }
		return false;
	}
	
	@Override       
    public boolean onContextItemSelected(MenuItem item) {         
         switch (item.getItemId()) {       
   	  				//---------------Let's get the Goddamned dialog involved. --------------------
              case R.id.DeleteRecord:  {
            	  final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            	  record singleRecord = (record) Section.getAdapter().getItem((int) info.position);
            	  /* Get the selected item out of the Adapter by its position. */    
            	  DeleteRecord = new AlertDialog.Builder(SectionActivity.this);
            	  DeleteRecord.setTitle("Delete Record");
            	  DeleteRecord.setMessage("Are you sure? ");
            	  DeleteRecord.setPositiveButton("Accept", new android.content.DialogInterface.OnClickListener(){
            		  public void onClick(DialogInterface DeleteRecord, int which) {
            			  simplenote.publics.datas.SectionCache.remove(info.position);
            			  MainteinDisplay(0, simplenote.publics.datas.SectionCache.size());
            			  Section = (AdvancedDragListView) findViewById(R.id.Section);
            			  SAdapter = new AdvancedDragListAdapter(SectionActivity.this, simplenote.publics.datas.SectionCache);
            			  Section.setAdapter(SAdapter);
            			  DeleteRecord.dismiss();  
        					// TODO Auto-generated method stub
        				}
            	  });
            	  DeleteRecord.setNegativeButton("Decline", new android.content.DialogInterface.OnClickListener() {
            		  public void onClick(DialogInterface DeleteRecord, int which) {
            			  DeleteRecord.dismiss();  
      						// TODO Auto-generated method stub
      					}
            	  });
            	  DeleteRecord.show();
                  return true;     
              }     
         }       
         return false;       
    }       
		
	public int findSubRecords(int from) { 
		//from+1 to return value-1 is the Sub Records of the "from". 
		//from to return value is two adj. same-level records 
		//with the deeper level between them. 
		int i = from;
		int rootLevel = simplenote.publics.datas.SectionCache.get(i).level;
		//you can't push it outta bound. 
		if (i == simplenote.publics.datas.SectionCache.size()-1) return i+1;
		do {i++;} while ((i<simplenote.publics.datas.SectionCache.size()-1) && (simplenote.publics.datas.SectionCache.get(i).level > rootLevel));
		//if you don't push it to the (maybe) next level, it's gonna NOT make a move. 
		if (i == simplenote.publics.datas.SectionCache.size()-1) return i+1; else return i;
	}

	public void MainteinDisplay(int from, int to) { //recrusive method to determine the boolean gottaInvisible of a record
		if ((from == to) && (from == 0)) return; 
		else if (from == to) {
			simplenote.publics.datas.SectionCache.get(from).gottaInvisible = false;
		} else {
			int fromTemp = from;
			int toTemp = from;
			while (toTemp < to) {
				toTemp = findSubRecords(fromTemp);
				simplenote.publics.datas.SectionCache.get(fromTemp).gottaInvisible = false;
				if (toTemp - fromTemp > 1) {
					if (simplenote.publics.datas.SectionCache.get(fromTemp).fold == 1) {
						for (int i = fromTemp + 1;i < toTemp;i++) {
							simplenote.publics.datas.SectionCache.get(i).gottaInvisible = true;
							}
						} else {
							MainteinDisplay(fromTemp+1, toTemp-1);
						}
					}
				fromTemp = toTemp;
			}
		}
	}
	
	public static class AdvancedDragListAdapter extends ArrayAdapter<record> {

        public AdvancedDragListAdapter(Context context, List<record> objects) {
            super(context, 0, objects);
        }
        
        public ArrayList<record> getList(){
            return simplenote.publics.datas.SectionCache;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	View view = convertView;
            view = LayoutInflater.from(getContext()).inflate(R.layout.record, null);
            record singleRecord = getItem(position);
            view.setPadding(25 * (singleRecord.level), view
                    .getPaddingTop(), 0, view.getPaddingBottom());
            view.setVisibility(View.VISIBLE);
            TextView RTitle = (TextView)view.findViewById(R.id.RTitle);
            RTitle.setText(singleRecord.title);
            TextView RContent = (TextView)view.findViewById(R.id.RContent);
            RContent.setText(simplenote.publics.datas.limit(singleRecord.content)); 
            ImageView RDragListItemImage = (ImageView)view.findViewById(R.id.RDragListItemImage);
            ImageView FoldItemImage = (ImageView)view.findViewById(R.id.FoldItemImage);
            if (singleRecord.fold == 1) {
                FoldItemImage.setImageResource(R.drawable.outline_list_collapse);
            } else {
            	FoldItemImage.setImageResource(R.drawable.outline_list_expand);
            }
            if (singleRecord.gottaInvisible){ 
            	//I'm gonna set up a bunch of 
            	//special complex methods to determine 
            	//the visibility of every single record 
            	//as the response of every fuckin' operation. 
                view.setVisibility(View.INVISIBLE);
            } else {
            	view.setVisibility(View.VISIBLE);
            }
            return view;
        }
	}
}
