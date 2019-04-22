package cn.xvkang.utils.mybatis;

public class TableNameGenerateCreateSequence {
	public static void main(String[] args) {

		String start = "CREATE SEQUENCE  \"";
		String end = "\"  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE";
		String[] arr = { 
				"ZFTJ"
		};
		for (String ar : arr) {

			String token = ar.trim();
			
			System.out.println("drop sequence "+ token +"_SEQ;");
		}
		for (String ar : arr) {

			String token = ar.trim();
			
			System.out.println(start + token +"_SEQ"+ end + ";");
		}
	}

}
