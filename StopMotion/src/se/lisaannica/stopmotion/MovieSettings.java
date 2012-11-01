package se.lisaannica.stopmotion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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

	private List<String> imageList;
	private String gifName;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_settings);

		Intent intent = getIntent();
		imageList = intent.getStringArrayListExtra("imageList");
	}

	/**
	 * Method called by pressing the save-button.
	 * 
	 * @param saveButton
	 */
	public void savePic(View saveButton)
	{
		EditText et = (EditText) findViewById(R.id.editText_title);
		gifName = et.getText().toString();

		if (onlyLetters(gifName)) {
			new Loader().execute(gifName);
		} else {
			Toast.makeText(this, getResources().getString(R.string.settings_file_name_notification), Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Method to control the name of the movie.
	 * @param name
	 * @return true if it only contain letters a-z
	 */
	private boolean onlyLetters(String name)
	{
		for(int i = 0; i < name.length(); i++) {
			if (!Character.isLetter(name.charAt(i))) { 
				return false;
			}
		}
		return true;
	}

	/**
	 * Go back to the main activity without saving the movie
	 * @param button
	 */
	public void discardPic(View button) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MovieSettings.this);

		//strings for the dialog
		String dialogText = getResources().getString(R.string.settings_discard_msg);
		String positiveText = getResources().getString(R.string.settings_yes);
		String negativeText = getResources().getString(R.string.settings_no);

		//set strings for message and buttons in dialog
		builder.setMessage(dialogText);
		builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				//remove images from folder
				cleanImagesFolder();

				//go back to main activity without possibilities to go back
				Intent intent = Intent.makeRestartActivityTask(
						new ComponentName("se.lisaannica.stopmotion","se.lisaannica.stopmotion.MainActivity"));
				startActivity(intent);
			}
		});

		//do nothing but close the dialog
		builder.setNegativeButton(negativeText, null);

		//show the dialog
		builder.create().show();
	}
	
	/**
	 * Deletes all the captured photos. This for saving memory.
	 */
	private void cleanImagesFolder()
	{
		File files = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), 
				getResources().getString(R.string.image_storage_file));
		if (files.exists()) {
			File[] fileList = files.listFiles();
			for (File file: fileList) {
				file.delete();
			}
		}
	}

	private class Loader extends AsyncTask<String, Integer, Boolean> {
		protected Boolean doInBackground(String...strings) {
			createGif(gifName);
			cleanImagesFolder();
			return true;
		}
		
		@Override
		protected void onPreExecute() {
			//Shows the dialog
			pd = ProgressDialog.show(MovieSettings.this, "", getResources().getString(R.string.settings_saving));
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			//Closes the dialog
			pd.dismiss();
			
			Toast.makeText(MovieSettings.this, R.string.setting_save_verification, Toast.LENGTH_LONG).show();

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
			File movieStorageDir = new File(MovieSettings.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), 
					getResources().getString(R.string.movie_storage_file));

			if (!movieStorageDir.exists()) {
				if (!movieStorageDir.mkdirs()) {
				}
			}

			//File where the gif will be saved.
			File gif = new File(movieStorageDir.getPath() + File.separator + gifName + ".gif");

			OutputStream os;
			try {
				os = new FileOutputStream(gif);
				encoder.start(os);

				Bitmap photo;
				//Adds all the bitmaps.
				for (String str: imageList) {
					photo = createBitmap(str);

					encoder.addFrame(resizeImage(photo));
					encoder.setDelay(500);
				}

				encoder.finish();

				os.flush();
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Creates a bitmap from a jpg in the stored file path.
		 * @param filePath
		 * @return
		 */
		private Bitmap createBitmap(String filePath)
		{
			File imageFile = new File(filePath);
			Bitmap bm = null;
			try {
				bm = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(Uri.fromFile(imageFile)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return bm;
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
}
