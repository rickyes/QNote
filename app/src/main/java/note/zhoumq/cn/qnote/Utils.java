package note.zhoumq.cn.qnote;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.x;
import org.xutils.ex.DbException;

import android.content.Context;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

public class Utils {
	
	static DaoConfig daoConfig;

	public static boolean isEmpty(Editable edt){
		if(!TextUtils.isEmpty(edt)){
			return false;
		}
		return true;
	}

	public static void showSnackbar(View v, String snackbarStr){
		final Snackbar mSnackbar = Snackbar.make(v,snackbarStr,Snackbar.LENGTH_LONG);
		mSnackbar.show();
	}
	
	public static void toast(Context context,String content){
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}


	public static int daysOfTwo(Date originalDate, Date compareDateDate) {
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(originalDate);
		int originalDay = aCalendar.get(Calendar.DAY_OF_YEAR);
		aCalendar.setTime(compareDateDate);
		int compareDay = aCalendar.get(Calendar.DAY_OF_YEAR);

		return originalDay - compareDay;
	}

	public static String FriendlyDate(Date compareDate) {
		Date nowDate = new Date();
		int dayDiff = daysOfTwo(nowDate, compareDate);

		if (dayDiff <= 0)
			return "今天";
		else if (dayDiff == 1)
			return "昨天";
		else if (dayDiff == 2)
			return "前天";
		else
			return new SimpleDateFormat("M月d日 E").format(compareDate);
	}


	public static DaoConfig getDaoConfig(){
		File file = new File(Environment.getExternalStorageDirectory().getPath());
		if(daoConfig == null){
			daoConfig = new DaoConfig()
					.setDbName("QNote.db")
					.setDbDir(file)
					.setAllowTransaction(true);
		}
		return daoConfig;
	}
	
	public static DbManager getDbManager() {
		return x.getDb(getDaoConfig());
	}
	
	public static void query(final Context context,final List<DataMessage>list,final HomeAdapter adapter){
		try {
			List<DataMessage> msgs = getDbManager().findAll(DataMessage.class);
			if(msgs==null||msgs.size()==0){
				Utils.toast(context, "开始第一篇日记吧");
			}else{
				list.removeAll(list);
				int i = 0;
				for(DataMessage DaMsg:msgs){
					list.add(DaMsg);
					adapter.notifyDataSetChanged();
				}
			}
		} catch (DbException e) {
			System.out.println("msgs error");
			Utils.toast(context, "插入数据失败");
			e.printStackTrace();
		}

	}
	
}
