package se.lisaannica.stopmotion;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Create a new stop motion movie.
 * @author Lisa Ring and Annica Lindstrom
 *
 */
public class MovieCreator extends Activity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;	//Code for photo
	private Uri imageFile;												//Image to save the captured image in.
	private File imageStorageDir;										//Direction to where the stored images are.
	
	private TextView instruction;
	private ImagePagerAdapter pagerAdapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_creator);
		
		//instructions to be visible when no images added
		instruction = (TextView) findViewById(R.id.textView_create_instruction);
		
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		pagerAdapter = new ImagePagerAdapter();
		viewPager.setAdapter(pagerAdapter);
		
		imageStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "StopMotionImages");
	}

	/**
	 * Method called by pressing the add-button
	 * @param addButton
	 */
	public void addPic(View addButton) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		/*Creates a file where the captured image can be stored and put it 
		  as an extra to the camera intent.*/
		imageFile = createFileForStorage();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFile);

		if (imageFile != null)
			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE); 
		else
			Toast.makeText(
					this, 
					"The SD-card is not correctly mounted, please fix this before taking pictures ",  
					Toast.LENGTH_LONG).show();
	}
	
	/**
	 * Creates a file where the captured image can be stored.
	 * @return file for storage or null if the SD-card is not correctly mounted.
	 */
	private Uri createFileForStorage()
	{
		//Return null if the SD-card is not correctly mounted.
		if(!Environment.getExternalStorageState().equals("mounted"))
			return null;

		//Creates the directory if it does not exists.
		if (!imageStorageDir.exists()) {
			System.out.println("Directory does not exist.");

			//TODO find out what this does.
			if (!imageStorageDir.mkdirs()) {
				System.out.println("Failed to create directory for images.");
			}
		} else {
			System.out.println("Directory exists.");
		}

		//Adds time stamp for unique file names.
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		File file = new File(imageStorageDir.getPath() + File.separator +
				"IMG_"+ timeStamp + ".jpg");

		return Uri.fromFile(file);
	}

	/**
	 * Method called by pressing the finish-button.
	 * @param finishButton
	 */
	public void finishMovie(View finishButton) {		
		Intent intent = new Intent(MovieCreator.this, MovieSettings.class);
		
		if (pagerAdapter.getImages().size() > 0) {
			intent.putExtra("images", pagerAdapter.getImages().toArray());
			this.startActivity(intent);
		} else {
			Toast.makeText(
					this, 
					"You have to take pictures before you can create a stop motion movie.", 
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Receives the result from the camera.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("show", "MovieCreator, onActivityResult RESULT_OK: " 
				+ RESULT_OK + ", resultCode: " + resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				
				//get the image from the camera
				Bitmap photo = null;
				if(data != null) {
					photo = (Bitmap) data.getExtras().get("data");
				} else {
					try {
						photo = BitmapFactory.decodeStream(getContentResolver()
								.openInputStream(imageFile));
					} catch (FileNotFoundException e) {
						Log.d("show", "MovieCreator, onActivityResult, FileNotFoundException");
						e.printStackTrace();
					}
				}
				
				//remove the instruction
				instruction.setText("");
				
				//set the image on the screen
	            pagerAdapter.addImage(resizeImage(photo));
	            pagerAdapter.notifyDataSetChanged();
			} else if (resultCode == RESULT_CANCELED) {
			} else {
				Toast.makeText(
						this, 
						"Something went wrong with the camera, please try again.",  
						Toast.LENGTH_LONG).show();
			}
		}
	}
	
	/**
	 * Resizes a bitmap
	 * @param original bitmap
	 * @return resized bitmap
	 */
	private Bitmap resizeImage(Bitmap original) {
		int width = original.getWidth()/4;
		int height = original.getHeight()/4;
		
		return Bitmap.createScaledBitmap(original, width, height, true);
	}
}
