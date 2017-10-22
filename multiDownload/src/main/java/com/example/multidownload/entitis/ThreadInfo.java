package com.example.multidownload.entitis;

/**
 * 
 * �Q����Ϣ����b�Q�̵�ID���Q�̵�url���Q���_ʼλ�ã��Y��λ�ã��Լ��ѽ���ɵ�λ��
 *
 */
public class ThreadInfo {
	private int id;
	private String url;
	private int start;
	private int end;
	private int finished;

	public ThreadInfo() {
		super();
	}

	/**
	 * 
	 * @param id
	 *            �Q�̵�ID
	 * @param url
	 *            ���d�ļ��ľW�j��ַ
	 * @param start
	 *            �Q�����d���_ʼλ��
	 * @param end
	 *            �Q�����d�ĽY��λ��
	 * @param finished
	 *            �Q���ѽ����d���Ă�λ��
	 */
	public ThreadInfo(int id, String url, int start, int end, int finished) {
		super();
		this.id = id;
		this.url = url;
		this.start = start;
		this.end = end;
		this.finished = finished;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	@Override
	public String toString() {
		return "ThreadInfo [id=" + id + ", url=" + url + ", start=" + start + ", end=" + end + ", finished=" + finished
				+ "]";
	}

}
