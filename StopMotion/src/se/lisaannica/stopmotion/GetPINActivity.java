package se.lisaannica.stopmotion;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GetPINActivity extends Activity implements OnClickListener {
  
  private Button mContinueButton;
  private Button mCancelButton;
  private EditText mEditText;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_get_pin);
    
    String url = getIntent().getStringExtra("url");
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(url));
    startActivity(i);

    mEditText = (EditText) findViewById(R.id.editText_pin);
    mContinueButton = (Button) findViewById(R.id.button_continue);
    mCancelButton = (Button) findViewById(R.id.button_cancel);
    
    mContinueButton.setOnClickListener(this);
    mCancelButton.setOnClickListener(this);
    
    int duration = 7000;
    Toast.makeText(GetPINActivity.this, getResources().getString(R.string.send_instruction), duration).show();
  }

  public void onClick(View v) {
    if (v.getId() == R.id.button_cancel) {      
      setResult(RESULT_CANCELED);
      finish();
      return;
    }
    Intent returnIntent = new Intent();
    returnIntent.putExtra("pin", mEditText.getText().toString());
    setResult(RESULT_OK, returnIntent);
    finish();
  }

}
