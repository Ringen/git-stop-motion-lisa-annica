package se.lisaannica.stopmotion;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceView;
import android.view.View;


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
		
		//String gifName = intent.getStringExtra("gifName");
		
		File imageStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), 
										"StopMotionImages");
		
		SurfaceView sv = (SurfaceView)findViewById(R.id.gif_view);
		
		GifRun gr = new GifRun();
		gr.LoadGiff(sv, this, gifPath);
		//gr.LoadGiff(sv, this, imageStorageDir.getPath() + File.separator + gifName);
	}
}
