package dalcoms.pub.mathkids;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class PopupOkNg extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_popup_ok_ng);
		
		Display dp = getWindowManager().getDefaultDisplay();
		double dpWidth = (double)dp.getWidth();
		double dpHeight = (double)dp.getHeight();
		Bitmap imgOK = BitmapFactory.decodeResource(getResources(), R.drawable.popup_goog_1);
		int imageWidth = imgOK.getWidth();
		int imageHeight = imgOK.getHeight();
		double scaleFactor = (dpWidth*2/3)/imageWidth;
		Log.v("scaleF",""+scaleFactor);
		Bitmap img_fixed = Bitmap.createScaledBitmap(imgOK, (int)(imageWidth*scaleFactor), (int)(imageHeight*scaleFactor), true);
		ImageView iv = (ImageView)findViewById(R.id.ok_ng_image);
		iv.setImageBitmap(img_fixed);
		
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				onBackPressed();;
			}
		}, 600);
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.popup_ok_ng, menu);
//		return true;
//	}
	
	public void popup_kill_click(View view){
//		onBackPressed();
	}

}
