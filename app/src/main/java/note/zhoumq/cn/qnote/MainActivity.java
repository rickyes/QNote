package note.zhoumq.cn.qnote;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import com.yalantis.phoenix.PullToRefreshView;
import note.zhoumq.cn.qnote.HomeAdapter.OnItemClickLitener;

public class MainActivity extends AppCompatActivity implements OnClickListener {

	private RecyclerView lv;
	public static DailyAdapater adapter;
	public static HomeAdapter home_adapter;
	private List<DataMessage> list;
	public final static int REQUEST_CODE = 0x00;
	public final static int ITEM_CODE = 0x01;

	private PullToRefreshView mPullToRefreshView;
	private FloatingActionButton fab_add;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		initData();
		initEvent();
	}

	private void initEvent() {

		home_adapter.setOnItemClickLitener(new OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {
				Intent it_item = new Intent();
				it_item.setClass(MainActivity.this, DetailActivity.class);
				it_item.putExtra("item", list.get(position));
				it_item.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivityForResult(it_item, ITEM_CODE);
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			}

			@Override
			public void onItemLongClick(View view, int position) {
			}
		});
	}

	private void initView() {
		lv = (RecyclerView) findViewById(R.id.lv);
		lv.setLayoutManager(new LinearLayoutManager(this));
		lv.setItemAnimator(new DefaultItemAnimator());
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
		fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
		fab_add.setOnClickListener(this);
	}

	@SuppressLint("SimpleDateFormat")
	private void initData() {
		list = new ArrayList<DataMessage>();
		home_adapter = new HomeAdapter(this,list);
		lv.setAdapter(home_adapter);
		Utils.query(this, list, home_adapter);
		mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mPullToRefreshView.postDelayed(new Runnable() {
					@Override
					public void run() {
						Utils.query(MainActivity.this, list, home_adapter);
						mPullToRefreshView.setRefreshing(false);
					}
				}, 1000);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.fab_add:
				Intent it_add = new Intent();
				it_add.setClass(MainActivity.this, AddActivity.class);
				it_add.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivityForResult(it_add, REQUEST_CODE);
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
				break;
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE || requestCode == ITEM_CODE) {
			Utils.query(MainActivity.this, list, home_adapter);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.action_about:
				Intent it_about = new Intent();
				it_about.setClass(MainActivity.this, AboutActivity.class);
				it_about.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(it_about);
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
	
