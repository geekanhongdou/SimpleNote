package simplenote.ui;

import java.util.ArrayList;
import java.util.List;

import simplenote.publics.element;
import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextPaint;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SectionGroupActivity extends Activity {
	
	private DragListView SectionGroup; 
	private DragListAdapter SGAdapter;
	
	private AlertDialog.Builder CreateSectionGroup;
	private AlertDialog.Builder CreateSection;  
	private AlertDialog.Builder RenameSectionGroup;
	private AlertDialog.Builder RenameSection;  
	private AlertDialog.Builder DeleteSectionGroup;
	private AlertDialog.Builder DeleteSection;  
	
	private EditText UniName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_section_group);
		setTitle(getIntent().getExtras().getString("name"));
		//-----------------------------------------------------------------------------------
		SectionGroup = (DragListView) findViewById(R.id.SectionGroup);
		SGAdapter = new DragListAdapter(this, simplenote.publics.datas.IndexCache);
		registerForContextMenu(SectionGroup);
		SectionGroup.setAdapter(SGAdapter);
		SectionGroup.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (simplenote.publics.datas.IndexCache.get(arg2).GroupSign == 1) {
					simplenote.filesystem.write.SectionGroup("");
					String name = simplenote.publics.datas.IndexCache.get(arg2).name;
					simplenote.filesystem.maintein.SectionGroup(name);
					simplenote.publics.datas.CurrentPath = simplenote.publics.datas.CurrentPath + name + "/";
					Intent SectionGroupIntent=new Intent();
					SectionGroupIntent.setClass(SectionGroupActivity.this, SectionGroupActivity.class );
					SectionGroupIntent.putExtra("name", name);
					startActivity(SectionGroupIntent);
				} else if (simplenote.publics.datas.IndexCache.get(arg2).GroupSign == 0) {
					simplenote.filesystem.write.SectionGroup("");
					Intent SectionIntent=new Intent();
					SectionIntent.setClass(SectionGroupActivity.this, SectionActivity.class );
					SectionIntent.putExtra("name", simplenote.publics.datas.IndexCache.get(arg2).name);
					startActivity(SectionIntent);
				}
			}
		});

		SectionGroup.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu conMenu, View view , ContextMenuInfo info) {       
				getMenuInflater().inflate(R.menu.section_group_context, conMenu);
		   }    
		});
		//-----------------------------------------------------------------------------------
	}
	@Override
	protected void onPause() {
		super.onPause();
		simplenote.filesystem.write.SectionGroup("");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		simplenote.filesystem.write.SectionGroup("");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		simplenote.filesystem.write.SectionGroup("");
		if (!simplenote.publics.datas.CurrentPath.equals("/")) {
			simplenote.publics.datas.CurrentPath = simplenote.publics.datas.getParentDic(simplenote.publics.datas.CurrentPath);
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		simplenote.filesystem.maintein.SectionGroup("");
		SectionGroup = (DragListView) findViewById(R.id.SectionGroup);
		
		SGAdapter = new DragListAdapter(this, simplenote.publics.datas.IndexCache);
		SectionGroup.setAdapter(SGAdapter);
		}
	
	@Override
	protected void onStart() {
		super.onStart();
		simplenote.filesystem.maintein.SectionGroup("");
		SectionGroup = (DragListView) findViewById(R.id.SectionGroup);
		
		SGAdapter = new DragListAdapter(this, simplenote.publics.datas.IndexCache);
		SectionGroup.setAdapter(SGAdapter);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		simplenote.filesystem.maintein.SectionGroup("");
		SectionGroup = (DragListView) findViewById(R.id.SectionGroup);
		
		SGAdapter = new DragListAdapter(this, simplenote.publics.datas.IndexCache);
		SectionGroup.setAdapter(SGAdapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.section_group, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		//---------------Refresh: maintein itself--------------------
        case R.id.Refresh: {
			simplenote.filesystem.maintein.SectionGroup("");
			SectionGroup = (DragListView) findViewById(R.id.SectionGroup);
			SGAdapter = new DragListAdapter(SectionGroupActivity.this, simplenote.publics.datas.IndexCache);
			SectionGroup.setAdapter(SGAdapter);
			return true;
		}
		//---------------Create Section Group: it's gonna be a fuckin' hard job... ----------------
        case R.id.CreateSectionGroup: {
			CreateSectionGroup = new AlertDialog.Builder(SectionGroupActivity.this);
			UniName = new EditText(this);
			UniName.setSingleLine();
			CreateSectionGroup.setTitle("Create a new Section Group: "); 
			CreateSectionGroup.setView(UniName);
			CreateSectionGroup.setPositiveButton("Accept", new android.content.DialogInterface.OnClickListener()
			{

				public void onClick(DialogInterface CreateSectionGroup, int which) {
					element singleElement = new element();
					singleElement.name = UniName.getText().toString();
					singleElement.GroupSign = 1;
					simplenote.filesystem.operation.Create.SectionGroup(singleElement.name);
					simplenote.publics.datas.IndexCache.add(singleElement);
					SectionGroup = (DragListView) findViewById(R.id.SectionGroup);
					SGAdapter = new DragListAdapter(SectionGroupActivity.this, simplenote.publics.datas.IndexCache);
					SectionGroup.setAdapter(SGAdapter);
					CreateSectionGroup.dismiss();  
					// TODO Auto-generated method stub
				}
			});  
			CreateSectionGroup.setNegativeButton("Decline", new android.content.DialogInterface.OnClickListener()
			{

				public void onClick(DialogInterface CreateSectionGroup, int which) {
					CreateSectionGroup.dismiss();  
					// TODO Auto-generated method stub
				}

			});  
			CreateSectionGroup.show();
			return true;
		}
		//---------------Create Section: Also fuckin' hard... ----------------
        case R.id.CreateSection: {
			CreateSection = new AlertDialog.Builder(SectionGroupActivity.this);
			UniName = new EditText(this);
			UniName.setSingleLine();
			CreateSection.setTitle("Create a new Section: "); 
			CreateSection.setView(UniName);
			CreateSection.setPositiveButton("Accept", new android.content.DialogInterface.OnClickListener()
			{

				public void onClick(DialogInterface CreateSection, int which) {
					element singleElement = new element();
					singleElement.name = UniName.getText().toString();
					singleElement.GroupSign = 0;
					simplenote.filesystem.operation.Create.Section(singleElement.name);
					simplenote.publics.datas.IndexCache.add(singleElement);
					SectionGroup = (DragListView) findViewById(R.id.SectionGroup);
					
					SGAdapter = new DragListAdapter(SectionGroupActivity.this, simplenote.publics.datas.IndexCache);
					SectionGroup.setAdapter(SGAdapter);
					CreateSection.dismiss();  
					// TODO Auto-generated method stub
				}

			});  
			CreateSection.setNegativeButton("Decline", new android.content.DialogInterface.OnClickListener()
			{

				public void onClick(DialogInterface CreateSection, int which) {
					CreateSection.dismiss();  
					// TODO Auto-generated method stub
				}

			});  
			CreateSection.show();
			return true;
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
              case R.id.UniDelete:  {
            	  final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            	  element singleElement = (element) SectionGroup.getAdapter().getItem((int) info.position);
            	  /* Get the selected item out of the Adapter by its position. */    
                  if (singleElement.GroupSign == 1) {
                	  DeleteSectionGroup = new AlertDialog.Builder(SectionGroupActivity.this);
                	  DeleteSectionGroup.setTitle("Delete Section Group");
                	  DeleteSectionGroup.setMessage("Are you sure? ");
                	  DeleteSectionGroup.setPositiveButton("Accept", new android.content.DialogInterface.OnClickListener(){
                		  public void onClick(DialogInterface DeleteSectionGroup, int which) {
                			  element singleElement = (element) SectionGroup.getAdapter().getItem((int) info.position);
                			  simplenote.publics.datas.IndexCache.remove(info.position);
                			  simplenote.filesystem.operation.Delete.SectionGroup(singleElement.name);
                			  SectionGroup = (DragListView) findViewById(R.id.SectionGroup);
                			  SGAdapter = new DragListAdapter(SectionGroupActivity.this, simplenote.publics.datas.IndexCache);
                			  SectionGroup.setAdapter(SGAdapter);
                			  DeleteSectionGroup.dismiss();  
            					// TODO Auto-generated method stub
            				}
                	  });
                	  DeleteSectionGroup.setNegativeButton("Decline", new android.content.DialogInterface.OnClickListener() {
                		  public void onClick(DialogInterface DeleteSectionGroup, int which) {
                			  DeleteSectionGroup.dismiss();  
          						// TODO Auto-generated method stub
          					}
                	  });
                	  DeleteSectionGroup.show();
                  } else {
                	  DeleteSection = new AlertDialog.Builder(SectionGroupActivity.this);
                	  DeleteSection.setTitle("Delete Section");
                	  DeleteSection.setMessage("Are you sure? ");
                	  DeleteSection.setPositiveButton("Accept", new android.content.DialogInterface.OnClickListener(){
                		  public void onClick(DialogInterface DeleteSection, int which) {
                			  element singleElement = (element) SectionGroup.getAdapter().getItem((int) info.position);
                			  simplenote.publics.datas.IndexCache.remove(info.position);
                			  simplenote.filesystem.operation.Delete.Section(singleElement.name);
                        	  SectionGroup = (DragListView) findViewById(R.id.SectionGroup);
                              
                              SGAdapter = new DragListAdapter(SectionGroupActivity.this, simplenote.publics.datas.IndexCache);
                              SectionGroup.setAdapter(SGAdapter);
                              DeleteSection.dismiss();  
            					// TODO Auto-generated method stub
            				}
                	  });
                	  DeleteSection.setNegativeButton("Decline", new android.content.DialogInterface.OnClickListener() {
                		  public void onClick(DialogInterface DeleteSection, int which) {
                			  DeleteSection.dismiss();  
          						// TODO Auto-generated method stub
          					}
                	  });
                	  DeleteSection.show();
                  }
                  return true;
              }     
              //---------------It's a little bit fuckin' complicated... Goddamned dialog involved. --------------------
              case R.id.UniRename: {
            	  final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            	  element singleElement = (element) SectionGroup.getAdapter().getItem((int) info.position);
            	  /* Get the selected item out of the Adapter by its position. */    
                  if (singleElement.GroupSign == 1) {
                	  RenameSectionGroup = new AlertDialog.Builder(SectionGroupActivity.this);
                	  UniName = new EditText(this);
                	  UniName.setSingleLine();
                	  UniName.setText(singleElement.name);
                	  RenameSectionGroup.setTitle("Give a new Section Group name: "); 
                	  RenameSectionGroup.setView(UniName);
                	  RenameSectionGroup.setPositiveButton("Accept", new android.content.DialogInterface.OnClickListener(){
                		  public void onClick(DialogInterface RenameSectionGroup, int which) {
                			  	element singleElement = (element) SectionGroup.getAdapter().getItem((int) info.position);
                			  	element newElement = new element();
                			  	/* Remove it from the list.*/       
                			  	simplenote.publics.datas.IndexCache.remove(info.position);
            					newElement.name = UniName.getText().toString();
            					newElement.GroupSign = 1;
            					simplenote.publics.datas.IndexCache.add(info.position, newElement);
            					simplenote.filesystem.operation.Rename.SectionGroup(singleElement.name, newElement.name);
            					SectionGroup = (DragListView) findViewById(R.id.SectionGroup);
                                
                                SGAdapter = new DragListAdapter(SectionGroupActivity.this, simplenote.publics.datas.IndexCache);
                                SectionGroup.setAdapter(SGAdapter);
            					RenameSectionGroup.dismiss();  
            					// TODO Auto-generated method stub
            				}
                	  });
                	  RenameSectionGroup.setNegativeButton("Decline", new android.content.DialogInterface.OnClickListener() {
                		  public void onClick(DialogInterface RenameSectionGroup, int which) {
                			  RenameSectionGroup.dismiss();  
          						// TODO Auto-generated method stub
          					}
                	  });
                	  RenameSectionGroup.show();
                  } else {
                	  RenameSection = new AlertDialog.Builder(SectionGroupActivity.this);
                	  UniName = new EditText(this);
                	  UniName.setSingleLine();
                	  UniName.setText(singleElement.name);
                	  RenameSection.setTitle("Give a new Section name: "); 
                	  RenameSection.setView(UniName);
                	  RenameSection.setPositiveButton("Accept", new android.content.DialogInterface.OnClickListener(){
                		  public void onClick(DialogInterface RenameSection, int which) {
                			  	element singleElement = (element) SectionGroup.getAdapter().getItem((int) info.position);
                			  	element newElement = new element();
                			  	/* Remove it from the list.*/       
                			  	simplenote.publics.datas.IndexCache.remove(info.position);
            					newElement.name = UniName.getText().toString();
            					newElement.GroupSign = 0;
            					simplenote.publics.datas.IndexCache.add(info.position, newElement);
            					simplenote.filesystem.operation.Rename.Section(singleElement.name, newElement.name);
            					SectionGroup = (DragListView) findViewById(R.id.SectionGroup);
                                
                                SGAdapter = new DragListAdapter(SectionGroupActivity.this, simplenote.publics.datas.IndexCache);
                                SectionGroup.setAdapter(SGAdapter);
            					RenameSection.dismiss();  
            					// TODO Auto-generated method stub
            				}
                	  });
                	  RenameSection.setNegativeButton("Decline", new android.content.DialogInterface.OnClickListener() {
                		  public void onClick(DialogInterface RenameSection, int which) {
                			  RenameSection.dismiss();  
          						// TODO Auto-generated method stub
          					}
                	  });
                	  RenameSection.show();
                  }
            	  return true; 
              }
         }       
         return false;       
    }       
	
	public static class DragListAdapter extends ArrayAdapter<element> {

        public DragListAdapter(Context context, List<element> objects) {
            super(context, 0, objects);
        }
        
        public ArrayList<element> getList(){
            return simplenote.publics.datas.IndexCache;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            
            View view = convertView;
            if(getItem(position).GroupSign == 1){
                //Section Group Layout!!! 
                view = LayoutInflater.from(getContext()).inflate(R.layout.section_group, null);
                TextView textView = (TextView)view.findViewById(R.id.SGTitle);
                TextPaint paint = textView.getPaint();  
                paint.setFakeBoldText(true);  
                textView.setText(getItem(position).name);
            }else{
                //Section Layout. 
                view = LayoutInflater.from(getContext()).inflate(R.layout.section, null);
                TextView textView = (TextView)view.findViewById(R.id.SGTitle);
                textView.setText(getItem(position).name);
            }
            
            
            return view;
        }
    }
}
