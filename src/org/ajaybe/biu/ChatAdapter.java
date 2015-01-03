package org.ajaybe.biu;

import java.util.List;
import org.ajaybe.biu.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;// �յ��Է�����Ϣ
		int IMVT_TO_MSG = 1;// �Լ����ͳ�ȥ����Ϣ
	}

	private static final int ITEMCOUNT = 2;// ��Ϣ���͵�����
	private List<ChatEntity> coll;// ��Ϣ��������
	private LayoutInflater mInflater;

	public ChatAdapter(Context context, List<ChatEntity> coll) {
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * �õ�Item�����ͣ��ǶԷ�����������Ϣ�������Լ����ͳ�ȥ��
	 */
	public int getItemViewType(int position) {
		ChatEntity entity = coll.get(position);

		if (entity.getMsgType()) {// �յ�����Ϣ
			return IMsgViewType.IMVT_COM_MSG;
		} else {// �Լ����͵���Ϣ
			return IMsgViewType.IMVT_TO_MSG;
		}
	}

	/**
	 * Item���͵�����
	 */
	public int getViewTypeCount() {
		return ITEMCOUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ChatEntity entity = coll.get(position);
		boolean isComMsg = entity.getMsgType();

		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			if (isComMsg) {
				convertView = mInflater.inflate(
						R.layout.chat_item_left, null);
			} else {
				convertView = mInflater.inflate(
						R.layout.chat_item_right, null);
			}

			viewHolder = new ViewHolder();
			viewHolder.tvSendTime = (TextView) convertView
					.findViewById(R.id.tv_sendtime);
			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tv_username);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_chatcontent);
			viewHolder.isComMsg = isComMsg;

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tvSendTime.setText(entity.getDate());
		viewHolder.tvUserName.setText(entity.getName());
		if (entity.getSave()) {
			viewHolder.tvContent.setText("");
		} else
			viewHolder.tvContent.setText(entity.getMessage());
		
		return convertView;
	}

	static class ViewHolder {
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvContent;
		public boolean isComMsg = true;
	}

}
