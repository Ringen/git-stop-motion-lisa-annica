package se.lisaannica.stopmotion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Create a new stop motion movie.
 * @author Lisa
 *
 */
public class MovieCreator extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_creator);
	}
	
	/**
	 * Method called by pressing the add-button
	 * @param addButton
	 */
	public void addPic(View addButton) {
		//TODO
	}
	
	/**
	 * Method called by pressing the finish-button.
	 * @param finishButton
	 */
	public void finishMovie(View finishButton) {
		//TODO put an extra or something?
		Intent intent = new Intent(MovieCreator.this, MovieSettings.class);
		this.startActivity(intent);
	}
}
