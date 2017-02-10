package note.zhoumq.cn.qnote;

import java.util.List;

import org.xutils.ex.DbException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DailyAdapater extends BaseAdapter {

	private LayoutInflater inflater = null;
	private List<DataMessage> list = null;
	private DataMessage daMsg;
	private Context context;
	
	public DailyAdapater(Context context,List<DataMessage> list) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
	}
	
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		convertView = inflater.inflate(R.layout.item, parent, false);
		if(viewHolder==null){
			viewHolder = new ViewHolder();
			viewHolder.del = (ImageView) convertView.findViewById(R.id.del_iv);
			viewHolder.title = (TextView) convertView.findViewById(R.id.item_title);
			viewHolder.date = (TextView) convertView.findViewById(R.id.item_date);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		daMsg = list.get(position);
		viewHolder.title.setText(daMsg.getTitle());
		viewHolder.date.setText(daMsg.getDate());
		viewHolder.del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
						.setTitleText("确定删除该篇日记吗")
						.setContentText("将无法恢复此文件！")
						.setConfirmText("是的，删除！")
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								try {
									sDialog
											.setTitleText("删除成功！")
											.setContentText("")
											.setConfirmText("OK")
											.setConfirmClickListener(null)
											.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
									Utils.getDbManager().deleteById(DataMessage.class, daMsg.getId());
									list.remove(position);
									MainActivity.adapter.notifyDataSetChanged();
								} catch (DbException e) {
									e.printStackTrace();
									Utils.toast(context, "删除日记失败");

								}
							}
						})
						.show();
			}
		});
		return convertView;
	}
	
	private final class ViewHolder{
		ImageView del;
		TextView title;
		TextView date;
	}

}
