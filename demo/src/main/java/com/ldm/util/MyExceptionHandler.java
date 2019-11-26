package com.ldm.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.widget.Toast;


/**
 * UncaughtExceptionHandler���߳�δ�����쳣����������������δ�����쳣�ġ� ʵ�ָýӿڲ�ע��Ϊ�����е�Ĭ��δ�����쳣����
 * ������δ�����쳣����ʱ���Ϳ�����Щ�쳣������� ���磺�ռ��쳣��Ϣ�����ʹ��󱨸� �ȡ�
 * 
 * @description��
 * @author ldm
 * @date Created by generallizhong on 2019/11/26.
 */
public class MyExceptionHandler implements UncaughtExceptionHandler {
	// ������
	private Context mContext;
	// �Ƿ���ϴ�
	public boolean openUpload = true;
	// Log�ļ�·��
	private static final String LOG_FILE_DIR = "log";
	// log�ļ��ĺ�׺��
	private static final String FILE_NAME = ".log";
	private static MyExceptionHandler instance = null;
	// ϵͳĬ�ϵ��쳣����Ĭ������£�ϵͳ����ֹ��ǰ���쳣����
	private UncaughtExceptionHandler mDefaultCrashHandler;

	private MyExceptionHandler(Context cxt) {
		// ��ȡϵͳĬ�ϵ��쳣������
		mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
		// ����ǰʵ����ΪϵͳĬ�ϵ��쳣������
		Thread.setDefaultUncaughtExceptionHandler(this);
		// ��ȡContext�������ڲ�ʹ��
		this.mContext = cxt.getApplicationContext();
	}

	public synchronized static MyExceptionHandler create(Context cxt) {
		if (instance == null) {
			instance = new MyExceptionHandler(cxt);
		}
		return instance;
	}

	/**
	 * ����������δ��������쳣��ϵͳ�����Զ�����#uncaughtException����
	 * threadΪ����δ�����쳣���̣߳�exΪδ������쳣���������ex�����ǾͿ��Եõ��쳣��Ϣ��
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			// ���浼���쳣��־��Ϣ��SD����
			saveToSDCard(ex);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// ���ϵͳ�ṩ��Ĭ�ϵ��쳣���������򽻸�ϵͳȥ�������ǵĳ��򣬷�����������Լ������Լ�
			Toast.makeText(mContext,
					"�ܱ�Ǹ��������������˳�:\r\n" + ex.getLocalizedMessage(),
					Toast.LENGTH_LONG).show();
			if (mDefaultCrashHandler != null) {
				mDefaultCrashHandler.uncaughtException(thread, ex);
			} else {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * �����ļ���SD��
	 * 
	 * @description��
	 * @author ldm
	 * @date 2016-4-18 ����11:37:17
	 */
	private void saveToSDCard(Throwable ex) throws Exception {
		File file = FileUtil.getAppointFile(mContext.getPackageName()
				+ File.separator + LOG_FILE_DIR,
				getDataTime("yyyy-MM-dd-HH-mm-ss") + FILE_NAME);
		PrintWriter pw = new PrintWriter(new BufferedWriter(
				new FileWriter(file)));
		// ���������쳣��ʱ��
		pw.println(getDataTime("yyyy-MM-dd-HH-mm-ss"));
		// �����ֻ���Ϣ
		savePhoneInfo(pw);
		pw.println();
		// �����쳣�ĵ���ջ��Ϣ
		ex.printStackTrace(pw);
		pw.close();
	}

	/**
	 * �����ֻ�Ӳ����Ϣ
	 * 
	 * @description��
	 * @author ldm
	 * @date 2016-4-18 ����11:38:01
	 */
	private void savePhoneInfo(PrintWriter pw) throws NameNotFoundException {
		// Ӧ�õİ汾���ƺͰ汾��
		PackageManager pm = mContext.getPackageManager();
		PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
				PackageManager.GET_ACTIVITIES);
		pw.print("App Version: ");
		pw.print(pi.versionName);
		pw.print('_');
		pw.println(pi.versionCode);
		pw.println();

		// android�汾��
		pw.print("OS Version: ");
		pw.print(Build.VERSION.RELEASE);
		pw.print("_");
		pw.println(Build.VERSION.SDK_INT);
		pw.println();

		// �ֻ�������
		pw.print("Manufacturer: ");
		pw.println(Build.MANUFACTURER);
		pw.println();

		// �ֻ��ͺ�
		pw.print("Model: ");
		pw.println(Build.MODEL);
		pw.println();
	}

	/**
	 * ����ʱ���ʽ����ʱ��
	 * 
	 * @description��
	 * @author ldm
	 * @date 2016-4-18 ����11:39:30
	 */
	private String getDataTime(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(new Date());
	}
}