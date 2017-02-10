package note.zhoumq.cn.qnote;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.ex.DbException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
{

	private List<DataMessage> mDatas;
	private LayoutInflater mInflater;
	private Context context;

	public interface OnItemClickLitener
	{
		void onItemClick(View view, int position);
		void onItemLongClick(View view, int position);
	}

	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
	{
		this.mOnItemClickLitener = mOnItemClickLitener;
	}
	

	public HomeAdapter(Context context, List<DataMessage> datas)
	{
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
		this.context = context;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		MyViewHolder holder = new MyViewHolder(mInflater.inflate(
				R.layout.item_simple_list_1, parent, false));
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position)
	{
		holder.tv_title.setText(mDatas.get(position).getTitle());
		holder.tv_content.setText(mDatas.get(position).getContent());
		int number = new Random().nextInt(App.img_avatar.length);
		holder.img_avatar.setBackgroundResource(App.img_avatar[number]);
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(mDatas.get(position).getDate());
			holder.tv_time.setText(FuzzyDateTimeFormatter.getTimeAgo(context,date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		holder.img_del.setOnClickListener(new OnClickListener() {

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
									Utils.getDbManager().deleteById(DataMessage.class, mDatas.get(position).getId());
									mDatas.remove(position);
									MainActivity.home_adapter.notifyDataSetChanged();
								} catch (DbException e) {
									e.printStackTrace();
									Utils.toast(context, "删除日记失败");
								}
							}
						})
						.show();
			}
		});
		// 如果设置了回调，则设置点击事件
		if (mOnItemClickLitener != null)
		{
			holder.itemView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					int pos = holder.getLayoutPosition();
					mOnItemClickLitener.onItemClick(holder.itemView, pos);
				}
			});
			
			holder.itemView.setOnLongClickListener(new OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View v)
				{
					int pos = holder.getLayoutPosition();
					mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
					return false;
				}
			});
		}
	}

	@Override
	public int getItemCount()
	{
		return mDatas.size();
	}

	public void addData(int position,DataMessage msg)
	{
		mDatas.add(position, msg);
		notifyItemInserted(position);
	}


	public void removeData(int position)
	{
		mDatas.remove(position);
		notifyItemRemoved(position);
	}

	class MyViewHolder extends ViewHolder
	{

		TextView tv_title,tv_content,tv_time;
		ImageView img_del,img_avatar;

		public MyViewHolder(View view)
		{
			super(view);
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_content = (TextView) view.findViewById(R.id.tv_content);
			tv_time = (TextView) view.findViewById(R.id.tv_time);
			img_del = (ImageView) view.findViewById(R.id.img_del);
			img_avatar = (ImageView) view.findViewById(R.id.civ_avatar);
		}
	}
}