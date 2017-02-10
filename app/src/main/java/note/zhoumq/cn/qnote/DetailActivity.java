package note.zhoumq.cn.qnote;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import org.xutils.ex.DbException;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailActivity extends AppCompatActivity{

	private TextView tv_date,tv_over_date;
	private EditText edt_title,edt_content;
	private DataMessage msg;
	private MenuItem menuItem;
	private int i = -1;
	private boolean isUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.detail);
		msg = (DataMessage) getIntent().getSerializableExtra("item");
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		if(actionBar != null){
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		initView();
		initData();
	}

	private void initData() {
		edt_title.setText(msg.getTitle());
		edt_content.setText(msg.getContent());
		tv_date.setText("创建："+msg.getDate());
		if(msg.getOverDate()!=null)
			tv_over_date.setText("最后更新："+msg.getOverDate());
		edt_title.addTextChangedListener(watcher);
		edt_content.addTextChangedListener(watcher);
	}

	private void initView() {
		edt_title = (EditText) findViewById(R.id.detail_title2);
		edt_content = (EditText) findViewById(R.id.detail_content2);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_over_date = (TextView) findViewById(R.id.tv_over_date);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu,menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menuItem = menu.getItem(0);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if(isUpdate){
					update();
				}else{
					setResult(RESULT_OK, new Intent().putExtra("detail","detail"));
					finish();
					overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
					return true;
				}
			case R.id.action_done:
				update();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void update(){
		try {
			final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
					.setTitleText("Loading");
			pDialog.show();
			pDialog.setCancelable(false);
			new CountDownTimer(800 * 7, 800) {
				public void onTick(long millisUntilFinished) {
					// you can change the progress bar color by ProgressHelper every 800 millis
					i++;
					switch (i){
						case 0:
							pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
							break;
						case 1:
							pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
							break;
						case 2:
							pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
							break;
						case 3:
							pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
							break;
						case 4:
							pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
							break;
						case 5:
							pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
							break;
						case 6:
							pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
							break;
					}
				}

				public void onFinish() {
					i = -1;
					setResult(RESULT_OK, new Intent().putExtra("detail","detail"));
					finish();
					overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
				}
			}.start();
			DataMessage data =  Utils.getDbManager().findById(DataMessage.class,msg.getId());
			String titleSrc = edt_title.getText().toString().trim();
			String contentSrc = edt_content.getText().toString().trim();
			String content = contentSrc.replaceAll("\\s*|\t|\r|\n", "");
			String title = titleSrc.replaceAll("\\s*|\t|\r|\n", "");
			data.setTitle(title);
			data.setContent(content);
			data.setOverDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
			Utils.getDbManager().update(data,new String[]{"title","content","overDate"});
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(menuItem==null) return;
			String labelSrc = msg.getTitle();
			String contentSrc = msg.getContent();
			String content = contentSrc.replaceAll("\\s*|\t|\r|\n", "");
			if(TextUtils.isEmpty(edt_content.getText().toString())||TextUtils.isEmpty(edt_title.getText().toString())){
				menuItem.setVisible(false);
				isUpdate = false;
				return;
			}
			if (!TextUtils.isEmpty(content)){
				if (TextUtils.equals(labelSrc, edt_title.getText().toString().trim()) && TextUtils.equals(contentSrc, edt_content.getText().toString().trim())){
					menuItem.setVisible(false);
					isUpdate = false;
					return;
				}
				menuItem.setVisible(true);
				isUpdate = true;
			}else{
				menuItem.setVisible(false);
				isUpdate = false;
			}
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	@Override
	public void onBackPressed() {
		if(isUpdate){
			new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
					.setTitleText("保存更改？")
					.setContentText("")
					.setConfirmText("是")
					.setCancelText("否")
					.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
						@Override
						public void onClick(SweetAlertDialog sweetAlertDialog) {
							setResult(RESULT_OK, new Intent().putExtra("detail","detail"));
							finish();
							overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
						}
					})
					.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
						@Override
						public void onClick(SweetAlertDialog sDialog) {
								update();
						}
					})
					.show();
		}else{
			setResult(RESULT_OK, new Intent().putExtra("detail","detail"));
			finish();
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}
	}
}
