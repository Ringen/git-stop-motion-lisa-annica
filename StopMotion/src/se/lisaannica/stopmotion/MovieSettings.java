package se.lisaannica.stopmotion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Set a name of the movie, creates a gif and saves it to a location 
 * connected to the application.
 * 
 * @author Lisa Ring and Annica Lindstrom
 *
 */
public class MovieSettings extends Activity {

	private List<Bitmap> imageList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_settings);

		Intent intent = getIntent();
		Serializable images = intent.getSerializableExtra("images");

		imageList = setImageList(images);
	}
	
	/**
	 * Sets the list with all the photos captured.
	 * 
	 * @param images
	 * @return
	 */
	private List<Bitmap> setImageList(Serializable images)
	{
		List<Bitmap> list = new ArrayList<Bitmap>();
		Object[] o = (Object[]) images;
		if (o != null) {
			for (int i = 0; i < o.length; i++) {
				if (o[i] instanceof Bitmap) {
					System.out.println("Is bitmap");
					list.add((Bitmap) o[i]);
				}
			}
		}
		return list;
	}

	/**
	 * Method called by pressing the save-button.
	 * 
	 * @param saveButton
	 */
	public void savePic(View saveButton)
	{
		EditText et = (EditText) findViewById(R.id.editText_title);
		String gifName = et.getText().toString();

		createGif(gifName);
		cleanImagesFolder();

		Toast.makeText(this, "Your stop motion movie " + gifName + " has now been saved.", Toast.LENGTH_LONG).show();

		Intent intent = Intent.makeRestartActivityTask(
				new ComponentName("se.lisaannica.stopmotion","se.lisaannica.stopmotion.MainActivity"));
		startActivity(intent); 
	}

	/**
	 * Creates a gif file.
	 * 
	 * @return gif file
	 */
	public void createGif(String gifName)
	{ 
		AnimatedGifEncoder encoder = new AnimatedGifEncoder();

		//Directory to where the file will be saved.
		File movieStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "StopMotionMovies");

		if (!movieStorageDir.exists()) {
			System.out.println("Directory does not exist.");

			//TODO find out what this does.
			if (!movieStorageDir.mkdirs()) {
				System.out.println("Failed to create directory for images.");
			}
		}
		
		//File where the gif will be saved.
		File gif = new File(movieStorageDir.getPath() + File.separator +
				gifName + ".gif");

		OutputStream os;
		try {
			os = new FileOutputStream(gif);

			encoder.start(os);

			//Adds all the bitmaps.
			for (Bitmap bm: imageList) {
				encoder.addFrame(bm);
			}

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
	}
	
	/**
	 * Deletes all the captured photos. This for saving memory.
	 */
	private void cleanImagesFolder()
	{
		File files = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "StopMotionImages");

		if (files.exists()) {
			File[] fileList = files.listFiles();

			for (File file: fileList) {
				file.delete();
			}
		}
	}
}
