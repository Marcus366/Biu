package org.ajaybe.biu;

public class ChatEntity {
	private String name;// ��Ϣ����
	private String date;// ��Ϣ����
	private String message;// ��Ϣ����
	private boolean isComMeg = true;// �Ƿ�Ϊ�յ�����Ϣ

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getMsgType() {
		return isComMeg;
	}

	public void setMsgType(boolean isComMsg) {
		isComMeg = isComMsg;
	}

	public ChatEntity() {
	}

	public ChatEntity(String name, String date, String text, boolean isComMsg) {
		this.name = name;
		this.date = date;
		this.message = text;
		this.isComMeg = isComMsg;
	}
}
