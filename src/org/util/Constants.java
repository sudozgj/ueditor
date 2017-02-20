package org.util;

import org.apache.struts2.ServletActionContext;

public class Constants {
	/**
	 * WINDOWS服务器--存放临时图片目录
	 */
	public static String WIN_tempDir = "ueditor\\jsp\\upload\\temp\\";
	/**
	 * LINUX服务器--存放临时图片目录
	 */
	public static String LINUX_tempDir = "ueditor/jsp/upload/temp/";

	/**
	 * WINDOWS服务器--存放上传文章及图片目录
	 */
	public static String WIN_UsedDir = "ueditor\\jsp\\upload\\article\\";
	/**
	 * LINUX服务器--存放上传文章及图片目录
	 */
	public static String LINUX_UsedDir = "ueditor/jsp/upload/article/";

	public static String articleUrl = ServletActionContext.getRequest()
			.getLocalAddr()
			+ ":"
			+ ServletActionContext.getRequest().getLocalPort()
			+ ServletActionContext.getRequest().getContextPath()
			+ "/"
			+ "ueditor/jsp/upload/article/";
}
