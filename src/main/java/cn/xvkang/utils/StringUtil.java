package cn.xvkang.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	public static boolean isNumber(String number) {
		int index = number.indexOf(".");
		if (index < 0) {
			return StringUtils.isNumeric(number);
		} else {
			String num1 = number.substring(0, index);
			String num2 = number.substring(index + 1);

			return StringUtils.isNumeric(num1) && StringUtils.isNumeric(num2);
		}
	}

}
