package cn.xvkang.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class IdcardValidatorUtils {
	/**
	 * 返回 map map中的msg==ok 代表身份证格式正确 可以拿到 省份 生日 性别 信息
	 * 
	 * @return
	 */
	public static Map<String, Object> checkIdcard(String idcard) {
		boolean isOk = true;
		String msg = null;
		@SuppressWarnings("serial")
		Map<Integer, String> city = new HashMap<Integer, String>() {
			{
				put(11, "北京");
				put(12, "天津");
				put(13, "河北");
				put(14, "山西");
				put(15, "内蒙古");
				put(21, "辽宁");
				put(22, "吉林");
				put(23, "黑龙江 ");
				put(31, "上海");
				put(32, "江苏");
				put(33, "浙江");
				put(34, "安徽");
				put(35, "福建");
				put(36, "江西");
				put(37, "山东");
				put(41, "河南");
				put(42, "湖北 ");
				put(43, "湖南");
				put(44, "广东");
				put(45, "广西");
				put(46, "海南");
				put(50, "重庆");
				put(51, "四川");
				put(52, "贵州");
				put(53, "云南");
				put(54, "西藏 ");
				put(61, "陕西");
				put(62, "甘肃");
				put(63, "青海");
				put(64, "宁夏");
				put(65, "新疆");
				put(71, "台湾");
				put(81, "香港");
				put(82, "澳门");
				put(91, "国外");

			}
		};
		String birthday = "";
		try {
			birthday = idcard.substring(6, 10) + "/" + idcard.substring(10, 12) + "/" + idcard.substring(12, 14);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Date d = sdf.parse(birthday);

			long currentTime = new Date().getTime();
			long time = d.getTime();
			int[] arrInt = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
			String[] arrCh = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
			int sum = 0, i = 0;
			String residue = "";
			String reg01 = "^\\d{17}(\\d|x|X)$";
			if (!idcard.matches(reg01)) {
				isOk = false;
				msg = "非法身份证";
			}

			String reg02 = "^\\d{17}(\\d|x|X)$";
			if (!idcard.matches(reg02)) {
				isOk = false;
				msg = "非法身份证";
			}

			if (!city.containsKey(Integer.parseInt(idcard.substring(0, 2)))) {
				isOk = false;
				msg = "非法地区";
			}

			if (time >= currentTime) {
				isOk = false;
				msg = "非法生日";
			}

			for (i = 0; i < 17; i++) {
				sum += Integer.parseInt(idcard.substring(i, i + 1)) * arrInt[i];
			}
			residue = arrCh[sum % 11];
			if (!residue.equalsIgnoreCase(idcard.substring(17, 18))) {
				isOk = false;
				msg = "非法身份证哦";
			}

		} catch (Exception e) {
			isOk = false;
			e.printStackTrace();
			msg = "身份证格式不正确";
		}

		// return
		// city[idcard.substring(0,2)]+","+birthday+","+(idcard.substring(16,1)%2?" //
		// 男":"女")
		Map<String, Object> result = new HashMap<>();

		if (isOk) {
			msg = "ok";
			result.put("province", city.get(Integer.parseInt(idcard.substring(0, 2))));
			result.put("birthday", birthday.replaceAll("\\/", ""));
			result.put("gender", Integer.parseInt(idcard.substring(16, 17)) % 2 != 0 ? "1" : "0");
		}

		result.put("msg", msg);
		return result;

	}

	public static boolean isIdcardFormat(String idcard) {

		try {
			boolean isOk = true;
			@SuppressWarnings("unused")
			String msg = null;
			@SuppressWarnings("serial")
			Map<Integer, String> city = new HashMap<Integer, String>() {
				{
					put(11, "北京");
					put(12, "天津");
					put(13, "河北");
					put(14, "山西");
					put(15, "内蒙古");
					put(21, "辽宁");
					put(22, "吉林");
					put(23, "黑龙江 ");
					put(31, "上海");
					put(32, "江苏");
					put(33, "浙江");
					put(34, "安徽");
					put(35, "福建");
					put(36, "江西");
					put(37, "山东");
					put(41, "河南");
					put(42, "湖北 ");
					put(43, "湖南");
					put(44, "广东");
					put(45, "广西");
					put(46, "海南");
					put(50, "重庆");
					put(51, "四川");
					put(52, "贵州");
					put(53, "云南");
					put(54, "西藏 ");
					put(61, "陕西");
					put(62, "甘肃");
					put(63, "青海");
					put(64, "宁夏");
					put(65, "新疆");
					put(71, "台湾");
					put(81, "香港");
					put(82, "澳门");
					put(91, "国外");

				}
			};
			String birthday = "";

			birthday = idcard.substring(6, 10) + "/" + idcard.substring(10, 12) + "/" + idcard.substring(12, 14);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Date d = sdf.parse(birthday);

			long currentTime = new Date().getTime();
			long time = d.getTime();
			int[] arrInt = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
			String[] arrCh = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
			int sum = 0, i = 0;
			String residue = "";
			String reg01 = "^\\d{17}(\\d|x|X)$";
			if (!idcard.matches(reg01)) {
				isOk = false;
				msg = "非法身份证";
			}

			String reg02 = "^\\d{17}(\\d|x|X)$";
			if (!idcard.matches(reg02)) {
				isOk = false;
				msg = "非法身份证";
			}

			if (!city.containsKey(Integer.parseInt(idcard.substring(0, 2)))) {
				isOk = false;
				msg = "非法地区";
			}

			if (time >= currentTime) {
				isOk = false;
				msg = "非法生日";
			}

			for (i = 0; i < 17; i++) {
				sum += Integer.parseInt(idcard.substring(i, i + 1)) * arrInt[i];
			}
			residue = arrCh[sum % 11];
			if (!residue.equalsIgnoreCase(idcard.substring(17, 18))) {
				isOk = false;
				msg = "非法身份证哦";
			}

			// return
			// city[idcard.substring(0,2)]+","+birthday+","+(idcard.substring(16,1)%2?" //
			// 男":"女")
			Map<String, Object> result = new HashMap<>();

			if (isOk) {
				msg = "ok";
				result.put("province", city.get(Integer.parseInt(idcard.substring(0, 2))));
				result.put("birthday", birthday);
				result.put("gender", Integer.parseInt(idcard.substring(16, 17)) % 2 != 0 ? "男" : "女");
				// return true;
			}

			// result.put("msg", msg);
			return isOk;
		} catch (Exception e) {
			// isOk = false;
			// e.printStackTrace();
			// msg = "身份证格式不正确";
			return false;
		}

	}

	// 18位身份证号码各位的含义:
	// 1-2位省、自治区、直辖市代码；
	// 3-4位地级市、盟、自治州代码；
	// 5-6位县、县级市、区代码；
	// 7-14位出生年月日，比如19670401代表1967年4月1日；
	// 15-17位为顺序号，其中17位（倒数第二位）男为单数，女为双数；
	// 18位为校验码，0-9和X。
	// 作为尾号的校验码，是由把前十七位数字带入统一的公式计算出来的，
	// 计算的结果是0-10，如果某人的尾号是0－9，都不会出现X，但如果尾号是10，那么就得用X来代替，
	// 因为如果用10做尾号，那么此人的身份证就变成了19位。X是罗马数字的10，用X来代替10
	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			String id = getRandomID("安定区");
			boolean isOk = isIdcardFormat(id);
			Map<String, Object> checkIdcard = checkIdcard(id);

			String msg = (String) checkIdcard.get("msg");
			if (msg != null && msg.equals("ok")) {
				//System.out.println(checkIdcard.get("birthday"));
				//System.out.println(checkIdcard.get("gender"));
			}

			if (!isOk) {
				//System.err.println(id);
			}
			System.out.println(id);
		}
