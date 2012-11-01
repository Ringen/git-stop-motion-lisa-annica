package se.lisaannica.stopmotion;

import java.io.File;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

	public void sendTweetOnClick (View v) {
		System.out.println("In onclick");
		if (v.getId() == R.id.button_send_tweet) {
			AsyncTask at = new TweetSender().execute();
		}
	}

	private class TweetSender extends AsyncTask<Intent, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Intent... arg0) {

			EditText et = (EditText) findViewById(R.id.editText_tweet_text);

			try {
				twitter = new TwitterConnection(getResources());
				String authorizationUrl = twitter.setup();
				
				
				Uri screenshotUri = Uri.parse(gifPath); 
				String twitpicUrl = twitter.uploadImage(
						new File(screenshotUri.toString()), token, tokenSecret);
				twitter4j.Status sentStatus = twitter.sendSecondTweet(et.getText() + " #stopmotion " + twitpicUrl, token, tokenSecret);
			
				//TODO
				//Close and start main again and toast
			} catch (TwitterException e) {
				Log.d("show", "SendTweetActivity, TweetSender, doInBackground: Could not send tweet");
				e.printStackTrace();
			}
			return null;
		}
	}
}
