package se.lisaannica.stopmotion;

import java.io.File;

import twitter4j.TwitterException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SendTweetActivity extends Activity{

	private TwitterConnection twitter;
	private String token;
	private String tokenSecret;
	private String gifPath;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_tweet);
		
		token = getIntent().getStringExtra("token");	
		tokenSecret = getIntent().getStringExtra("tokenSecret");
		gifPath = getIntent().getStringExtra("gifPath");
	}

	public void tweetOnClick (View v) {
		System.out.println("In onclick");

		if (v.getId() == R.id.button_send_tweet) {
			new TweetSender().execute();
		} else if(v.getId() == R.id.button_dont_send) {
			Intent intent = new Intent(SendTweetActivity.this, MainActivity.class);
			startActivity(intent);
		}
	}

	private class TweetSender extends AsyncTask<Intent, Integer, Boolean> {

		private ProgressDialog pd;
		
		@Override
		protected Boolean doInBackground(Intent... arg0) {
			EditText et = (EditText) findViewById(R.id.editText_tweet_text);

			try {
				twitter = new TwitterConnection(getResources());
				twitter.setup();
				
				Uri screenshotUri = Uri.parse(gifPath); 
				String twitpicUrl = twitter.uploadImage(
						new File(screenshotUri.toString()), token, tokenSecret);
				twitter.sendSecondTweet(et.getText() + " #stopmotion " + twitpicUrl, token, tokenSecret);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPreExecute() {
			//Shows the dialog
			pd = ProgressDialog.show(SendTweetActivity.this, "", getResources().getString(R.string.send_tweet_sending)); 
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			//Closes the dialog
			pd.dismiss();
			
			Toast.makeText(SendTweetActivity.this, R.string.send_tweet_sent, Toast.LENGTH_LONG).show();

			Intent intent = Intent.makeRestartActivityTask(
					new ComponentName("se.lisaannica.stopmotion","se.lisaannica.stopmotion.MainActivity"));
			startActivity(intent); 
		}
	}
}
