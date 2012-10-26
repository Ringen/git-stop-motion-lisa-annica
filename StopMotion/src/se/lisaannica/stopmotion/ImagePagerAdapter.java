package se.lisaannica.stopmotion;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Adapter to handle all the images to be shown in the view pager
 * @author Lisa Ring
 *
 */
public class ImagePagerAdapter extends PagerAdapter {
	private List<Bitmap> images;
	private LayoutInflater inflater;

	public ImagePagerAdapter() {
		super();
		images = new ArrayList<Bitmap>();
	}
	
	/**
	 * Add a bitmap to the list of images
	 * @param image
	 */
	public void addImage(Bitmap image) {
		Log.d("show", "ImagePagerAdapter, addImage " + image);
		Log.d("show", "ImagePagerAdapter, addImage, height: " + image.getHeight() + ", width: " + image.getWidth());
		images.add(image);
	}
	
	/**
	 * Returns the array with images.
	 * @return
	 */
	public List<Bitmap> getImages()
	{
		return images;
	}

	@Override
	public int getCount() {
		return images.size();
	}
	
	@Override
	public Object instantiateItem(ViewGroup viewPager, int position) {
		/*called on an item in the viewpager that becomes the neighbor to
		 * the current page (when swiping)*/
		Log.d("show", "ImagePagerAdapter, instantiateItem " + position);
		
		if(inflater == null){
			inflater = (LayoutInflater) viewPager.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		View root = inflater.inflate(R.layout.fragment_content, null, false);
		ImageView imageView = (ImageView) root.findViewById(R.id.imageView);
		imageView.setImageBitmap(images.get(position));
		
		viewPager.addView(imageView);
		
		return imageView;
	}
	
	@Override
	public void destroyItem(ViewGroup viewPager, int position, Object object) {
		Log.d("show", "ImagePagerAdapter, destroyItem " + position);
		/*called on an item in the viewpager that no longer is a neighbor 
		 * to the current page (when swiping)*/
		viewPager.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == ((ImageView) obj);
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		CharSequence title = "Image " + (position+1) + "/" + images.size();
		Log.d("show", "ImagePagerADapter, getPageTitle: " + title);
		return title;
	}
}