//		boolean idcardFormat = isIdcardFormat("62242920020528312x");
//		System.out.println(idcardFormat);

	}

	/**
	 * 获取随机生成的身份证号码
	 * 
	 * @author mingzijian
	 * @return
	 */
//	public static String getRandomID() {
//		String id = "420222199204179999";
//		// 随机生成省、自治区、直辖市代码 1-2
//		String provinces[] = { "11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37",
//				"41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71",
//				"81", "82" };
//		String province = randomOne(provinces);
//		// 随机生成地级市、盟、自治州代码 3-4
//		String city = randomCityCode(18);
//		// 随机生成县、县级市、区代码 5-6
//		String county = randomCityCode(28);
//		// 随机生成出生年月 7-14
//		String birth = randomBirth(20, 50);
//		// 随机生成顺序号 15-17(随机性别)
//		String no = new Random().nextInt(899) + 100 + "";
//		// 随机生成校验码 18
//		String checks[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "X" };
//		String check = randomOne(checks);
//		// 拼接身份证号码
//		id = province + city + county + birth + no + check;
//		return id;
//	}

	/**
	 * 从String[] 数组中随机取出其中一个String字符串
	 * 
	 * @author mingzijian
	 * @param s
	 * @return
	 */
	public static String randomOne(String s[]) {
		return s[new Random().nextInt(s.length - 1)];
	}

	/**
	 * 随机生成两位数的字符串（01-max）,不足两位的前面补0
	 * 
	 * @author mingzijian
	 * @param max
	 * @return
	 */
	public static String randomCityCode(int max) {
		int i = new Random().nextInt(max) + 1;
		return i > 9 ? i + "" : "0" + i;
	}

	/**
	 * 随机生成minAge到maxAge年龄段的人的生日日期
	 * 
	 * @author mingzijian
	 * @param minAge
	 * @param maxAge
	 * @return
	 */
	public static String randomBirth(int minAge, int maxAge) {
		SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
		Calendar date = Calendar.getInstance();
		date.setTime(new Date());// 设置当前日期
		// 随机设置日期为前maxAge年到前minAge年的任意一天
		int randomDay = 365 * minAge + new Random().nextInt(365 * (maxAge - minAge));
		date.set(Calendar.DATE, date.get(Calendar.DATE) - randomDay);
		return dft.format(date.getTime());
	}

	/**
	 * 计算并输出身份证号的主要方法 写的比较累赘比较笨，但条例应该比较清晰。
	 */
	public static String getRandomID(String areaCode) {
		// = ""; // 用于存放用户输入的区域编号
		String birthday = ""; // 用户存放用户输入的出生日期
		String sex = ""; // 用户存放用户输入的性别
		String idNo = ""; // 用户存放用户输入的顺序编号
		// InputStreamReader reader = new InputStreamReader(System.in);
		// BufferedReader in = new BufferedReader(reader);
//		System.out.println("说明：身份证号码生成器：目前仅能支持江苏省的新身份证号(18位)的生成，" + "\r" + "您可以用生成的身份证号去网站注册一个帐号，而不必担心真实姓名和身份证号无法对应，"
//				+ "\r" + "可能无法生成和你期望的号码一致的身份证号，这是因为顺序编号和性别编号范围值导致的，" + "\r" + "比如要生成一个和您身份证一致的号码，这两个编号就必须吻合，"
//				+ "顺序编号在身份证号的左起第15，16位，" + "\r" + "性别编号在身份证号的左起第17位，" + "\r"
//				+ "可以按个人需要添加到省份编号，或做成读取文件的形式比较方便，这里仅仅是一个演示。" + "\r");
//		System.out.println("现在请输入您所在的市/区/县的名称，格式(如南京市玄武区就输入：玄武区就行了): ");
		// try {
		areaCode = "安定区";// in.readLine();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// System.out.println("请输入您的出生日期，格式(19891019): "); // 获取用户输入的出生日期的值
		// try {
		birthday = randomBirth(11, 18);// in.readLine();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// System.out.println("请输入您的性别，格式(范围：1-9，男：奇数，女：偶数): "); // 获取用户输入的性别的值
		// try {
		sex = 1 + (int) (Math.random() * (9 - 1 + 1)) + "";
		;// in.readLine();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// System.out.println("请输入您希望的(两位)顺序号，格式(00-99): "); //
			// 获取用户输入的顺序编号的值，此编号在一定程度生决定了您的身份证号不与他人重复
			// try {
		idNo = String.format("%02d", 0 + (int) (Math.random() * (99)));// in.readLine();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// 判断输入的市/区/县的名称与之对应的编号，目前仅支持江苏省
		if (areaCode.trim().equals("玄武区")) {
			areaCode = Integer.toString(320102);
		} else if (areaCode.trim().equals("白下区")) {
			areaCode = Integer.toString(320103);
		} else if (areaCode.trim().equals("秦淮区")) {
			areaCode = Integer.toString(320104);
		} else if (areaCode.trim().equals("建邺区")) {
			areaCode = Integer.toString(320105);
		} else if (areaCode.trim().equals("鼓楼区")) {
			areaCode = Integer.toString(320106);
		} else if (areaCode.trim().equals("下关区")) {
			areaCode = Integer.toString(320107);
		} else if (areaCode.trim().equals("浦口区")) {
			areaCode = Integer.toString(320111);
		} else if (areaCode.trim().equals("栖霞区")) {
			areaCode = Integer.toString(320113);
		} else if (areaCode.equals("雨花台区")) {
			areaCode = Integer.toString(320114);
		} else if (areaCode.trim().equals("江宁区")) {
			areaCode = Integer.toString(320115);
		} else if (areaCode.trim().equals("六合区")) {
			areaCode = Integer.toString(320116);
		} else if (areaCode.trim().equals("溧水县")) {
			areaCode = Integer.toString(320124);
		} else if (areaCode.trim().equals("高淳县")) {
			areaCode = Integer.toString(320125);
		} else if (areaCode.trim().equals("崇安区")) {
			areaCode = Integer.toString(320202);
		} else if (areaCode.trim().equals("南长区")) {
			areaCode = Integer.toString(320203);
		} else if (areaCode.trim().equals("北塘区")) {
			areaCode = Integer.toString(320204);
		} else if (areaCode.trim().equals("锡山区")) {
			areaCode = Integer.toString(320205);
		} else if (areaCode.trim().equals("惠山区")) {
			areaCode = Integer.toString(320206);
		} else if (areaCode.trim().equals("滨湖区")) {
			areaCode = Integer.toString(320211);
		} else if (areaCode.trim().equals("江阴市")) {
			areaCode = Integer.toString(320281);
		} else if (areaCode.trim().equals("宜兴市")) {
			areaCode = Integer.toString(320282);
		} else if (areaCode.trim().equals("鼓楼区")) {
			areaCode = Integer.toString(320302);
		} else if (areaCode.trim().equals("云龙区")) {
			areaCode = Integer.toString(320303);
		} else if (areaCode.trim().equals("九里区")) {
			areaCode = Integer.toString(320304);
		} else if (areaCode.trim().equals("贾汪区")) {
			areaCode = Integer.toString(320305);
		} else if (areaCode.trim().equals("泉山区")) {
			areaCode = Integer.toString(320311);
		} else if (areaCode.trim().equals("丰县")) {
			areaCode = Integer.toString(320321);
		} else if (areaCode.trim().equals("沛县")) {
			areaCode = Integer.toString(320322);
		} else if (areaCode.trim().equals("铜山县")) {
			areaCode = Integer.toString(320323);
		} else if (areaCode.trim().equals("睢宁县")) {
			areaCode = Integer.toString(320324);
		} else if (areaCode.trim().equals("新沂市")) {
			areaCode = Integer.toString(320381);
		} else if (areaCode.trim().equals("邳州市")) {
			areaCode = Integer.toString(320382);
		} else if (areaCode.trim().equals("天宁区")) {
			areaCode = Integer.toString(320402);
		} else if (areaCode.trim().equals("钟楼区")) {
			areaCode = Integer.toString(320404);
		} else if (areaCode.trim().equals("戚墅堰区")) {
			areaCode = Integer.toString(320405);
		} else if (areaCode.trim().equals("新北区")) {
			areaCode = Integer.toString(320411);
		} else if (areaCode.trim().equals("武进区")) {
			areaCode = Integer.toString(320412);
		} else if (areaCode.trim().equals("溧阳市")) {
			areaCode = Integer.toString(320481);
		} else if (areaCode.trim().equals("金坛市")) {
			areaCode = Integer.toString(320482);
		} else if (areaCode.trim().equals("沧浪区")) {
			areaCode = Integer.toString(320502);
		} else if (areaCode.trim().equals("平江区")) {
			areaCode = Integer.toString(320503);
		} else if (areaCode.trim().equals("金阊区")) {
			areaCode = Integer.toString(320504);
		} else if (areaCode.trim().equals("虎丘区")) {
			areaCode = Integer.toString(320505);
		} else if (areaCode.trim().equals("吴中区")) {
			areaCode = Integer.toString(320506);
		} else if (areaCode.trim().equals("相城区")) {
			areaCode = Integer.toString(320507);
		} else if (areaCode.trim().equals("常熟市")) {
			areaCode = Integer.toString(320581);
		} else if (areaCode.trim().equals("张家港市")) {
			areaCode = Integer.toString(320582);
		} else if (areaCode.trim().equals("昆山市")) {
			areaCode = Integer.toString(320583);
		} else if (areaCode.trim().equals("吴江市")) {
			areaCode = Integer.toString(320584);
		} else if (areaCode.trim().equals("太仓市")) {
			areaCode = Integer.toString(320585);
		} else if (areaCode.trim().equals("崇川区")) {
			areaCode = Integer.toString(320602);
		} else if (areaCode.trim().equals("港闸区")) {
			areaCode = Integer.toString(320611);
		} else if (areaCode.trim().equals("通州区")) {
			areaCode = Integer.toString(320612);
		} else if (areaCode.trim().equals("海安县")) {
			areaCode = Integer.toString(320621);
		} else if (areaCode.trim().equals("如东县")) {
			areaCode = Integer.toString(320623);
		} else if (areaCode.trim().equals("启东市")) {
			areaCode = Integer.toString(320681);
		} else if (areaCode.trim().equals("如皋市")) {
			areaCode = Integer.toString(320682);
		} else if (areaCode.trim().equals("海门市")) {
			areaCode = Integer.toString(320684);
		} else if (areaCode.trim().equals("连云区")) {
			areaCode = Integer.toString(320703);
		} else if (areaCode.trim().equals("新浦区")) {
			areaCode = Integer.toString(320705);
		} else if (areaCode.trim().equals("海州区")) {
			areaCode = Integer.toString(320706);
		} else if (areaCode.trim().equals("赣榆县")) {
			areaCode = Integer.toString(320721);
		} else if (areaCode.trim().equals("东海县")) {
			areaCode = Integer.toString(320722);
		} else if (areaCode.trim().equals("灌云县")) {
			areaCode = Integer.toString(320723);
		} else if (areaCode.trim().equals("灌南县")) {
			areaCode = Integer.toString(320724);
		} else if (areaCode.trim().equals("清河区")) {
			areaCode = Integer.toString(320802);
		} else if (areaCode.trim().equals("楚州区")) {
			areaCode = Integer.toString(320803);
		} else if (areaCode.trim().equals("淮阴区")) {
			areaCode = Integer.toString(320804);
		} else if (areaCode.trim().equals("清浦区")) {
			areaCode = Integer.toString(320811);
		} else if (areaCode.trim().equals("涟水县")) {
			areaCode = Integer.toString(320826);
		} else if (areaCode.trim().equals("洪泽县")) {
			areaCode = Integer.toString(320829);
		} else if (areaCode.trim().equals("盱眙县")) {
			areaCode = Integer.toString(320830);
		} else if (areaCode.trim().equals("金湖县")) {
			areaCode = Integer.toString(320831);
		} else if (areaCode.trim().equals("亭湖区")) {
			areaCode = Integer.toString(320902);
		} else if (areaCode.trim().equals("盐都区")) {
			areaCode = Integer.toString(320903);
		} else if (areaCode.trim().equals("响水县")) {
			areaCode = Integer.toString(320921);
		} else if (areaCode.trim().equals("滨海县")) {
			areaCode = Integer.toString(320922);
		} else if (areaCode.trim().equals("阜宁县")) {
			areaCode = Integer.toString(320923);
		} else if (areaCode.trim().equals("射阳县")) {
			areaCode = Integer.toString(320924);
		} else if (areaCode.trim().equals("建湖县")) {
			areaCode = Integer.toString(320925);
		} else if (areaCode.trim().equals("东台市")) {
			areaCode = Integer.toString(320981);
		} else if (areaCode.trim().equals("大丰市")) {
			areaCode = Integer.toString(320982);
		} else if (areaCode.trim().equals("广陵区")) {
			areaCode = Integer.toString(321002);
		} else if (areaCode.trim().equals("邗江区")) {
			areaCode = Integer.toString(321003);
		} else if (areaCode.trim().equals("维扬区")) {
			areaCode = Integer.toString(321011);
		} else if (areaCode.trim().equals("宝应县")) {
			areaCode = Integer.toString(321023);
		} else if (areaCode.trim().equals("仪征市")) {
			areaCode = Integer.toString(321081);
		} else if (areaCode.trim().equals("高邮市")) {
			areaCode = Integer.toString(321084);
		} else if (areaCode.trim().equals("江都市")) {
			areaCode = Integer.toString(321088);
		} else if (areaCode.trim().equals("京口区")) {
			areaCode = Integer.toString(321102);
		} else if (areaCode.trim().equals("润州区")) {
			areaCode = Integer.toString(321111);
		} else if (areaCode.trim().equals("丹徒区")) {
			areaCode = Integer.toString(321112);
		} else if (areaCode.trim().equals("丹阳市")) {
			areaCode = Integer.toString(321181);
		} else if (areaCode.trim().equals("扬中市")) {
			areaCode = Integer.toString(321182);
		} else if (areaCode.trim().equals("句容市")) {
			areaCode = Integer.toString(321183);
		} else if (areaCode.trim().equals("海陵区")) {
			areaCode = Integer.toString(321202);
		} else if (areaCode.trim().equals("高港区")) {
			areaCode = Integer.toString(321203);
		} else if (areaCode.trim().equals("兴化市")) {
			areaCode = Integer.toString(321281);
		} else if (areaCode.trim().equals("靖江市")) {
			areaCode = Integer.toString(321282);
		} else if (areaCode.trim().equals("泰兴市")) {
			areaCode = Integer.toString(321283);
		} else if (areaCode.trim().equals("姜堰市")) {
			areaCode = Integer.toString(321284);
		} else if (areaCode.trim().equals("宿城区")) {
			areaCode = Integer.toString(321302);
		} else if (areaCode.trim().equals("宿豫区")) {
			areaCode = Integer.toString(321311);
		} else if (areaCode.trim().equals("沭阳县")) {
			areaCode = Integer.toString(321322);
		} else if (areaCode.trim().equals("泗阳县")) {
			areaCode = Integer.toString(321323);
		} else if (areaCode.trim().equals("泗洪县")) {
			areaCode = Integer.toString(321324);
//			621100 　　定西
//			621102 　　安定区
//			621121 　　通渭县
//			621122 　　陇西县
//			621123 　　渭源县
//			621124 　　临洮县
//			621125 　　漳县
//			621126 　　岷县
		} else if (areaCode.trim().equals("定西")) {
			areaCode = Integer.toString(621100);
		} else if (areaCode.trim().equals("安定区")) {
			areaCode = Integer.toString(621102);
		} else if (areaCode.trim().equals("通渭县")) {
			areaCode = Integer.toString(621121);
		} else if (areaCode.trim().equals("陇西县")) {
			areaCode = Integer.toString(621122);
		} else if (areaCode.trim().equals("渭源县")) {
			areaCode = Integer.toString(621123);
		} else if (areaCode.trim().equals("临洮县")) {
			areaCode = Integer.toString(621124);
		} else if (areaCode.trim().equals("漳县")) {
			areaCode = Integer.toString(621125);
		} else if (areaCode.trim().equals("岷县")) {
			areaCode = Integer.toString(621126);
		}
		// 前17位要除以的数：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
		int finalAreaCode = Integer.parseInt(areaCode.substring(0, 1)) * 7
				+ Integer.parseInt(areaCode.substring(1, 2)) * 9 + Integer.parseInt(areaCode.substring(2, 3)) * 10
				+ Integer.parseInt(areaCode.substring(3, 4)) * 5 + Integer.parseInt(areaCode.substring(4, 5)) * 8
				+ Integer.parseInt(areaCode.substring(5, 6)) * 4;
		int finalBirthday = Integer.parseInt(birthday.substring(0, 1)) * 2
				+ Integer.parseInt(birthday.substring(1, 2)) * 1 + Integer.parseInt(birthday.substring(2, 3)) * 6
				+ Integer.parseInt(birthday.substring(3, 4)) * 3 + Integer.parseInt(birthday.substring(4, 5)) * 7
				+ Integer.parseInt(birthday.substring(5, 6)) * 9 + Integer.parseInt(birthday.substring(6, 7)) * 10
				+ Integer.parseInt(birthday.substring(7, 8)) * 5;
		int NoIs = (Integer.parseInt(idNo.substring(0, 1))) * 8 + (Integer.parseInt(idNo.substring(1, 2))) * 4;
		int sexNo = (Integer.parseInt(sex.substring(0, 1))) * 2;
		int checkCode = (finalAreaCode + finalBirthday + NoIs + sexNo) % 11;
		int finalCheckCode = 0;
		// 余数范围： 0 1 2 3 4 5 6 7 8 9 10
		// 余数对应的数：1 0 X 9 8 7 6 5 4 3 2
		// 计算出最终的校验码：finalCheckCode
		switch (checkCode) {
		case 0:
			finalCheckCode = 1;
			break;
		case 1:
			finalCheckCode = 0;
			break;
		case 2:
			finalCheckCode = -3;
			break;
		case 3:
			finalCheckCode = 9;
			break;
		case 4:
			finalCheckCode = 8;
			break;
		case 5:
			finalCheckCode = 7;
			break;
		case 6:
			finalCheckCode = 6;
			break;
		case 7:
			finalCheckCode = 5;
			break;
		case 8:
			finalCheckCode = 4;
			break;
		case 9:
			finalCheckCode = 3;
			break;
		case 10:
			finalCheckCode = 2;
			break;
		default:
			break;
		}
		// System.out.println("恭喜，生成的身份证号是：" + "\r" + areaCode + birthday + idNo + sex +
		// finalCheckCode);
		return areaCode + birthday + idNo + sex + (finalCheckCode == -3 ? "X" : finalCheckCode);
		// 区域编号(6位数)+出生日期(8位数)+顺序编号(2位数)+性别号(1位数)+校验码(1位数)=身份证号(18位数)
	}

	/**
	 * CalcID类的无参构造方法，调用此方法即可调用其方法。
	 */

}
