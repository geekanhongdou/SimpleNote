package simplenote.ui;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import simplenote.publics.*;
import android.widget.*;

public class EditorActivity extends Activity {
	private EditText Title;
	private EditText Content;
	private int id;
	private String name;
	private record Record;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		id = getIntent().getExtras().getInt("Record id"); //acquire record id
		name = getIntent().getExtras().getString("name"); //acquire SECTION file name
		Record = simplenote.publics.datas.SectionCache.get(id);
		Title = (EditText)findViewById(R.id.Title);
		Content = (EditText)findViewById(R.id.Content);
	}
	
	
	protected  void onStart()
	{
		super.onStart();
		Title.setText(Record.title);
		Content.setText(Record.content);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		Record.title=Title.getText().toString();
		Record.content=Content.getText().toString();
	}
	
	protected void onStop()
	{
		super.onStop();
		Record.title=Title.getText().toString();
		Record.content=Content.getText().toString();
	}
	
	protected void onDestroy()
	{
		super.onDestroy();
		Record.title=Title.getText().toString();
		Record.content=Content.getText().toString();
		simplenote.filesystem.write.Section(name);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.editor, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		//---------------Create Section Group: it's gonna be a fuckin' hard job... ----------------
        case R.id.Large: {
        	Content.setTextSize(25);
			return true;
        }
        case R.id.Medium: {
        	Content.setTextSize(18);
			return true;
        }
        case R.id.Small: {
        	Content.setTextSize(12);
			return true;
        }
		//---------------Save Manually: the ultimate failsafe. --------------------
        case R.id.EditorSave: {
        	simplenote.filesystem.write.Section(name);
        }
        default:
        	break;
        }
		return false;
	}
}