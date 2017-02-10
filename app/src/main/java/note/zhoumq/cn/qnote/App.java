package note.zhoumq.cn.qnote;

import org.xutils.x;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;

public class App extends Application {
	
	public static SharedPreferences sp;
	public static int[] img_avatar = {
			R.mipmap.card1,R.mipmap.card2,R.mipmap.card3,R.mipmap.card4,
			R.mipmap.card6,R.mipmap.card9,
			R.mipmap.card11,R.mipmap.card12,R.mipmap.ic_avatar
	};

	@Override
	public void onCreate() {
		super.onCreate();
		x.Ext.init(this);
		x.Ext.setDebug(false);
		sp = getSharedPreferences("config", MODE_PRIVATE);
	}
	
}
