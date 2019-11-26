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
 * 文件操作工具类
 * 
 * @description：
 * @author ldm
 * @date Created by generallizhong on 2019/11/26.
 */
public final class FileUtil {
	/**
	 * 检测SD卡是否存在
	 */
	public static boolean isExistSDcard() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * 从指定文件夹获取文件
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-4-18 上午11:42:24
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
	 * 获取文件夹对象
	 * 
	 * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创建
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
	 * 文件流关闭操作
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-4-18 上午11:28:07
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
	 * 上传文件到服务器 (原帖地址：http://blog.csdn.net/lk_blog/article/details/7706348)
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-4-18 下午1:38:33
	 */
	public static String sendFile(String serverUrl, String filePath,
			String newName) throws Exception {
		String end = "\r\n";
		String hyphens = "--";
		String boundary = "*****";

		URL url = new URL(serverUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		/* 允许Input、Output，不使用Cache */
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		/* 设置传送的method=POST */
		con.setRequestMethod("POST");
		/* setRequestProperty */

		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="
				+ boundary);
		/* 设置DataOutputStream */
		DataOutputStream ds = new DataOutputStream(con.getOutputStream());
		ds.writeBytes(hyphens + boundary + end);
		ds.writeBytes("Content-Disposition: form-data; "
				+ "name=\"file1\";filename=\"" + newName + "\"" + end);
		ds.writeBytes(end);

		/* 取得文件的FileInputStream */
		FileInputStream fStream = new FileInputStream(filePath);
		/* 设置每次写入1024bytes */
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		int length = -1;
		/* 从文件读取数据至缓冲区 */
		while ((length = fStream.read(buffer)) != -1) {
			/* 将资料写入DataOutputStream中 */
			ds.write(buffer, 0, length);
		}
		ds.writeBytes(end);
		ds.writeBytes(hyphens + boundary + hyphens + end);

		/* close streams */
		fStream.close();
		ds.flush();

		/* 取得Response内容 */
		InputStream is = con.getInputStream();
		int ch;
		StringBuffer b = new StringBuffer();
		while ((ch = is.read()) != -1) {
			b.append((char) ch);
		}
		/* 关闭DataOutputStream */
		ds.close();
		return b.toString();
	}
}