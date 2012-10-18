package se.lisaannica.stopmotion;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

/**
 * Create a new stop motion movie.
 * @author Lisa Ring and Annica Lindstrom
 *
 */
public class MovieCreator extends FragmentActivity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;	//Code for photo
	private Uri imageFile;												//Image to save the captured image in.
	private File imageStorageDir;										//Direction to where the stored images are.
	
	private ImagePagerAdapter pagerAdapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_creator);
		
		pagerAdapter = new ImagePagerAdapter(getSupportFragmentManager());
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);
		
		imageStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "StopMotionImages");
	}

	/**
	 * Method called by pressing the add-button
	 * @param addButton
	 */
	public void addPic(View addButton) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		//Creates a file where the captured image can be stored and put it as
		//an extra to the camera intent.
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
	 * Create a new fragment to hold a new image
	 */
	private void addFragment(Uri imageUri) {
		pagerAdapter.addImage(imageUri);
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
		if (!imageStorageDir.exists()){
			System.out.println("Directory does not exist.");

			//TODO find out what this does.
			if (!imageStorageDir.mkdirs()){
				System.out.println("Failed to create directory for images.");
			}
		}
		else {
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
		
		//TODO Send list with all captured images to createGif method.
		File gif = createGif();
		
		//TODO Replace this with the second commented part. Should the path be extra or the file or what?
		//Maybe the whole list, and the gif creating takes part in the MovieSettings class instead
		//so that naming it wont be a problem?
		//The next four lines are only for testing.
		/*Intent intent = new Intent(MovieCreator.this, MoviePlayer.class);
		intent.putExtra("gifPath", gif.getPath());
		startActivity(intent);*/
		
		Intent intent = new Intent(MovieCreator.this, MovieSettings.class);
		intent.putExtra("gifPath", gif.getPath());
		this.startActivity(intent);
	}
	
	/**
	 * Creates a gif file.
	 * 
	 * @return gif file
	 */
	public File createGif()
	{ 
		AnimatedGifEncoder encoder = new AnimatedGifEncoder();
		
		//TODO Create bitmaps for all the captured images.
		Bitmap icon = BitmapFactory.decodeResource(this.getResources(), 
                R.drawable.ic_action_search); 
		Bitmap ic = BitmapFactory.decodeResource(this.getResources(), 
                R.drawable.ic_launcher);
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		File gif = new File(imageStorageDir.getPath() + File.separator +
				"stopmotion_"+ timeStamp + ".gif");
		
		OutputStream os;
		try {
			os = new FileOutputStream(gif);
			
			encoder.start(os);
			encoder.setDelay(1000);
			
			//TODO Add all the bitmaps.
			encoder.addFrame(icon);
			encoder.addFrame(ic);
			encoder.finish();
			
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not find the gif file.");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gif;
	}

	/**
	 * Receives the result from the camera.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				
				//Sets the capture image.
				addFragment(imageFile);
			} 
			else if (resultCode == RESULT_CANCELED) {
			} 
			else {
				Toast.makeText(
						this, 
						"Something went wrong with the camera, please try again.",  
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
