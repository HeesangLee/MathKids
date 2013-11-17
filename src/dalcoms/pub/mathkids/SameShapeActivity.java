//TODO :각 view의 색상을 시작할 때, 수동으로 지정할 것.
package dalcoms.pub.mathkids;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class SameShapeActivity extends Activity {

	private int colorTheme;
	private String practiceKind;
	private int score = 0;
	private int okCount = 0;
	private int practiceLevel = 0;
	private int mAnswer_num = 0;
	private int cntNumbyLevel[]={3,5,9,16,20,25,30};
	
	private TextView tvTitle;
	private TextView tvScore;
	private LinearLayout llQuestionContainer;
	private LinearLayout llQuestionBg;
	private ImageView ivQuestion;
	private LinearLayout llAnswerContainer;
	private LinearLayout llAnswerBg_1;
	private LinearLayout llAnswerBg_2;
	private LinearLayout llAnswerBg_3;
	private ImageView ivAnswer_1;
	private ImageView ivAnswer_2;
	private ImageView ivAnswer_3;
	
	private final int mShapeNum=26;
	private ArrayList<Integer> mShapeImgIndex = new ArrayList<Integer>();
	private ArrayList<Bitmap> mShapeImgLarge = new ArrayList<Bitmap>();
	private ArrayList<Bitmap> mShapeImgSmall = new ArrayList<Bitmap>();
	
	private final int mCharacterNum=34;
	private ArrayList<Integer> mCharacterImgIndex = new ArrayList<Integer>();
	private ArrayList<Bitmap> mCharacterImg= new ArrayList<Bitmap>();
	
	private Point pQuestionSize = new Point(); 
	private Point pAnswerSize = new Point();
//	private Point pCharacterImgSize = new Point();

	private final int MAX_CNT_NUM = 30;
	private final double QuestionSizeRatioWidth = 0.8625; //display window 사이즈에 대한 질문 영역의 사이즈 비율
	private final double QuestionSizeRatioHeight = 0.4561;
	private final double QuestionAdSizeRatioHeight = 0.4155;
	private final double AnswerSizeRatioWidth = 0.2597;
	private final double AnswerSizeRatioHeight = 0.2508;
	private final double AnswerAdSizeRatioHeight = 0.2289;
	private ArrayList<Integer> alCntNumExample = new ArrayList<Integer>();//숫자 세기의 보기 저장...->답을 맞춰보기 위해서.
	
	static final boolean AD_ON = true;
	private AdView adView;
	private int adOnCount=0;
	private boolean flagAdOn=false;
	
	private Vibrator vibe;
	static final int VIBE_TOUCH = 25;
	static final int VIBE_OK = 25;
	static final int VIBE_NG = 200;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_same_shape);
		initActivity();
		
		/*// Show the Up button in the action bar.
		setupActionBar();*/
	}
	
	
	protected void makeSameShapeProblem(){
		Collections.shuffle(mShapeImgIndex);
		mAnswer_num = (int)Math.round(Math.random()*2);
		
		ivQuestion.setImageBitmap(mShapeImgLarge.get(mShapeImgIndex.get(mAnswer_num)));
		ivAnswer_1.setImageBitmap(mShapeImgSmall.get(mShapeImgIndex.get(0)));
		ivAnswer_2.setImageBitmap(mShapeImgSmall.get(mShapeImgIndex.get(1)));
		ivAnswer_3.setImageBitmap(mShapeImgSmall.get(mShapeImgIndex.get(2)));
	}
	protected void makeProblem(){
		if(this.practiceKind.equalsIgnoreCase("sameShape")){
			makeSameShapeProblem();
		}else if(this.practiceKind.equalsIgnoreCase("countNum")){
			makeCntNumProblem();
		}
	}
	protected void makeCntNumProblem(){
		Point matrixLocation = new Point();
		Point divBg = new Point();
		ArrayList<RectF> rectPos = new ArrayList<RectF>();
		Bitmap bmpQestionBg = null;
		
		bmpQestionBg = Bitmap.createBitmap(
				this.pQuestionSize.x, 
				this.pQuestionSize.y, 
				Bitmap.Config.ARGB_8888);
		
		//Shuffle the image index array. -> 문제를 생성할 때 마다 이미지를 섞어서 다양한 문제인 듯 보이게 할 것.
		Collections.shuffle(mCharacterImgIndex);
		//Select answer(number of images)
		if(this.practiceLevel<this.cntNumbyLevel.length){
			mAnswer_num = (int)Math.round(Math.random()*(this.cntNumbyLevel[this.practiceLevel]-1))+1;
		}else{
			mAnswer_num = (int)Math.round(Math.random()*(this.MAX_CNT_NUM-1))+1;
		}
		Log.v("level","level : "+this.practiceLevel+"maxNum : "+this.cntNumbyLevel[this.practiceLevel]);
		
		//decide location matrix
		matrixLocation.x = (int)Math.round(Math.sqrt(mAnswer_num)+0.45);
		matrixLocation.y = (int)Math.round(Math.sqrt(mAnswer_num));
		divBg.x =(int)((float)this.pQuestionSize.x / (float)matrixLocation.x);
		divBg.y =(int)((float)this.pQuestionSize.y / (float)matrixLocation.y);
		//calculate the image position.
		for (int y=0;y<matrixLocation.y;y++){  
			for (int x=0;x<matrixLocation.x;x++){
				rectPos.add(new RectF(x*divBg.x, y*divBg.y, (x+1)*divBg.x, (y+1)*divBg.y));
			}
		}
		//Shuffle rectPos : 위치를 섞어서 이미지 위치를 덜 딱딱하게 ....
		Collections.shuffle(rectPos);
		//Draw images on question imageView.
		Canvas cv = new Canvas(bmpQestionBg);
		for (int i=0;i<mAnswer_num;i++){
			cv.drawBitmap(this.mCharacterImg.get(this.mCharacterImgIndex.get(i)),null,rectPos.get(i),null);
		}
		this.ivQuestion.setImageBitmap(bmpQestionBg);
		//Draw answer text 
		makeCntNumAnswer();
	}
	protected void drawCntNumAnswer(){
		//Draw images on question imageView.
		Bitmap bmpAnswer = null;
		Canvas cvAnswer = null;

		Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
		paintText.setStyle(Paint.Style.FILL);
		paintText.setTextAlign(Paint.Align.CENTER);
		paintText.setColor(0xffffffff);
		paintText.setTextSize(this.pAnswerSize.x>>1);
		
		bmpAnswer = Bitmap.createBitmap(
				this.pAnswerSize.x, 
				this.pAnswerSize.y, 
				Bitmap.Config.ARGB_8888);
		cvAnswer = new Canvas(bmpAnswer);
		cvAnswer.drawText(this.alCntNumExample.get(0).toString(), this.pAnswerSize.x>>1, this.pAnswerSize.y>>1, paintText);
		this.ivAnswer_1.setImageBitmap(bmpAnswer);
		
		bmpAnswer = Bitmap.createBitmap(
				this.pAnswerSize.x, 
				this.pAnswerSize.y, 
				Bitmap.Config.ARGB_8888);
		cvAnswer = new Canvas(bmpAnswer);
		cvAnswer.drawText(this.alCntNumExample.get(1).toString(), this.pAnswerSize.x>>1, this.pAnswerSize.y>>1, paintText);
		this.ivAnswer_2.setImageBitmap(bmpAnswer);
		
		bmpAnswer = Bitmap.createBitmap(
				this.pAnswerSize.x, 
				this.pAnswerSize.y, 
				Bitmap.Config.ARGB_8888);
		cvAnswer = new Canvas(bmpAnswer);
		cvAnswer.drawText(this.alCntNumExample.get(2).toString(), this.pAnswerSize.x>>1, this.pAnswerSize.y>>1, paintText);
		this.ivAnswer_3.setImageBitmap(bmpAnswer);
		
		Log.v("text",""+this.alCntNumExample);
	}
	protected void makeCntNumAnswer(){
		int exMin_1,exMin_2,exMax_1,exMax_2;
		final int exRange = 4;
		
		this.alCntNumExample.clear();
		
		if(this.mAnswer_num<exRange+2){
			exMin_1 = this.mAnswer_num+1;
			exMax_1 = exMin_1+exRange;
			exMin_2 = exMax_1+1;
			exMax_2 = exMin_2+exRange;
		}else{
			exMax_1 = this.mAnswer_num-1;
			exMin_1 = exMax_1-exRange;
			exMin_2 = this.mAnswer_num+1;
			exMax_2 = exMin_2+exRange;
		}
		this.alCntNumExample.add(this.mAnswer_num);
		this.alCntNumExample.add((int)Math.round(Math.random()*(exMax_1-exMin_1)+exMin_1));
		this.alCntNumExample.add((int)Math.round(Math.random()*(exMax_2-exMin_2)+exMin_2));
		
		Collections.shuffle(this.alCntNumExample);
		drawCntNumAnswer();		
	}

	protected void initActivity(){
		this.vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		importViews();
		getExtraFromRoot();
		setColorTheme();
		setTitleText();
		rootStartAnim();
		loadImages();
		if(this.practiceKind.equalsIgnoreCase("countNum")){//CntNum 에서 이미지를 뿌리기 위한 Q/A 의 View size 획득
			getQnAViewSize(false);
//			prepareCntNumQuestion();
		}
		makeProblem();
	}
	protected void getQnAViewSize(boolean adSet){
		Display dp = getWindowManager().getDefaultDisplay();
		double dpWidth = (double)dp.getWidth();
		double dpHeight = (double)dp.getHeight();
		
		if(adSet){//Admob enabled.
			this.pQuestionSize.x = (int) Math.round(dpWidth*QuestionSizeRatioWidth);
			this.pQuestionSize.y = (int) Math.round(dpHeight*QuestionAdSizeRatioHeight);
			this.pAnswerSize.x = (int) Math.round(dpWidth*AnswerSizeRatioWidth);
			this.pAnswerSize.y = (int) Math.round(dpHeight*AnswerAdSizeRatioHeight);
		}else{//Admob disabled
			this.pQuestionSize.x = (int) Math.round(dpWidth*QuestionSizeRatioWidth);
			this.pQuestionSize.y = (int) Math.round(dpHeight*QuestionSizeRatioHeight);
			this.pAnswerSize.x = (int) Math.round(dpWidth*AnswerSizeRatioWidth);
			this.pAnswerSize.y = (int) Math.round(dpHeight*AnswerSizeRatioHeight);
		}
	}
	
	protected void loadSameShapeImages(){
		for(int i=0;i<mShapeNum;i++){//load shape_large image
			String resName = "@drawable/shape_large_"+(i+1);
			String packName = this.getPackageName();
			int resId = getResources().getIdentifier(resName, "drawable", packName);
			mShapeImgLarge.add(BitmapFactory.decodeResource(this.getResources(), resId));
		}
		for(int i=0;i<mShapeNum;i++){//load shape_small image
			String resName = "@drawable/shape_small_"+(i+1);
			String packName = this.getPackageName();
			int resId = getResources().getIdentifier(resName, "drawable", packName);
			mShapeImgSmall.add(BitmapFactory.decodeResource(this.getResources(), resId));
		}
		for(int i=0;i<mShapeNum;i++){
			mShapeImgIndex.add(i);
		}
	}
	protected void loadCountNumImages(){
		for(int i=0;i<this.mCharacterNum;i++){//load character image
			String resName = "@drawable/character_"+(i+1);
			String packName = this.getPackageName();
			int resId = getResources().getIdentifier(resName, "drawable", packName);
			this.mCharacterImg.add(BitmapFactory.decodeResource(this.getResources(), resId));
		}

		for(int i=0;i<mCharacterNum;i++){
			this.mCharacterImgIndex.add(i);
		}
	}
	
	protected void loadImages(){
		if(this.practiceKind.equalsIgnoreCase("sameShape")){
			loadSameShapeImages();
		}else if(this.practiceKind.equalsIgnoreCase("countNum")){
			loadCountNumImages();
		}
	}
	
	protected void importViews(){
		this.tvTitle = (TextView)findViewById(R.id.sameShape_practice_title);
		this.tvScore = (TextView)findViewById(R.id.sameShape_practice_score);
		this.ivQuestion = (ImageView)findViewById(R.id.sameShape_question);
		this.llAnswerBg_1 = (LinearLayout)findViewById(R.id.sameShape_answer_1_bg);
		this.llAnswerBg_2 = (LinearLayout)findViewById(R.id.sameShape_answer_2_bg);
		this.llAnswerBg_3 = (LinearLayout)findViewById(R.id.sameShape_answer_3_bg);
		this.ivAnswer_1 = (ImageView)findViewById(R.id.sameShape_answer_1);
		this.ivAnswer_2 = (ImageView)findViewById(R.id.sameShape_answer_2);
		this.ivAnswer_3 = (ImageView)findViewById(R.id.sameShape_answer_3);
		this.llQuestionBg = (LinearLayout)findViewById(R.id.sameShape_question_bg);
	}
	protected void getExtraFromRoot(){
		Intent intent = getIntent();
		this.colorTheme = intent.getIntExtra(MainActivity.EXTRA_COLOR_THEME,android.R.color.black);
		this.practiceKind = intent.getStringExtra(MainActivity.EXTRA_SELECTED_MAIN_MENU);
	}
	protected void setTitleText(){
		Log.v("extra",""+this.practiceKind);
		if(this.practiceKind.equalsIgnoreCase("sameShape")){
			tvTitle.setText(this.getResources().getString(R.string.menu_same_shape));
		}else if(this.practiceKind.equalsIgnoreCase("countNum")){
			tvTitle.setText(this.getResources().getString(R.string.menu_count_num));
		}
	}
	protected void setColorTheme(){
		setColorThemeTitleBar();
		setColorThemeQuestion();
		setColorThemeAnswer();
	}
	protected void setColorThemeAnswer(){
		llAnswerBg_1.setBackgroundColor(this.colorTheme);
		llAnswerBg_2.setBackgroundColor(this.colorTheme);
		llAnswerBg_3.setBackgroundColor(this.colorTheme);
	}
	protected void setColorThemeTitleBar(){
		LinearLayout layoutTitleBar = (LinearLayout) findViewById(R.id.sameShape_title_bar);
		layoutTitleBar.setBackgroundColor(this.colorTheme);
	}
	protected void setColorThemeQuestion(){
		View QeustionLeft = (View) findViewById(R.id.sameShape_questionLeft);
		QeustionLeft.setBackgroundColor(this.colorTheme);
		
		GradientDrawable questionBgSahpe = (GradientDrawable) this.llQuestionBg.getBackground();
		questionBgSahpe.setStroke(2, this.colorTheme);
		
		View QeustionRight = (View) findViewById(R.id.sameShape_questionRight);
		QeustionRight.setBackgroundColor(this.colorTheme);
	}
	protected void rootStartAnim(){
		LinearLayout layoutPracticeRoot = (LinearLayout) findViewById(R.id.sameShape_root_layout);
        Animation animPracticeStart = AnimationUtils.loadAnimation(this, R.anim.anim_practice_start);
        layoutPracticeRoot.startAnimation(animPracticeStart);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	private void setupActionBar() {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//			getActionBar().setDisplayHomeAsUpEnabled(true);
//		}
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.math_practice, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected boolean checkAnswer(int ans){
		boolean okNg=false;
		
		if(this.practiceKind.equalsIgnoreCase("sameShape")){
			if (mAnswer_num==ans){
				okNg=true;
				this.tvScore.setText(Integer.toString(++this.score));
			}else{
				okNg=false;
			}
		}else if(this.practiceKind.equalsIgnoreCase("countNum")){
			if (mAnswer_num == this.alCntNumExample.get(ans)){
				okNg=true;
				this.tvScore.setText(Integer.toString(++this.score));
				if(++this.okCount > 2){
					this.okCount = 0;
					this.practiceLevel = this.practiceLevel<(cntNumbyLevel.length-1)?
							this.practiceLevel+1:
								cntNumbyLevel.length-1;
/*					//test
					Intent intent = new Intent(this,PopupOkNg.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);//실행시키는 activity를 history 에 추가시키지 않게 해서 다음 act에서 또 다른 act로 간 뒤 뒤로 가기를 했을때, 실행시키는 act로 안 가고 지금 이곳으로 오게 끔 한다.
					startActivity(intent);*/
				}
			}else{
				okNg=false;
				this.okCount = this.okCount>0?this.okCount-1:0;
				if(this.okCount==0){
					this.practiceLevel = this.practiceLevel>0?this.practiceLevel-1:0;
				}
			}
		}
		
		
		return okNg;
	}
	protected void makeNextProblem(){
		makeProblem();
		Animation animNextQuestion = AnimationUtils.loadAnimation(this, R.anim.anim_question_ok);
		setColorThemeAnswer();
		this.ivQuestion.startAnimation(animNextQuestion);
	}
	public void onClickEvent(View view){
		if(++this.adOnCount>Math.round(Math.random()*3)){
			if (AD_ON && this.flagAdOn==false) {
	        	AdRequest adrequest = new AdRequest();
	        	adrequest.setGender(AdRequest.Gender.FEMALE);
	        	adView = (AdView) findViewById(R.id.adView);
	            adView.loadAd(adrequest);
	            this.flagAdOn = true;
	            getQnAViewSize(true);
//	            prepareCntNumQuestion();
	        }
			
		}
		
		switch(view.getId()){//put Extra here.
		case R.id.sameShape_goto_home_menu: // go to home menu activity.
			goToHomeMenuActivity();
			vibe.vibrate(VIBE_TOUCH);
			break;
		case R.id.sameShape_answer_1:
			if(checkAnswer(0)){//ok-->next question
				makeNextProblem();
				vibe.vibrate(VIBE_OK);
			}else{//wrong -->change selected view answer.
				Animation animWrongAnswer = AnimationUtils.loadAnimation(this, R.anim.anim_click_bounce);
				this.llQuestionBg.startAnimation(animWrongAnswer);
				this.llAnswerBg_1.setBackgroundColor(this.getResources().getColor(R.color.wrongAnswer));
				vibe.vibrate(VIBE_NG);
			}
			break;
		case R.id.sameShape_answer_2:
			if(checkAnswer(1)){//ok-->next question
				makeNextProblem();
				vibe.vibrate(VIBE_OK);
			}else{//wrong -->change selected view answer.
				Animation animWrongAnswer = AnimationUtils.loadAnimation(this, R.anim.anim_click_bounce);
				this.llQuestionBg.startAnimation(animWrongAnswer);
				this.llAnswerBg_2.setBackgroundColor(this.getResources().getColor(R.color.wrongAnswer));
				vibe.vibrate(VIBE_NG);
			}
			break;
		case R.id.sameShape_answer_3:
			if(checkAnswer(2)){//ok-->next question
				makeNextProblem();
				vibe.vibrate(VIBE_OK);
			}else{//wrong -->change selected view answer.
				Animation animWrongAnswer = AnimationUtils.loadAnimation(this, R.anim.anim_click_bounce);
				this.llQuestionBg.startAnimation(animWrongAnswer);
				this.llAnswerBg_3.setBackgroundColor(this.getResources().getColor(R.color.wrongAnswer));
				vibe.vibrate(VIBE_NG);
			}
			break;
		}
	}
	protected void goToHomeMenuActivity(){
		Intent intent = new Intent(this,MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
