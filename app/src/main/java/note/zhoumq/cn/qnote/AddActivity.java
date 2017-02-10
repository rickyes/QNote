package note.zhoumq.cn.qnote;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.xutils.ex.DbException;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;


public class AddActivity extends AppCompatActivity implements OnClickListener {

	private EditText edt_title,edt_content;
	private FloatingActionButton fab_add;
	private FireworkView mFireworkView,fireworkView_pwd;
	private LoadingView fan_load;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add);
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		if(actionBar != null){
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		initView();
	}
	
	private void initView() {
		fab_add = (FloatingActionButton) findViewById(R.id.btnFloatingAction);
		fab_add.setOnClickListener(this);
		fan_load = (LoadingView) findViewById(R.id.electric_fan_view);
		edt_title=  (EditText) findViewById(R.id.edt_title);
		edt_content=  (EditText) findViewById(R.id.edt_content);
		mFireworkView = (FireworkView) findViewById(R.id.fire_work);
		fireworkView_pwd = (FireworkView) findViewById(R.id.fire_work_pwd);

		mFireworkView.bindEditText(edt_title);
		fireworkView_pwd.bindEditText(edt_content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnFloatingAction:
			Editable edt_t = edt_title.getText();
			Editable edt_c = edt_content.getText();
			if(!Utils.isEmpty(edt_t)&&!Utils.isEmpty(edt_c)){
				fan_load.setVisibility(View.VISIBLE);
				Utils.showSnackbar(v,"好开心！坚持");
				DataMessage msg = new DataMessage();
				msg.setTitle(edt_t.toString().trim());
				msg.setContent(edt_c.toString().trim());
				msg.setDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
				try {
					Utils.getDbManager().save(msg);
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(2000);
								setResult(RESULT_OK, new Intent().putExtra("save", "save"));
								overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
								finish();
								return;
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}).start();
				} catch (DbException e) {
					e.printStackTrace();
					Utils.toast(AddActivity.this, "添加日记失败");
				}
			}else{
				Utils.toast(AddActivity.this, "补全信息");
			}
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				setResult(RESULT_OK, new Intent().putExtra("back","back"));
				finish();
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
