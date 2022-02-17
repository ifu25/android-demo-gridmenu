package cn.com.lttc.myapplication.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.com.lttc.myapplication.AppConfig;
import cn.com.lttc.myapplication.AppContext;
import cn.com.lttc.myapplication.R;
import cn.com.lttc.myapplication.drag.DragAdapterInterface;
import cn.com.lttc.myapplication.entity.MenuEntity;
import cn.com.lttc.myapplication.ui.MenuManageActivity;

/**
 * 仿支付菜单演示（支付拖拽排序）
 */
public class MyAdapter extends BaseAdapter implements DragAdapterInterface {
	private boolean IsEdit = false;
	private List<MenuEntity> datas = new ArrayList<MenuEntity>();
	private MenuManageActivity context;
	private AppContext appContext;

	public MyAdapter(MenuManageActivity context, AppContext appContext, List<MenuEntity> datas) {
		this.context = context;
		this.appContext = appContext;
		this.datas = datas;
	}

	public void setDatas(List<MenuEntity> datas) {
		this.datas.clear();
		this.datas.addAll(datas);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		MenuEntity bean = datas.get(position);
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = LayoutInflater.from(context).inflate(R.layout.view_item, null);
			holder.deleteImg = (ImageView) convertView.findViewById(R.id.delete_img);
			holder.iconImg = (ImageView) convertView.findViewById(R.id.icon_img);
			holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
			holder.container = convertView.findViewById(R.id.item_container);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.deleteImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				context.DelMeun(datas.get(position),position);
				datas.remove(position);
				String key = AppConfig.KEY_USER_TEMP;
				appContext.saveObject((Serializable) datas, key);
			}
		});
		if (IsEdit) {
			holder.deleteImg.setVisibility(View.VISIBLE);
		} else {
			holder.deleteImg.setVisibility(View.GONE);
		}
        //获取资源图片
        int drawableId = context.getResources().getIdentifier(bean.getIco(),"mipmap", context.getPackageName());
        holder.iconImg.setImageResource(drawableId);

//		Glide.with(context).load(bean.getIco()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
//				.into(holder.iconImg);

		holder.nameTv.setText(bean.getTitle());
		holder.container.setBackgroundColor(Color.WHITE);
		return convertView;
	}

	class Holder {
		public ImageView deleteImg;
		public ImageView iconImg;
		public TextView nameTv;
		public View container;
	}

	@Override
	public void reOrder(int startPosition, int endPosition) {
		if (endPosition < datas.size()) {
			MenuEntity object = datas.remove(startPosition);
			datas.add(endPosition, object);
			String key = AppConfig.KEY_USER_TEMP;
			appContext.saveObject((Serializable) datas, key);
			notifyDataSetChanged();
		}
	}

	public void setEdit() {
		IsEdit = true;
		notifyDataSetChanged();
	}

	public void getDatas() {
		for (MenuEntity data : datas) {
			// DebugLog.i("点击 Item " + data.getId());
		}
	}

	public void endEdit() {
		IsEdit = false;
		notifyDataSetChanged();
	}
	public boolean getEditStatue() {
		return IsEdit;
	}


}
