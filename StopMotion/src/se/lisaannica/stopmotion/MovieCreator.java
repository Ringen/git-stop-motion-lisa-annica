package se.lisaannica.stopmotion;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

/**
 * Create a new stop motion movie.
 * @author Lisa Ring and Annica Lindstrom
 *
 */
public class MovieCreator extends Activity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri imageFile;

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
		/*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		//Creates file for storing captured image.
		File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "StopMotionImages");
		
		//Creates the directory if it does not exists.
		if (!imageStorageDir.exists()){
			System.out.println("Directory does not exist.");
	        if (!imageStorageDir.mkdirs()){
	            System.out.println("Failed to create directory for images.");
	        }
	    }
		else {
			System.out.println("Directory exists.");
		}
		
		//TODO attach filename to imageStorageDir.
		imageFile = Uri.fromFile(some file with a name);
		File mediaFile = new File(imageStorageDir.getPath() + File.separator +
        "IMG_"+ timeStamp + ".jpg");

		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);*/ 
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	        	System.out.println("Image captured");
	        	System.out.println("Data: " + data.getData());
	        } 
	        else if (resultCode == RESULT_CANCELED) {
	            System.out.println("User canceled");
	        } 
	        else {
	            System.out.println("Image capture failed");
	        }
        }
	}
}
