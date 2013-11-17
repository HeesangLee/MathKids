package dalcoms.pub.mathkids;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PopUpSubMenu extends Activity {
	LinearLayout llPopUpRoot;
//	LinearLayout llPopUpTitleContainer;
//	ImageView ivPopUpCancel;
//	LinearLayout llPopUpSub1Container;
//	ImageView ivPopUpGoSub1;
//	ImageView ivPopUpGoSub2;
	
	private int colorTheme;
	private String practiceKind;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pop_up_sub_menu);
		initActivity();
	}

	protected void initActivity(){
		importViews();
		getExtraFromRoot();
		setColorTheme();
		setTexts();
	}
	protected void importViews(){
		this.llPopUpRoot = (LinearLayout)findViewById(R.id.popup_root);
	}
	protected void getExtraFromRoot(){
		Intent intent = getIntent();
		this.colorTheme = intent.getIntExtra(MainActivity.EXTRA_COLOR_THEME,android.R.color.black);
		this.practiceKind = intent.getStringExtra(MainActivity.EXTRA_SELECTED_MAIN_MENU);
	}
	protected void setColorTheme(){
		this.llPopUpRoot.setBackgroundColor(this.getResources().getColor(R.color.white));
		LinearLayout llTitleContainer = (LinearLayout)findViewById(R.id.popup_title_container);
		llTitleContainer.setBackgroundColor(this.colorTheme);
	}
	protected void setTexts(){
		TextView tvTitle = (TextView)findViewById(R.id.popup_title);
		TextView tvSub1 = (TextView)findViewById(R.id.popup_sub1);
		TextView tvSub2 = (TextView)findViewById(R.id.popup_sub2);
		if (this.practiceKind.equalsIgnoreCase("addition")){
			tvTitle.setText(this.getResources().getString(R.string.menu_addition_selecti));
			tvSub1.setText(this.getResources().getString(R.string.submenu_add_type_1));
			tvSub2.setText(this.getResources().getString(R.string.submenu_add_type_2));
		}else if (this.practiceKind.equalsIgnoreCase("subtraction")){
			tvTitle.setText(this.getResources().getString(R.string.menu_subtraction_select));
			tvSub1.setText(this.getResources().getString(R.string.submenu_sub_type_1));
			tvSub2.setText(this.getResources().getString(R.string.submenu_sub_type_2));
		}
		
	}
	public void onClickLEvent(View view){
		Intent intentGotoPractice = new Intent(this,MathPracticeActivity.class);
		switch(view.getId()){//put Extra here.
		case R.id.popup_sub1:
		case R.id.popup_sub1_go:
			intentGotoPractice.putExtra(MainActivity.EXTRA_COLOR_THEME, this.colorTheme);
			if (this.practiceKind.equalsIgnoreCase("addition")){
				intentGotoPractice.putExtra(MainActivity.EXTRA_SELECTED_MENU, "addtitionType1");
				startActivity(intentGotoPractice);
			}else if (this.practiceKind.equalsIgnoreCase("subtraction")){
				intentGotoPractice.putExtra(MainActivity.EXTRA_SELECTED_MENU, "subtractionType1");
				startActivity(intentGotoPractice);
			}
			break;
		case R.id.popup_sub2:
		case R.id.popup_sub2_go:
			intentGotoPractice.putExtra(MainActivity.EXTRA_COLOR_THEME, this.colorTheme);
			if (this.practiceKind.equalsIgnoreCase("addition")){
				intentGotoPractice.putExtra(MainActivity.EXTRA_SELECTED_MENU, "addtitionType2");
				startActivity(intentGotoPractice);
			}else if (this.practiceKind.equalsIgnoreCase("subtraction")){
				intentGotoPractice.putExtra(MainActivity.EXTRA_SELECTED_MENU, "subtractionType2");
				startActivity(intentGotoPractice);
			}
			break;
		case R.id.popup_cancel:
			onBackPressed();
			break;
		}
	}

}
