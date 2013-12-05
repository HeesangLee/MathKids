package dalcoms.pub.mathkids;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class MathPracticeActivity extends Activity {
	private int colorTheme;
	private String practiceKind;
	private HashMap<String, String> mapPracticeKind = new HashMap<String, String>();
	private int score = 0;
	private int okCount = 0;
	private int practiceLevel = 0;
	private String strQuestion="";
	private String strAnswer="";
	
	static final boolean AD_ON = true;
	private AdView adView;
	private int adOnCount=0;
	private boolean flagAdOn=false;
	
	private final int[][] ADDITION_TABLE = {//A_min/A_max/B_min/B_max/OK for Next Level
			{1,5,1,2,2},
			{5,9,1,2,3},
			{5,9,0,9,5},
			{0,9,0,9,5},
			{0,19,0,9,5},
			{0,19,0,19,5},
			{10,30,0,10,5},
			{10,30,10,30,5},
			{0,50,0,50,5}
	};
	
	private final int[][] SUBTRACTION_TABLE = {//A_min/A_max/B_min/B_max/OK for Next Level, B_Max 가 0일 경우 B_Max=A
			{2,5,1,0,2},
			{5,9,1,0,3},
			{5,19,1,0,5},
			{10,20,1,10,5},
			{10,30,5,10,5},
			{10,30,5,0,5},
			{10,30,1,20,5},
			{0,50,0,50,5}
	};
	
	private TextView tvTitle;
	private TextView tvScore;
	private LinearLayout llQuestionContainer;
	private TextView tvQuestion;
	private LinearLayout llAnswerContainer;
	private LinearLayout llAnswerBg_1;
	private LinearLayout llAnswerBg_2;
	private LinearLayout llAnswerBg_3;
	private TextView tvAnswer_1;
	private TextView tvAnswer_2;
	private TextView tvAnswer_3;
	
	private Vibrator vibe;
	static final int VIBE_TOUCH = 25;
	static final int VIBE_OK = 25;
	static final int VIBE_NG = 200;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_math_practice);
		initActivity();
		
		/*// Show the Up button in the action bar.
		setupActionBar();*/
	}

//	덧셈 문제 만들기
	protected HashMap<String, String> makeAddProblem(int pLevel){
		HashMap<String, String> retVal = new HashMap<String, String>();
		Integer a,b,c;
		a = (int)Math.round(Math.random()*(ADDITION_TABLE[pLevel][1]-ADDITION_TABLE[pLevel][0])+ADDITION_TABLE[pLevel][0]);
		b = (int)Math.round(Math.random()*(ADDITION_TABLE[pLevel][3]-ADDITION_TABLE[pLevel][2])+ADDITION_TABLE[pLevel][2]);
		c = a + b;
		retVal.put("a", a.toString());
		retVal.put("b", b.toString());
		retVal.put("c", c.toString());
		
		return retVal;
	}
