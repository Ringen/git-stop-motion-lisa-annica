package se.lisaannica.stopmotion;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment to hold an image to be shown on a page in the view pager
 * @author Lisa
 *
 */
public class ImageFragment extends Fragment {
	private View rootView;
	private Uri imageUri;
	private Bitmap image;
	
	public ImageFragment(Uri imageUri) {
		this.imageUri = imageUri;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		rootView = inflater.inflate(
                R.layout.fragment_content, container, false);
//        Bundle args = getArguments();
		
        resizeImage();
        setImage();
		//TODO remove
//		((TextView) rootView.findViewById(R.id.textView_test)).setText("texten ändrad!");
        return rootView;
	}
	
	private void setImage() {
		Log.d("leak", "ImageFragment, setImage, image: " + image);
		Log.d("leak", "ImageFragment, setImage, imageUri: " + imageUri);
		((ImageView) rootView.findViewById(R.id.imageView)).setImageBitmap(image);
	}
	
	public void resizeImage() {
		Log.d("leak", "ImageFragment, resizeImage");
		try {
			Thread tempThread = new Thread(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					URLConnection conn;
					try {
						conn = new URL(imageUri.toString()).openConnection();
						InputStream in = conn.getInputStream();

						Bitmap bitmap = BitmapFactory.decodeStream(in);
						image = Bitmap.createScaledBitmap(bitmap, 96, 96, true);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			
			//TODO create service instead of sleep?
			tempThread.start();
			Thread.sleep(1000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
