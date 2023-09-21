package simplenote.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	private final int CreateRequest = 1;
	private final int OpenRequest = 2;
	private final int maxHRNumber = 10;
	private final String HRPath = "HistoryRecord";
	
	private Button CreateNotebook;
	private Button OpenNotebook;
	private Button CreateDeterminePath;
	private Button OpenDeterminePath;
	private EditText NotebookParentPath;
	private EditText NotebookPath;
	private EditText NotebookName;

	private LayoutInflater CreateNotebookDialogLayout;
	private LayoutInflater OpenNotebookDialogLayout;  
	private View CreateNotebookDialogView;
	private View OpenNotebookDialogView; 
	private AlertDialog.Builder CreateNotebookDialog;
	private AlertDialog.Builder OpenNotebookDialog;  
	
	private ListView HistoryRecord;
	private SimpleAdapter HRAdapter;
	private File HRFile;
	private FileReader HRReader;
	private BufferedReader HRBR;
	private FileWriter HRWriter;
	private HashMap<String, Object> map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_initial);
		//-----------------------------------------------------------------------------------
		HistoryRecord=(ListView)findViewById(R.id.HistoryRecord);
		simplenote.publics.datas.HRList = new ArrayList<HashMap<String, Object>>();  
		ReadHR();
		HRAdapter = new SimpleAdapter(this,simplenote.publics.datas.HRList, R.layout.history_record,new String[] {"HRNotebookName", "HRNotebookPath"}, new int[] {R.id.HRNotebookName,R.id.HRNotebookPath}); 
		HistoryRecord.setAdapter(HRAdapter);
		HistoryRecord.setOnItemClickListener(new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						simplenote.publics.datas.CurrentPath=(String)simplenote.publics.datas.HRList.get(arg2).get("HRNotebookName");
						simplenote.publics.datas.AbsolutePath=(String)simplenote.publics.datas.HRList.get(arg2).get("HRNotebookPath");
						simplenote.publics.datas.HRList.remove(arg2);
						map = new HashMap<String, Object>();  
				        map.put("HRNotebookName", simplenote.publics.datas.CurrentPath);  
				        map.put("HRNotebookPath", simplenote.publics.datas.AbsolutePath);  
				        simplenote.publics.datas.HRList.add(0,map);
				        HRAdapter.notifyDataSetChanged();
				        WriteHR();
				        OpenNotebook();
					}
		});
		//-----------------------------------------------------------------------------------
		CreateNotebook=(Button)findViewById(R.id.CreateNotebook);
		CreateNotebook.setOnClickListener(new OnClickListener()		 
			{
				@Override
				public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//--------------------------------------------CreateNotebookDialog----------------------------------------
				CreateNotebookDialogLayout = LayoutInflater.from(MainActivity.this);  
				CreateNotebookDialogView = CreateNotebookDialogLayout.inflate(R.layout.create_notebook, null); 
				CreateNotebookDialog = new AlertDialog.Builder(MainActivity.this);  
				CreateNotebookDialog.setTitle("Create a new Notebook: ");  
				CreateNotebookDialog.setView(CreateNotebookDialogView);  	
				CreateNotebookDialog.setPositiveButton("Accept", new android.content.DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface CreateNotebookDialog, int which) {
						simplenote.publics.datas.AbsolutePath=NotebookParentPath.getText().toString();
						simplenote.publics.datas.CurrentPath=NotebookName.getText().toString();
						if (simplenote.publics.datas.HRList.size()>=maxHRNumber){
							simplenote.publics.datas.HRList.remove(maxHRNumber-1);
						}
						map = new HashMap<String, Object>();  
				        map.put("HRNotebookName", simplenote.publics.datas.CurrentPath);  
				        map.put("HRNotebookPath", simplenote.publics.datas.AbsolutePath);  
				        simplenote.publics.datas.HRList.add(0,map);
				        HRAdapter.notifyDataSetChanged();
				        WriteHR();
				        simplenote.filesystem.operation.Create.Notebook();
				        OpenNotebook();
						CreateNotebookDialog.dismiss();
						// TODO Auto-generated method stub
					}

				});  
				CreateNotebookDialog.setNegativeButton("Decline", new android.content.DialogInterface.OnClickListener()
				{

					public void onClick(DialogInterface CreateNotebookDialog, int which) {
						CreateNotebookDialog.dismiss();  
						// TODO Auto-generated method stub
					}

				});  
				CreateNotebookDialog.show(); 
				//--------------------------------------------Widgets in CreateNotebookDialog----------------------------------------
				NotebookParentPath = (EditText)CreateNotebookDialogView.findViewById(R.id.NotebookParentPath);
				NotebookName = (EditText)CreateNotebookDialogView.findViewById(R.id.NotebookName);
				CreateDeterminePath=(Button)CreateNotebookDialogView.findViewById(R.id.CreateDeterminePath);
				CreateDeterminePath.setOnClickListener(new OnClickListener()		 
					{
						@Override
						public void onClick(View arg0) {
						// TODO Auto-generated method stub
							Intent Create = new Intent(Intent.ACTION_GET_CONTENT);  
							Create.setType("*/*");  
							Create.addCategory(Intent.CATEGORY_OPENABLE);  
							try {  
								startActivityForResult(Intent.createChooser(Create, "Create Notebook"), CreateRequest);  
							    } catch (android.content.ActivityNotFoundException ex) {  
							        Toast.makeText(getApplicationContext(), "Install a file manager. ", Toast.LENGTH_SHORT).show();  
							    }
						}
					});
				
			}
				
			});
		//-----------------------------------------------------------------------------------
		OpenNotebook=(Button)findViewById(R.id.OpenNotebook);
		OpenNotebook.setOnClickListener(new OnClickListener()		 
			{
				@Override
				public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//---------------------------------------------OpenNotebookDialog---------------------------------------
				OpenNotebookDialogLayout = LayoutInflater.from(MainActivity.this);  
				OpenNotebookDialogView = OpenNotebookDialogLayout.inflate(R.layout.open_notebook, null); 
				OpenNotebookDialog = new AlertDialog.Builder(MainActivity.this);  
				OpenNotebookDialog.setTitle("Open Existed Notebook: ");  
				OpenNotebookDialog.setView(OpenNotebookDialogView);  
				OpenNotebookDialog.setPositiveButton("Accept", new android.content.DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface OpenNotebookDialog, int which) {
						simplenote.publics.datas.AbsolutePath=NotebookPath.getText().toString();
						simplenote.publics.datas.CurrentPath=simplenote.publics.datas.AbsolutePath.substring(simplenote.publics.datas.Find(simplenote.publics.datas.AbsolutePath), simplenote.publics.datas.AbsolutePath.length());
						simplenote.publics.datas.AbsolutePath=simplenote.publics.datas.AbsolutePath.substring(0, simplenote.publics.datas.Find(simplenote.publics.datas.AbsolutePath));
						if (simplenote.publics.datas.HRList.size()>=maxHRNumber){
							simplenote.publics.datas.HRList.remove(maxHRNumber-1);
						}
						map = new HashMap<String, Object>();  
				        map.put("HRNotebookName", simplenote.publics.datas.CurrentPath);  
				        map.put("HRNotebookPath", simplenote.publics.datas.AbsolutePath);  
				        simplenote.publics.datas.HRList.add(0,map);
				        HRAdapter.notifyDataSetChanged();
				        WriteHR();
				        OpenNotebook();
						OpenNotebookDialog.dismiss();  
						// TODO Auto-generated method stub
					}
				});  
				OpenNotebookDialog.setNegativeButton("Decline", new android.content.DialogInterface.OnClickListener()
				{

					public void onClick(DialogInterface OpenNotebookDialog, int which) {
						OpenNotebookDialog.dismiss();  
						// TODO Auto-generated method stub
					}

				});
				OpenNotebookDialog.show();  
				//--------------------------------------------Widgets in OpenNotebookDialog----------------------------------------
				NotebookPath = (EditText)OpenNotebookDialogView.findViewById(R.id.NotebookPath);
				OpenDeterminePath=(Button)OpenNotebookDialogView.findViewById(R.id.OpenDeterminePath);
				OpenDeterminePath.setOnClickListener(new OnClickListener()		 
				{
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
							Intent Open = new Intent(Intent.ACTION_GET_CONTENT);  
							Open.setType("*/*");  
							Open.addCategory(Intent.CATEGORY_OPENABLE);  
							try {  
								startActivityForResult(Intent.createChooser(Open, "Open Notebook"), OpenRequest);  
							    } catch (android.content.ActivityNotFoundException ex) {  
							        Toast.makeText(getApplicationContext(), "Install a file manager. ", Toast.LENGTH_SHORT).show();  
							    }
						}
				});
			}
			});
		//-----------------------------------------------------------------------------------
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        // TODO Auto-generated method stub  
			super.onActivityResult(requestCode, resultCode, data);  
	        if (resultCode == RESULT_OK & data != null) 
	        {  
	            // Get the Url of the selected file  
	        	Uri uri = data.getData();
	        	switch (requestCode)  
	            {  
	            case CreateRequest:  
	            	simplenote.publics.datas.AbsolutePath=uri.toString().substring(7)+"/";
		        	NotebookParentPath.setText(simplenote.publics.datas.AbsolutePath);
	                break;  
	            case OpenRequest:  
	            	simplenote.publics.datas.AbsolutePath=uri.toString().substring(7);
		        	NotebookPath.setText(simplenote.publics.datas.AbsolutePath);
	                break;  
	            }
	        }
	        
	    }  
	
	protected void onPause(){
		super.onPause();
		WriteHR();
	}
	
	//--------------file HR to simplenote.publics.datas.HRList---------------------------------------------------------------------
	private void ReadHR(){
		HRFile=new File(getFilesDir(), HRPath);
		if (!HRFile.isFile()) {
			return;
		}
		try {
			HRReader = new FileReader(HRFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HRBR = new BufferedReader(HRReader);
		try{
			String temp = HRBR.readLine();
			String temp2 = HRBR.readLine();
			while (temp!=null){
				map = new HashMap<String, Object>();  
				map.put("HRNotebookName", temp);  
		        map.put("HRNotebookPath", temp2);  
		        simplenote.publics.datas.HRList.add(map);  
		        temp = HRBR.readLine();
		        temp2 = HRBR.readLine();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	//--------------simplenote.publics.datas.HRList to file HR---------------------------------------------------------------------
	private void WriteHR(){
		HRFile=new File(getFilesDir(), HRPath);
		if (!HRFile.isFile()) {
			try{
				HRFile.createNewFile();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			HRWriter = new FileWriter(HRFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			String temp;
			for(int i=0;i<simplenote.publics.datas.HRList.size();i++){
				temp=(String)simplenote.publics.datas.HRList.get(i).get("HRNotebookName")+"\n";
				HRWriter.write(temp);
				temp=(String)simplenote.publics.datas.HRList.get(i).get("HRNotebookPath")+"\n";
				HRWriter.write(temp);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		try {
			HRWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//--------------All the fuckin' open notebook operation codes are here---------------------------------------------------------------------
	private void OpenNotebook(){
		Intent SectionGroupIntent=new Intent();
		SectionGroupIntent.putExtra("name", simplenote.publics.datas.CurrentPath);
		simplenote.publics.datas.AbsolutePath = simplenote.publics.datas.AbsolutePath+simplenote.publics.datas.CurrentPath;
		simplenote.publics.datas.CurrentPath = "/";
		simplenote.filesystem.maintein.Notebook();
		SectionGroupIntent.setClass(MainActivity.this, SectionGroupActivity.class );
		startActivity(SectionGroupIntent);
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.initial, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.ClearHistoryRecord){
			simplenote.publics.datas.HRList.clear();
			HRAdapter.notifyDataSetChanged();
			HRFile=new File(getFilesDir(), HRPath);
			HRFile.delete();
		}
		return true;
	}
}