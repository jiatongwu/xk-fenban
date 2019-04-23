package cn.xvkang.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 生产指定长度随机字符串a-z,A-Z,0-9
 * 
 * @author happyqing
 * @since 2016.5.30
 */
public class RandomStringUtil {

	/**
	 * 获取随机字符串 a-z,A-Z,0-9
	 * 
	 * @param length 长度
	 * @return
	 */
	public static String getRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; ++i) {
			// int number = random.nextInt(62);// [0,62)
			sb.append(str.charAt(random.nextInt(62)));
		}
		return sb.toString();
	}

	/**
	 * JAVA获得0-9,a-z,A-Z范围的随机数
	 * 
	 * @param length 随机数长度
	 * @return String
	 */
	public static String getRandomChar(int length) {
		char[] chr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
				'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
				'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
				'Z' };
		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buffer.append(chr[random.nextInt(62)]);
		}
		return buffer.toString();
	}

	/**
	 * 获取随机字符串 a-z
	 * 
	 * @param length 长度
	 * @return
	 */
	public static String getLowerLetter(int length) {
		String str = "abcdefghijklmnopqrstuvwxyz";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; ++i) {
			sb.append(str.charAt(random.nextInt(26)));
		}
		return sb.toString();
	}

	/**
	 * 传一个字符串 把顺序打乱 随机的顺序
	 */
	public static String getRandomSequence(String str) {
		String result = "";
		if (str == null) {
			return null;
		}
		// 递归终止条件
		if (str.equals("")) {
			return "";
		}

		int length = str.length();
		Random random = new Random();
		int nextInt = random.nextInt(length);
		char charAt = str.charAt(nextInt);

		String nextLoopStr = null;
		if (nextInt == 0) {
			nextLoopStr = str.substring(1, str.length());
		} else if (nextInt == str.length() - 1) {
			nextLoopStr = str.substring(0, str.length() - 1);
		} else {
			nextLoopStr = str.substring(0, nextInt) + str.substring(nextInt + 1, str.length());
		}

		result = result + new Character(charAt).toString();
		result = result + getRandomSequence(nextLoopStr);
		return result;
	}

	/**
	 * 获取随机字符串 a-z
	 * 
	 * @param length 长度
	 * @return
	 */
	public static String getUpperLetter(int length) {
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; ++i) {
			sb.append(str.charAt(random.nextInt(26)));
		}
		return sb.toString();
	}

	/**
	 * 获取随机字符串 0-9
	 * 
	 * @param length 长度
	 * @return
	 */
	public static String getNumber(int length) {
		String str = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; ++i) {
			sb.append(str.charAt(random.nextInt(10)));
		}
		return sb.toString();
	}

	/**
	 * 获取随机字符串 0-9,a-z,0-9 有两遍0-9，增加数字概率
	 * 
	 * @param length 长度
	 * @return
	 */
	public static String getLowerLetterNumber(int length) {
		String str = "0123456789abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; ++i) {
			sb.append(str.charAt(random.nextInt(46)));
		}
		return sb.toString();
	}

	/**
	 * 获取随机密码，lLength位小写英文+nLength位数字
	 * 
	 * @param lLength 字母长度
	 * @param nLength 数字长度
	 * @return
	 */
	public static String getPasswordSimple(int lLength, int nLength) {
		return getLowerLetter(lLength) + getNumber(nLength);
	}

	/**
	 * 获取随机密码，包含英文和数字
	 * 
	 * @param length 密码长度
	 * @return
	 */
	public static String getPasswordComplex(int length) {
		String password;
		while (true) {
			password = getLowerLetterNumber(6);
			// 必须包含字母和数字
			if (password.matches("(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}")) {
				return password;
			}
		}
	}

	/**
	 * 将一个数字分成随机的三份 8 分成 2＋1＋5 也可以分成 1＋3＋4 .....
	 * 
	 * 暂定为num必须大于8
	 */
	public static int[] splitToNPlus(int num, int fenershu) {
		int[] result = new int[fenershu];
		for (int i = 0; i < fenershu; i++) {
			result[i] = 0;
		}
		if (num < fenershu) {
			return result;
		}
		if (num < 8) {
			return result;
		}

		Random rand = new Random();
		int sum = 0; // 已生成的随机数总和
		for (int i = 0; i < fenershu - 1; i++) {
			int temp = num - sum;
			int random = rand.nextInt(temp);
			result[i] = random;
			sum += random;
		}
		result[fenershu - 1] = num - sum;

		return result;
	}

	/**
	 * 随机指定范围内N个不重复的数 最简单最基本的方法
	 * 
	 * @param min 指定范围最小值
	 * @param max 指定范围最大值
	 * @param n   随机数个数
	 */
	public static int[] randomCommon(int min, int max, int n) {
		if (n > (max - min + 1) || max < min) {
			return null;
		}
		int[] result = new int[n];
		int count = 0;
		while (count < n) {
			int num = (int) (Math.random() * (max - min)) + min;
			boolean flag = true;
			for (int j = 0; j < n; j++) {
				if (num == result[j]) {
					flag = false;
					break;
				}
			}
			if (flag) {
				result[count] = num;
				count++;
			}
		}
		return result;
	}

	/**
	 * 将一个正整数L随机拆分成n个正整数 每个正整数都大于0
	 * 
	 * @param L 正整数
	 * @param n 个数
	 */
	public static List<Integer> random(int L, int n) {
		List<Integer> result = new ArrayList<>();
		
		// 递归终止条件
		if (n == 1) {
			result.add(L);
			return result;
		}
		int random=0;
		if(L==2&&n==2) {
			random=1;
			
		}else {
			 random= getRandomLength(1, L - n + 1);
		}
		if(random==0) {
			System.out.println("error");
		}
		result.add(random);
		result.addAll(random(L - random, n - 1));
		return result;
	}

	/**
	 * 获取一个8到12位数字之间的随机数 包括8 或12
	 */
	public static int getRandomLength(int start, int end) {
		int i = 0;
		if (start == 0 || end == 0 || end <= start || start < 0) {
			return 0;
		}
		int cha = end - start;
		Random random = new Random();
		int nextInt = random.nextInt(cha + 1);
		i = nextInt + start;
		if(i==0) {
			System.out.println("error");
		}
		return i;
	}

	/**
	 * 测试方法 生成8－12位随机的密码 必须包含大写字母 小写字母 数字
	 */
	public static String get8To12Password() {
		// 获取密码长度
		int randomLength = getRandomLength(8, 12);
		// 将长度为n的密码分成3份 每一份分别存放 大写字母 小写字母 数字
		List<Integer> random = random(randomLength, 3);
		// 生成n个随机的大写字母
		String upper = getUpperLetter(random.get(0));
		// 生成n个随机的小写字母
		String lower = getLowerLetter(random.get(1));
		// 生成n个随机的数字
		String number = getNumber(random.get(2));
		String union = upper + lower + number;
		String randomSequence = getRandomSequence(union);
		String reg="[A-Za-z0-9]*[a-z]+[A-Za-z0-9]*";
		String reg2="[A-Za-z0-9]*[A-Z]+[A-Za-z0-9]*";
		String reg3="[A-Za-z0-9]*[0-9]+[A-Za-z0-9]*";
		if(!randomSequence.matches(reg)||!randomSequence.matches(reg2)||!randomSequence.matches(reg3)) {
			System.out.println("error");
		}
		return randomSequence;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 30000; i++) {
			System.out.println(get8To12Password());
		}
		String reg="[A-Za-z0-9]*[a-z]+[A-Za-z0-9]*";
		System.out.println("8AAA8A88B8A".matches(reg));
		System.out.println("2D68GDtLEz".matches(reg));
		
		
	}
}