//	뺄셈 문제 만들기
	protected HashMap<String, String> makeSubtractProblem(int pLevel){
		HashMap<String, String> retVal = new HashMap<String, String>();
		Integer a,b,c;
		int tempMaxB;
		
		a = (int)Math.round(Math.random()*(SUBTRACTION_TABLE[pLevel][1]-SUBTRACTION_TABLE[pLevel][0])+SUBTRACTION_TABLE[pLevel][0]);
		tempMaxB=SUBTRACTION_TABLE[pLevel][3]==0?a:SUBTRACTION_TABLE[pLevel][3];//bMax가 0이면 a를 max로 취한다.
		b = (int)Math.round(Math.random()*(tempMaxB-SUBTRACTION_TABLE[pLevel][2])+SUBTRACTION_TABLE[pLevel][2]);
		c = a - b;
		retVal.put("a", a.toString());
		retVal.put("b", b.toString());
		retVal.put("c", c.toString());
		
		return retVal;
	}
	
	protected void makeProblem(String pKind,int pLevel){
		HashMap<String, String> tempMap = new HashMap<String, String>();
		
		if(pKind.equals("addtitionType1")){
			tempMap = makeAddProblem(pLevel);
			strQuestion = tempMap.get("a") + " + " + tempMap.get("b") + " = ?";
			strAnswer = tempMap.get("c");
		}else if(pKind.equals("addtitionType2")){
			tempMap = makeAddProblem(pLevel);
			strQuestion = tempMap.get("a") + " + ? = " + tempMap.get("c");
			strAnswer = tempMap.get("b");
		}else if(pKind.equals("subtractionType1")){
			tempMap = makeSubtractProblem(pLevel);
			strQuestion = tempMap.get("a") + " - " + tempMap.get("b") + " = ?";
			strAnswer = tempMap.get("c");
		}else if(pKind.equals("subtractionType2")){
			tempMap = makeSubtractProblem(pLevel);
			strQuestion = tempMap.get("a") + " - ? = " + tempMap.get("c");
			strAnswer = tempMap.get("b");
		}
		
		writeQnA();
	}
	protected void writeQnA(){
		ArrayList<Integer> ansTotalList = new ArrayList<Integer>();
		ArrayList<Integer> ansList = new ArrayList<Integer>();
		
		int ansOk,ansMin;
		int j=0;
		
		this.tvQuestion.setText(strQuestion);
		
		ansOk = Integer.parseInt(strAnswer);
		if(ansOk<0){//답이 음수일 경우 음수 포함.
			ansMin = ansOk-10;
		}else{//답이 양수일 경우 양수만 포함.
			ansMin = ansOk-10<0 ? 0 : ansOk-10;
		}
		
		for (int i = 0 ; i<20 ; i++){
			ansTotalList.add(ansMin+i);
		}
		Collections.shuffle(ansTotalList);
		Iterator<Integer> itr = ansTotalList.iterator();

		while (itr.hasNext()){
			Integer temp=itr.next();
			if(temp!=ansOk){
				ansList.add(temp);
				j++;
			}
			if(j>1){
				break;
			}
		}
		ansList.add(ansOk);
		Collections.shuffle(ansList);

		tvAnswer_1.setText(ansList.get(0).toString());
		tvAnswer_2.setText(ansList.get(1).toString());
		tvAnswer_3.setText(ansList.get(2).toString());
	}
	protected void initActivity(){
		this.vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		importViews();
		getExtraFromRoot();
		makePracticeKindMap();
		setColorTheme();
		setTitleText();
		rootStartAnim();
		
		makeProblem(this.practiceKind,this.practiceLevel);
	}

	
	protected void importViews(){
		this.tvTitle = (TextView)findViewById(R.id.practice_title);
		this.tvScore = (TextView)findViewById(R.id.practice_score);
//		this.llQuestionContainer = (LinearLayout)findViewById(R.id.question_container);
		this.tvQuestion = (TextView)findViewById(R.id.question);
//		this.llAnswerContainer = (LinearLayout)findViewById(R.id.answer_container);
		this.llAnswerBg_1 = (LinearLayout)findViewById(R.id.answer_1_bg);
		this.llAnswerBg_2 = (LinearLayout)findViewById(R.id.answer_2_bg);
		this.llAnswerBg_3 = (LinearLayout)findViewById(R.id.answer_3_bg);
		this.tvAnswer_1 = (TextView)findViewById(R.id.answer_1);
		this.tvAnswer_2 = (TextView)findViewById(R.id.answer_2);
		this.tvAnswer_3 = (TextView)findViewById(R.id.answer_3);
	}
	protected void makePracticeKindMap(){
		mapPracticeKind.put("addtitionType1", this.getResources().getString(R.string.menu_addition));
		mapPracticeKind.put("addtitionType2", this.getResources().getString(R.string.menu_addition));
		mapPracticeKind.put("subtractionType1", this.getResources().getString(R.string.menu_subtraction));
		mapPracticeKind.put("subtractionType2", this.getResources().getString(R.string.menu_subtraction));
		mapPracticeKind.put("make10", this.getResources().getString(R.string.menu_make10));
	}
	protected void getExtraFromRoot(){
		Intent intent = getIntent();
		this.colorTheme = intent.getIntExtra(MainActivity.EXTRA_COLOR_THEME,android.R.color.black);
		this.practiceKind = intent.getStringExtra(MainActivity.EXTRA_SELECTED_MENU);
	}
	protected void setTitleText(){
		tvTitle.setText((String)mapPracticeKind.get(this.practiceKind));
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
		LinearLayout layoutTitleBar = (LinearLayout) findViewById(R.id.title_bar);
		layoutTitleBar.setBackgroundColor(this.colorTheme);
	}
	protected void setColorThemeQuestion(){
		View QeustionLeft = (View) findViewById(R.id.questionLeft);
		QeustionLeft.setBackgroundColor(this.colorTheme);
		
		GradientDrawable questionBgSahpe = (GradientDrawable) this.tvQuestion.getBackground();
		questionBgSahpe.setStroke(2, this.colorTheme);
		
		View QeustionRight = (View) findViewById(R.id.questionRight);
		QeustionRight.setBackgroundColor(this.colorTheme);
	}
	protected void rootStartAnim(){
		LinearLayout layoutPracticeRoot = (LinearLayout) findViewById(R.id.practice_root_layout);
        Animation animPracticeStart = AnimationUtils.loadAnimation(this, R.anim.anim_practice_start);
        layoutPracticeRoot.startAnimation(animPracticeStart);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

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

	protected boolean checkAnswer(String ans){
		boolean okNg=false;
		if (ans.equalsIgnoreCase(this.strAnswer)){//OK
			this.tvScore.setText(Integer.toString(++this.score));
			if(this.practiceKind.equalsIgnoreCase("addtitionType1")
					||this.practiceKind.equalsIgnoreCase("addtitionType2")){//Addition
				if(++this.okCount>this.ADDITION_TABLE[this.practiceLevel][4]){//level up
					this.okCount = 0;
					this.practiceLevel = this.practiceLevel<this.ADDITION_TABLE.length-1?
							this.practiceLevel+1:
								this.practiceLevel;
				}
			}else if(this.practiceKind.equalsIgnoreCase("subtractionType1")
					||this.practiceKind.equalsIgnoreCase("subtractionType2")){//Subtraction
				if(++this.okCount>this.SUBTRACTION_TABLE[this.practiceLevel][4]){//level up
					this.okCount = 0;
					this.practiceLevel = this.practiceLevel<this.SUBTRACTION_TABLE.length-1?
							this.practiceLevel+1:
								this.practiceLevel;
				}
			}
			okNg=true;
			
		}else{//Wrong.
			if(--this.okCount<0){//Level down.
				this.okCount=0;
				this.practiceLevel=this.practiceLevel>0?this.practiceLevel-1:0;
			}
		}
		return okNg;
	}
	protected void makeNextProblem(){
		makeProblem(this.practiceKind,this.practiceLevel);
		Animation animNextQuestion = AnimationUtils.loadAnimation(this, R.anim.anim_question_ok);
		setColorThemeAnswer();
		this.tvQuestion.startAnimation(animNextQuestion);
	}
	public void onClickEvent(View view){
//		if(++this.adOnCount>Math.round(Math.random()*3)){
			if (AD_ON && this.flagAdOn==false) {
	        	AdRequest adrequest = new AdRequest();
//	        	adrequest.setGender(AdRequest.Gender.FEMALE);
	        	adView = (AdView) findViewById(R.id.adView);
	            adView.loadAd(adrequest);
	            this.flagAdOn = true;
	        }
//		}
		switch(view.getId()){//put Extra here.
		case R.id.goto_home_menu: // go to home menu activity.
			goToHomeMenuActivity();
			vibe.vibrate(VIBE_TOUCH);
			break;
		case R.id.answer_1:
			if(checkAnswer(this.tvAnswer_1.getText().toString())){//ok-->next question
				makeNextProblem();
				vibe.vibrate(VIBE_OK);
			}else{//wrong -->change selected view answer.
				Animation animWrongAnswer = AnimationUtils.loadAnimation(this, R.anim.anim_click_bounce);
				this.tvQuestion.startAnimation(animWrongAnswer);
				this.llAnswerBg_1.setBackgroundColor(this.getResources().getColor(R.color.wrongAnswer));
				vibe.vibrate(VIBE_NG);
			}
			break;
		case R.id.answer_2:
			if(checkAnswer(this.tvAnswer_2.getText().toString())){//ok-->next question
				makeNextProblem();
				vibe.vibrate(VIBE_OK);
			}else{//wrong -->change selected view answer.
				Animation animWrongAnswer = AnimationUtils.loadAnimation(this, R.anim.anim_click_bounce);
				this.tvQuestion.startAnimation(animWrongAnswer);
				this.llAnswerBg_2.setBackgroundColor(this.getResources().getColor(R.color.wrongAnswer));
				vibe.vibrate(VIBE_NG);
			}
			break;
		case R.id.answer_3:
			if(checkAnswer(this.tvAnswer_3.getText().toString())){//ok-->next question
				makeNextProblem();
				vibe.vibrate(VIBE_OK);
			}else{//wrong -->change selected view answer.
				Animation animWrongAnswer = AnimationUtils.loadAnimation(this, R.anim.anim_click_bounce);
				this.tvQuestion.startAnimation(animWrongAnswer);
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



































