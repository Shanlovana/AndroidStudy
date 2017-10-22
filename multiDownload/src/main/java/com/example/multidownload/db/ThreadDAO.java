package com.example.multidownload.db;

import java.util.List;

import com.example.multidownload.entitis.ThreadInfo;

/**
 * ����������Ľӿ��
 *
 */
public interface ThreadDAO {
	// ����Q��
	public void insertThread(ThreadInfo info);
	// �h���Q��
	public void deleteThread(String url);
	// ���¾Q��
	public void updateThread(String url, int thread_id, int finished);
	// ��ԃ�Q��
	public List<ThreadInfo> queryThreads(String url);
	// �Д�Q���Ƿ����
	public boolean isExists(String url, int threadId);
}
