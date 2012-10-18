package se.lisaannica.stopmotion;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

/**
 * List of stop motion movies.
 * @author Annica
 *
 */
public class MainActivity extends ListActivity {
	private ArrayList<String> movies = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //TODO remove this temporary stuff
        movies.add("Movie 1");
        movies.add("Movie 2");
        movies.add("Movie 3");
        movies.add("Movie 4");

        //TODO should we create our own adapter?
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, movies);
        setListAdapter(adapter);
        
        registerForContextMenu(getListView());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	
    	if(item.getItemId() == R.id.menu_create_new) {
    		//TODO change back to MovieCreator
    		Intent intent = new Intent(MainActivity.this, MovieCreator.class);
    		this.startActivity(intent);
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
    		ContextMenuInfo menuInfo) {
    	// TODO maybe we should remove the option to remove and edit?
    	super.onCreateContextMenu(menu, view, menuInfo);
    	
    	if (view.getId() == getListView().getId()) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(movies.get(info.position).toString());
			menu.add(0, 0, 0, getResources().getString(R.string.main_play));
//			menu.add(0, 1, 0, getResources().getString(R.string.main_remove));
//			menu.add(0, 2, 0, getResources().getString(R.string.main_edit));
		}
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	if(item.getItemId() == 0) {
    		//TODO play the movie
    		Log.d("main", "MainActivity, onContextItemSelected, item: " + item.getItemId());
    	}
    	
    	return super.onContextItemSelected(item);
    }
}
