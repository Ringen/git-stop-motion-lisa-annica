package se.lisaannica.stopmotion;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

/**
 * Adapter to handle all the images to be shown in the view pager
 * @author Lisa
 *
 */
public class ImagePagerAdapter extends FragmentPagerAdapter {
	private ArrayList<ImageFragment> fragments;
	
	public ImagePagerAdapter(FragmentManager fm) {
		super(fm);
		fragments = new ArrayList<ImageFragment>();
	}
	
	/**
	 * Add an image fragment that should be displayed in the page view
	 */
	public void addImage(Uri imageUri) {
		fragments.add(new ImageFragment(imageUri));
	}

	@Override
	public Fragment getItem(int pos) {
		return fragments.get(pos);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		Log.d("leak", "ImagePagerAdapter, instantiateItem " + position);
		return super.instantiateItem(container, position);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		Log.d("leak", "ImagePagerAdapter, destroyItem " + position);
		super.destroyItem(container, position, object);
		
	}
}
