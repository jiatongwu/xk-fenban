package cn.xvkang.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class NumberUtils {
	// 科目A级别 是20％
	public static int kemuALevel = 20;
	public static int kemuBLevel = 30;
	public static int kemuCLevel = 30;
	public static int kemuDLevel = 10;
	public static int kemuELevel = 10;

	// # 总分成绩各级别各占多少 A1 A2 A3 B 代表的是前多少人 C D 代表的是百分之多少 E代表D后面再数多少人就是E级别
	public static int zongFenLevelA1 = 30;
	public static int zongFenLevelA2 = 100;
	public static int zongFenLevelA3 = 150;
	public static int zongFenLevelB = 350;
	public static int zongFenLevelC = 30;
	public static int zongFenLevelD = 10;
	public static int zongFenLevelE = 50;

	static {
		Properties prop = new Properties();
		try {
			prop.load(NumberUtils.class.getClassLoader().getResourceAsStream("config/config.properties"));
			int dankeLevelAPercent = Integer.parseInt((String) prop.get("dankeLevelAPercent"));
			int dankeLevelBPercent = Integer.parseInt((String) prop.get("dankeLevelBPercent"));
			int dankeLevelCPercent = Integer.parseInt((String) prop.get("dankeLevelCPercent"));
			int dankeLevelDPercent = Integer.parseInt((String) prop.get("dankeLevelDPercent"));
			int dankeLevelEPercent = Integer.parseInt((String) prop.get("dankeLevelEPercent"));
			int zongFenLevelA1Para = Integer.parseInt((String) prop.get("zongFenLevelA1"));
			int zongFenLevelA2Para = Integer.parseInt((String) prop.get("zongFenLevelA2"));
			int zongFenLevelA3Para = Integer.parseInt((String) prop.get("zongFenLevelA3"));
			int zongFenLevelBPara = Integer.parseInt((String) prop.get("zongFenLevelB"));
			int zongFenLevelCPara = Integer.parseInt((String) prop.get("zongFenLevelC"));
			int zongFenLevelDPara = Integer.parseInt((String) prop.get("zongFenLevelD"));
			int zongFenLevelEPara = Integer.parseInt((String) prop.get("zongFenLevelE"));

			kemuALevel = dankeLevelAPercent;
			kemuBLevel = dankeLevelBPercent;
			kemuCLevel = dankeLevelCPercent;
			kemuDLevel = dankeLevelDPercent;
			kemuELevel = dankeLevelEPercent;

			zongFenLevelA1 = zongFenLevelA1Para;
			zongFenLevelA2 = zongFenLevelA2Para;
			zongFenLevelA3 = zongFenLevelA3Para;
			zongFenLevelB = zongFenLevelBPara;
			zongFenLevelC = zongFenLevelCPara;
			zongFenLevelD = zongFenLevelDPara;
			zongFenLevelE = zongFenLevelEPara;

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 将一个正整数 分成 20％ 30％ 30％ 10％ 10％ 返回 各个级别中有多少个人 此处的 20％ 30％ 30％ 10％ 10％ 后期要做成动态的
	 */
	public static List<Integer> splitNumber(int n) {
		if (n >= 5) {
			List<Integer> result = new ArrayList<>();
			double oneLevelDouble = (kemuALevel * 0.01) * n;
			double twoLevelDouble = (kemuBLevel * 0.01) * n;
			double threeLevelDouble = (kemuCLevel * 0.01) * n;
			double fourLevelDouble = (kemuDLevel * 0.01) * n;
			double fiveLevelDouble = (kemuELevel * 0.01) * n;

			int oneLevelCount = new Double(Math.floor(oneLevelDouble)).intValue();
			if(oneLevelCount==0) {
				oneLevelCount=1;
			}
			int twoLevelCount = new Double(Math.floor(twoLevelDouble)).intValue();
			int threelevelCount = new Double(Math.floor(threeLevelDouble)).intValue();
			int fourLevelCount = 0;
			int fiveLevelCount = 0;

			fourLevelCount = new Double(Math.floor(fourLevelDouble)).intValue();
			fiveLevelCount=new Double(Math.floor(fiveLevelDouble)).intValue();
			

			result.add(oneLevelCount);
			result.add(twoLevelCount);
			result.add(threelevelCount);
			result.add(fourLevelCount);
			result.add(fiveLevelCount);
			return result;
		} else if (n == 4) {
			return Arrays.asList(1, 1, 1, 1);
		} else if (n == 3) {
			return Arrays.asList(1, 1, 1);
		} else if (n == 2) {
			return Arrays.asList(1, 1);
		} else if (n == 1) {
			return Arrays.asList(1);
		}
		return new ArrayList<Integer>();

	}

	/**
	 * 返回一个数的百分之多少
	 * 
	 * @param n
	 * @param percent
	 * @return 例如：percent(20,25) 返回20的百分之25
	 */
	public static int percent(int n, int percent) {
		int intValue = new Double(Math.floor(n * (0.01 * percent))).intValue();
		return intValue;
	}

	public static void main(String[] args) {
		int percent = percent(10, 25);
		System.out.println(percent);
		List<Integer> splitNumber = splitNumber(42);
		for (int i = 0; i < splitNumber.size(); i++) {
			System.out.println(splitNumber.get(i));
		}

	}

}
