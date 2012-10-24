package se.lisaannica.stopmotion;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Fragment to hold an image to be shown on a page in the view pager
 * @author Lisa
 *
 */
public class ImageFragment extends Fragment {
	private ImageView imageView;

	/**
	 * create a new instance of this imageFragment
	 * @param imageUri
	 * @return
	 */
	public static ImageFragment newInstance(String imageUri) {
		ImageFragment fragment = new ImageFragment();
		Bundle bundle = new Bundle();
		
		//set the image-uri as extra
		bundle.putString("imageUri", imageUri);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("show", "ImageFragment, onCreateView");
		super.onCreateView(inflater, container, savedInstanceState);
		
		//find the imageView to hold the image
		View rootView = inflater.inflate(R.layout.fragment_content, container, false);
		imageView = (ImageView) rootView.findViewById(R.id.imageView);

		//Resize the image and set it to the imageView
		ImageResizeTask task = new ImageResizeTask();
		task.execute();
		
		return imageView;
	}

	/**
	 * Set the content of the imageView
	 * @param bm
	 */
	private void setImage(Bitmap bm) {
		imageView.setImageBitmap(bm);
	}


	/**
	 * Handles resizing of image
	 * @author Lisa Ring
	 *
	 */
	private class ImageResizeTask extends AsyncTask<Void, Void, Bitmap> {
		private Bitmap bm;

		@Override
		protected Bitmap doInBackground(Void... params) {
			Log.d("show", "doinBackground");
			URLConnection conn;
			try {
				//create a stream to the original image
				conn = new URL(getArguments().getString("imageUri")).openConnection();
				InputStream in = conn.getInputStream();

				//create a resized bitmap
				Bitmap bitmap = BitmapFactory.decodeStream(in);
				int width = bitmap.getWidth()/4;
				int height = bitmap.getHeight()/4;
				
				
				bm = Bitmap.createScaledBitmap(bitmap, width, height, true);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return bm;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			Log.d("show", "onPostExecute");
			
			super.onPostExecute(result);
			/*Automatically set the resized image as the content of the 
			 * imageView when doInBackground if finished*/
			setImage(result);
		}

	}
}
