package se.lisaannica.stopmotion;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * List of stop motion movies.
 * @author Annica
 *
 */
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("main", "this is new");
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
    		Intent intent = new Intent(MainActivity.this, MovieCreator.class);
    		this.startActivity(intent);
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
}
