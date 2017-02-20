package org.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 结果集工具类
 * @author marshall
 *
 */
public class ResultUtils {
	/**
	 * 返回Map结果数组
	 * @param state 1：暂时没有数据
	 * @return
	 */
	public static Map<String, String> toResult(int state) {
		Map<String, String> message = new HashMap<String, String>();
		switch (state) {
		case 1:
			message.put("message", "error");
			message.put("description", "暂时没有数据!");
			break;
		case 2:
			break;
		default:
			break;
		}
		return message;
	}
}
