package dalcoms.pub.mathkids;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	public final static String EXTRA_COLOR_THEME = "dalcoms.pub.mathkids.colorTheme";
	public final static String EXTRA_SELECTED_MENU = "dalcoms.pub.mathkids.selectedMenu";
	public final static String EXTRA_SELECTED_MAIN_MENU = "dalcoms.pub.mathkids.selectedMainMenu";
	
	TextView tvMainMenuTitle;
	private Vibrator vibe;
	static final int VIBE_TOUCH = 25;
	
//	SoundPool mSoundPool = null;
//	private int spId_startUp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		
        setContentView(R.layout.activity_main);
        initActivity();

	}
	
	@Override
	public void onBackPressed(){
		if (Math.random()<0.8){
			popUpExtiMessageDlg();
		}else{
			popUpAdMsgDlg();
		}
	}
	
	private void popUpExtiMessageDlg(){
		AlertDialog.Builder dlgBackPressed = new AlertDialog.Builder(this);
		dlgBackPressed.setMessage(R.string.say_good_bye)
		.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.exit(0);
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.setTitle("Exit")
		.setIcon(R.drawable.mathkids_ico)
		.show();
	}
	private void popUpAdMsgDlg(){
		AlertDialog.Builder dlgBackPressed = new AlertDialog.Builder(this);
		dlgBackPressed.setMessage(R.string.advertize_myself)
		.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try{
					startActivity(
							new Intent(Intent.ACTION_VIEW,
									Uri.parse("market://search/?q=pub:Dalcoms")));
				}catch(android.content.ActivityNotFoundException e){
					startActivity(
							new Intent(Intent.ACTION_VIEW,
									Uri.parse("https://play.google.com/store/search?q=dalcoms")));
				}
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				popUpExtiMessageDlg();
			}
		})
		.setTitle("Free app")
		.setIcon(R.drawable.mathkids_ico)
		.show();
	}
	
	protected void initActivity(){
		rootStartAnim();
		this.tvMainMenuTitle = (TextView) findViewById(R.id.mainMenuTitle);
	}
	
/*	protected void init_soundPool(Context context){
		mSoundPool = new SoundPool(16,AudioManager.STREAM_MUSIC,0);
		spId_startUp = mSoundPool.load(context, R.raw.start,1);
	}
	
	protected void startupSoundOn(){
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mSoundPool.play(spId_startUp, 0.5f, 0.5f, 1, 0, 1);
			}
		}, 1000);
		
	}*/
	
	/* Start root menu layout activity with animation.
	 * */
	protected void rootStartAnim(){
		startLayoutAnim();
		startFrameAnim();
	}
	protected void startLayoutAnim(){
		LinearLayout layoutMainMenu = (LinearLayout) findViewById(R.id.main_root);
        Animation animRootStart = AnimationUtils.loadAnimation(this, R.anim.anim_root_start);
        layoutMainMenu.startAnimation(animRootStart);
	}
	protected void startFrameAnim(){
		ImageView ivAnimTitle=(ImageView)findViewById(R.id.mainMenuTitleAnimation);
//		ivAnimTitle.setVisibility(View.VISIBLE);
		ivAnimTitle.setBackgroundResource(R.drawable.anim_main_title);
		AnimationDrawable frameAnim = (AnimationDrawable) ivAnimTitle.getBackground();
		frameAnim.start();
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/* Called when one of menu is selected. 
	 * */
	public void goMathPractice(View view){
		Intent intent = new Intent(this,MathPracticeActivity.class);
		Intent intentShape = new Intent(this,SameShapeActivity.class);
		vibe.vibrate(VIBE_TOUCH);
		
		switch(view.getId()){//put Extra here.
		case R.id.sub_menu_add_1:
			intent.putExtra(EXTRA_COLOR_THEME, this.getResources().getColor(R.color.addition_theme_color));
			intent.putExtra(EXTRA_SELECTED_MENU, "addtitionType1");
			startActivity(intent);
			break;
		case R.id.sub_menu_add_2:
			intent.putExtra(EXTRA_COLOR_THEME, this.getResources().getColor(R.color.addition_theme_color));
			intent.putExtra(EXTRA_SELECTED_MENU, "addtitionType2");
			startActivity(intent);
			break;
		case R.id.sub_menu_sub_1:
			intent.putExtra(EXTRA_COLOR_THEME, this.getResources().getColor(R.color.subtraction_theme_color));
			intent.putExtra(EXTRA_SELECTED_MENU, "subtractionType1");
			startActivity(intent);
			break;
		case R.id.sub_menu_sub_2:
			intent.putExtra(EXTRA_COLOR_THEME, this.getResources().getColor(R.color.subtraction_theme_color));
			intent.putExtra(EXTRA_SELECTED_MENU, "subtractionType2");
			startActivity(intent);
			break;
		case R.id.same_shape_right:
		case R.id.same_shape_left:
			intentShape.putExtra(EXTRA_COLOR_THEME, this.getResources().getColor(R.color.same_shape_color));
			intentShape.putExtra(EXTRA_SELECTED_MAIN_MENU, "sameShape");
//			intentShape.putExt
			startActivity(intentShape);
			break;
		case R.id.count_num_left:
		case R.id.count_num_right:
			intentShape.putExtra(EXTRA_COLOR_THEME, this.getResources().getColor(R.color.count_num_color));
			intentShape.putExtra(EXTRA_SELECTED_MAIN_MENU, "countNum");
			startActivity(intentShape);
			break;
		}
		
	}
	
	public void selectSubMenu(View view){
		Intent intent = new Intent(this,PopUpSubMenu.class);
		vibe.vibrate(VIBE_TOUCH);
		
		switch(view.getId()){//put Extra here.
		case R.id.addition_left:
			intent.putExtra(EXTRA_COLOR_THEME, this.getResources().getColor(R.color.addition_theme_color));
			intent.putExtra(EXTRA_SELECTED_MAIN_MENU, "addition");
			break;
		case R.id.subtraction_left:
			intent.putExtra(EXTRA_COLOR_THEME, this.getResources().getColor(R.color.subtraction_theme_color));
			intent.putExtra(EXTRA_SELECTED_MAIN_MENU, "subtraction");
			Log.v("sub","here");
			break; 
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);//실행시키는 activity를 history 에 추가시키지 않게 해서 다음 act에서 또 다른 act로 간 뒤 뒤로 가기를 했을때, 실행시키는 act로 안 가고 지금 이곳으로 오게 끔 한다.
		startActivity(intent);
	}
	
	public void justClickOn(View view){
		Animation animClickBounce = AnimationUtils.loadAnimation(this, R.anim.anim_click_bounce);
		vibe.vibrate(VIBE_TOUCH);
		switch(view.getId()){//put Extra here.
		case R.id.mainMenuTitle:
			this.tvMainMenuTitle.startAnimation(animClickBounce);
			break;
		}
	}

}









