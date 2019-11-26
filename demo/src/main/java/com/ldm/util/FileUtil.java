package com.ldm.util;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;

/**
 * �ļ�����������
 * 
 * @description��
 * @author ldm
 * @date Created by generallizhong on 2019/11/26.
 */
public final class FileUtil {
	/**
	 * ���SD���Ƿ����
	 */
	public static boolean isExistSDcard() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * ��ָ���ļ��л�ȡ�ļ�
	 * 
	 * @description��
	 * @author ldm
	 * @date 2016-4-18 ����11:42:24
	 */
	public static File getAppointFile(String folderPath, String fileNmae) {
		File file = new File(getSaveFolder(folderPath).getAbsolutePath()
				+ File.separator + fileNmae);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * ��ȡ�ļ��ж���
	 * 
	 * @return ����SD���µ�ָ���ļ��ж������ļ��в������򴴽�
	 */
	public static File getSaveFolder(String folderName) {
		File file = new File(Environment.getExternalStorageDirectory()
				.getAbsoluteFile()
				+ File.separator
				+ folderName
				+ File.separator);
		file.mkdirs();
		return file;
	}

	/**
	 * �ļ����رղ���
	 * 
	 * @description��
	 * @author ldm
	 * @date 2016-4-18 ����11:28:07
	 */
	public static void close(Closeable... closeables) {
		if (null == closeables || closeables.length <= 0) {
			return;
		}
		for (Closeable cb : closeables) {
			try {
				if (null == cb) {
					continue;
				}
				cb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * �ϴ��ļ��������� (ԭ����ַ��http://blog.csdn.net/lk_blog/article/details/7706348)
	 * 
	 * @description��
	 * @author ldm
	 * @date 2016-4-18 ����1:38:33
	 */
	public static String sendFile(String serverUrl, String filePath,
			String newName) throws Exception {
		String end = "\r\n";
		String hyphens = "--";
		String boundary = "*****";

		URL url = new URL(serverUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		/* ����Input��Output����ʹ��Cache */
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		/* ���ô��͵�method=POST */
		con.setRequestMethod("POST");
		/* setRequestProperty */

		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="
				+ boundary);
		/* ����DataOutputStream */
		DataOutputStream ds = new DataOutputStream(con.getOutputStream());
		ds.writeBytes(hyphens + boundary + end);
		ds.writeBytes("Content-Disposition: form-data; "
				+ "name=\"file1\";filename=\"" + newName + "\"" + end);
		ds.writeBytes(end);

		/* ȡ���ļ���FileInputStream */
		FileInputStream fStream = new FileInputStream(filePath);
		/* ����ÿ��д��1024bytes */
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		int length = -1;
		/* ���ļ���ȡ������������ */
		while ((length = fStream.read(buffer)) != -1) {
			/* ������д��DataOutputStream�� */
			ds.write(buffer, 0, length);
		}
		ds.writeBytes(end);
		ds.writeBytes(hyphens + boundary + hyphens + end);

		/* close streams */
		fStream.close();
		ds.flush();

		/* ȡ��Response���� */
		InputStream is = con.getInputStream();
		int ch;
		StringBuffer b = new StringBuffer();
		while ((ch = is.read()) != -1) {
			b.append((char) ch);
		}
		/* �ر�DataOutputStream */
		ds.close();
		return b.toString();
	}
}