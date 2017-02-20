package org.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.util.ChangeTime;
import org.util.Constants;
import org.util.Utils;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.org.apache.xpath.internal.operations.Bool;

public class Demo extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	private static String fileDir = "";// 日期作为文件目录 ，只是根据提交时间生成的年月日数字
	private static String targetDir = ""; // 目标目录，只是一串随机生成的数字
	private Object result;

	public String test() {
		System.out.println("request.getLocalAddr():" + request.getLocalAddr());
		System.out.println("request.getContextPath():"
				+ request.getContextPath());
		System.out.println("request.getPathTranslated():"
				+ request.getPathTranslated());
		System.out.println("request.getPathInfo():" + request.getPathInfo());
		System.out.println("request.getPathTranslated():"
				+ request.getPathTranslated());
		System.out.println("request.getLocalPort():" + request.getLocalPort());
		System.out
				.println("request.getRemoteHost():" + request.getRemoteHost());
		System.out
				.println("request.getRequestURI():" + request.getRequestURI());
		System.out
				.println("request.getRequestURL():" + request.getRequestURL());
		System.out.println("request.getServletPath():"
				+ request.getServletPath());
		System.out.println(request.getRealPath("/"));
		System.out.println("Constants.ArticleUrl:" + Constants.articleUrl);
		return SUCCESS;
	}

	/**
	 * 发表文章
	 */
	public String publish() {
		System.out.println(request.getParameter("content"));
		String content = request.getParameter("content");
		String digest = request.getParameter("digest");
		String title = request.getParameter("title");
		String timeStamp = ChangeTime.timeStamp();
		String fileName = "";
		String usedDir = "";
		String tempDir = "";
		String realPath = request.getRealPath("/");
		Boolean isWindows = realPath.contains("\\");
		if (isWindows) {
			usedDir = realPath + Constants.WIN_UsedDir;
			tempDir = realPath + Constants.WIN_tempDir;
		} else {
			usedDir = realPath + Constants.LINUX_UsedDir;
			tempDir = realPath + Constants.LINUX_tempDir;
		}
		content = movePic(title, content, timeStamp, tempDir, usedDir,
				isWindows);

		if (fileDir.equals("")) {
			fileDir = ChangeTime.currentDate() + "";
		}
		if (isWindows) {
			fileName = realPath + Constants.WIN_UsedDir + fileDir + "\\"
					+ targetDir + "\\" + "1.html";
		} else {
			fileName = realPath + Constants.LINUX_UsedDir + fileDir + "/"
					+ targetDir + "/" + "1.html";
		}

		File file = new File(fileName);
		try {
			if (!file.getParentFile().exists()) {
				System.out.println("目标文件的目录不存在，准备创建目录...");
				if (!file.getParentFile().mkdirs()) {
					System.out.println("创建目录失败");
				}
				System.out.println(file.createNewFile());
			}
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.flush();
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/************* 上面是文件相关操作 *******************/
		/************* 下面是数据相关操作 *******************/
		String ArticleUrl = Constants.articleUrl + fileDir + "/" + targetDir
				+ "/" + "1.html";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ArticleUrl", ArticleUrl);
		result = map;
		return SUCCESS;
	}

	/**
	 * 识别图片地址，移动图片位置，修改图片url
	 * 
	 * @param content
	 *            内容
	 */
	private String movePic(String title, String content, String timeStamp,
			String tempDir, String usedDir, Boolean isWindows) {
		System.out.println("Moving picture...");
		String pattern = "/jsp/upload/temp/.*?(.jpg|.png|.gif)";
		String dir = "";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(content);
		targetDir = timeStamp + Utils.ran6();
		while (m.find()) {
			String used_file = m.group().replace("/jsp/upload/temp/", "")
					.replace("/", "\\");
			String[] a = used_file.split("\\\\");
			dir = a[0];
			fileDir = dir;
			String filename = a[1];
			String orginalFile = "";
			String targetFile = "";
			if (isWindows) {
				orginalFile = tempDir + used_file;
				targetFile = usedDir + dir + "\\" + targetDir + "\\" + filename;
			} else {
				orginalFile = tempDir + dir + "/" + filename;
				targetFile = usedDir + dir + "/" + targetDir + "/" + filename;
			}
			System.out.println("orginalFile:" + orginalFile);
			System.out.println("targetFile:" + targetFile);

			File file = new File(orginalFile);

			File file2 = new File(targetFile);
			if (!file2.getParentFile().exists()) {
				System.out.println("目标文件的目录不存在，准备创建目录...");
				if (!file2.getParentFile().mkdirs()) {
					System.out.println("创建目录失败");
				}
			}
			try {
				System.out.println(file.renameTo(file2));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		System.out.println("Moving Complete...");
		content = content.replaceAll("/jsp/upload/temp/[0-9]*/?",
				"/jsp/upload/article/" + dir + "/" + targetDir + "/");
		return content;
	}

	/**
	 * 删除文章
	 */
	public String delete() {

		return null;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
