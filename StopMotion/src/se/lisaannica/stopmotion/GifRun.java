// Created by JeffMeJones@gmail.com
package se.lisaannica.stopmotion;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;


public class GifRun implements Runnable, Callback {


	public Bitmap bmb;
	public GIFDecode decode;
	public int ind;
	public int gifCount;
	public SurfaceHolder mSurfaceHolder ;
	boolean surfaceExists;

	public void LoadGiff(SurfaceView v, android.content.Context theTHIS, String path)
	{		
		mSurfaceHolder = v.getHolder();
		mSurfaceHolder.addCallback(this);
		decode = new GIFDecode();
		try {
			decode.read(new BufferedInputStream(new FileInputStream(path)));
		} catch (FileNotFoundException e) {
			System.out.println("GifRun could not create inputstream from gif path: ");
			e.printStackTrace();
		}
		ind = 0;
		// decode.
		gifCount = decode.getFrameCount();
		bmb = decode.getFrame(0);
		surfaceExists=true;
		Thread t = new Thread(this);
		t.start();
	}

	public void run() 
	{
		while (surfaceExists) {
			try {

				Canvas rCanvas = mSurfaceHolder.lockCanvas();
				rCanvas.drawBitmap(bmb, 0, 0, new Paint());
				//ImageView im = (ImageView) findViewById(R.id.imageView1);
				//im.setImageBitmap(bmb);

				mSurfaceHolder.unlockCanvasAndPost(rCanvas);
				bmb = decode.next();

				Thread.sleep(100);
			} catch (Exception ex) {

			}
		}

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) 
	{



	}

	public void surfaceCreated(SurfaceHolder holder) 
	{

	}

	public void surfaceDestroyed(SurfaceHolder holder) {

		surfaceExists=false;
	}

}
