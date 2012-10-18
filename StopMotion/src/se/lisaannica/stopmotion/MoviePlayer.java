package se.lisaannica.stopmotion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


/**
 * Class for playing stop motion movies (gif).
 * 
 * @author Annica Lindstrom and Lisa Ring
 *
 */
public class MoviePlayer extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_player);
		
		Intent intent = getIntent();
		String gifPath = intent.getStringExtra("gifPath");
		
		//TODO
		//Draw the gif by extracting bitmap by bitmap.
	}
}
