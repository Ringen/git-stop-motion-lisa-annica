package se.lisaannica.stopmotion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;

/**
 * List of stop motion movies. Main activity for the application.
 * @author Annica Lindstr�m and Lisa Ring
 *
 */
public class MainActivity extends ListActivity {
	private List<String> movies;
	private File movieStorageDir;
	private ArrayAdapter<String> adapter;
	private TwitterConnection twitter;
	private String movieName;
	private SharedPreferences prefs;
	private String token;
	private String tokenSecret;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//where to store movies
		movieStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "StopMotionMovies");
		movies = setMovieList(movieStorageDir);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		//set adapter to use for handle viewpager content
		adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, movies);
		setListAdapter(adapter);

		//make it possible to long click on list items (movies)
		registerForContextMenu(getListView());
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		token = prefs.getString("token", null);
		tokenSecret = prefs.getString("tokenSecret", null);
	}

	/**
	 * Sets the list with all the movie names.
	 * @param files
	 * @return
	 */
	private List<String> setMovieList(File files)
	{
		List<String> movieList = new ArrayList<String>();
		if (files.exists()) {
			File[] fileList = files.listFiles();
			for (File file: fileList) {
				if ((file.getName()).endsWith(".gif")) {
					movieList.add(file.getName().replaceAll(".gif", ""));
				}
			}
		}
		return movieList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.menu_create_new) {
			Intent intent = new Intent(MainActivity.this, MovieCreator.class);
			this.startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		//create the content of the context menu
		super.onCreateContextMenu(menu, view, menuInfo);

		if (view.getId() == getListView().getId()) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(movies.get(info.position).toString());
			menu.add(0, 0, 0, getResources().getString(R.string.main_play));
			menu.add(0, 1, 0, getResources().getString(R.string.main_share));
			menu.add(0, 2, 0, getResources().getString(R.string.main_remove));
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		movieName = movies.get((info.position));

		if(item.getItemId() == 0) { //Play movie
			Intent intent = new Intent(MainActivity.this, MoviePlayer.class);
			intent.putExtra("gifName", movieName);
			this.startActivity(intent);
		} else if (item.getItemId() == 1) { 
			//Share on Twitter
			new TwitterSetup().execute();
		} else if (item.getItemId() == 2) { 
			//Remove movie
			deleteMovie(movieName);
		} 
		return super.onContextItemSelected(item);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			if(requestCode == 0) {
				new TwitterAuthentication().execute(data);
			}
		}
	}

	/**
	 * Deletes the selected movie.
	 * @param movieName
	 */
	private void deleteMovie(String name)
	{
		File gif = new File(movieStorageDir.getPath() + File.separator +
				name + ".gif");
		gif.delete();

		for(int i = 0; i < movies.size(); i++) {
			if(name.equals(movies.get(i))) {
				movies.remove(i);
			}	
		}
		adapter.remove(name);
	}
	
	/**
	 * Send a tweet on twitter
	 */
	private void sendTweet() {
		Intent sendTweetIntent = new Intent(MainActivity.this, SendTweetActivity.class);
		sendTweetIntent.putExtra("token", token);
		sendTweetIntent.putExtra("tokenSecret", tokenSecret);
		sendTweetIntent.putExtra("gifPath", movieStorageDir.getPath() + File.separator +
				movieName + ".gif");
		startActivity(sendTweetIntent);
	}
	
	/**
	 * Asynctask for setting up the twitter connection with an intent.
	 *
	 */
	private class TwitterSetup extends AsyncTask<String, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				twitter = new TwitterConnection(getResources());
				String authorizationUrl = twitter.setup();
				
				if(token == null || tokenSecret == null) {
					Intent authIntent = new Intent(MainActivity.this, GetPINActivity.class);
					authIntent.putExtra("url", authorizationUrl);
					startActivityForResult(authIntent, 0);
				} else {
					sendTweet();
				}
			} catch (TwitterException e) {
				e.printStackTrace();
			}

			return null;
		}
	}

	/**
	 * Asynctask for sending the tweet.
	 *
	 */
	private class TwitterAuthentication extends AsyncTask<Intent, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Intent... datas) {
			Intent data = datas[0];

			try {
				String pin = data.getStringExtra("pin");
				AccessToken accessToken = twitter.authenticate(pin);
				token = accessToken.getToken();
				tokenSecret = accessToken.getTokenSecret();

				SharedPreferences.Editor edit = prefs.edit();
				edit.putString("token", token);
				edit.putString("tokenSecret", tokenSecret);
				edit.apply();
				
				sendTweet();
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	
}
