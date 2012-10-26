package se.lisaannica.stopmotion;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceView;
import android.widget.Toast;


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
		String gifName = intent.getStringExtra("gifName");
		
		File imageStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), 
										"StopMotionMovies");
		
		File gifFile = new File(imageStorageDir.getPath() + File.separator + gifName + ".gif");
		
		if (gifFile.exists())
		{
			SurfaceView sv = (SurfaceView)findViewById(R.id.gif_view);
			
			GifRun gr = new GifRun();
			gr.LoadGiff(sv, this, gifFile.getPath());
		} else {
			Toast.makeText(this, "The gif you chose does not exist.", Toast.LENGTH_LONG).show();
		}		
	}
}
