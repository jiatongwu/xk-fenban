package cn.xvkang.utils.mybatis;

public class TableNameGenerateMybatisGeneratorXML {
	public static void main(String[] args) {

		String[] arr = { "ZFTJ" };

		for (String ar : arr) {
			String token = ar.trim();
			String xml = "<table tableName=\"%s\"> <property name=\"useActualColumnNames\" value=\"false\" /> <generatedKey column=\"id\" sqlStatement=\"SELECT %s.nextval AS id FROM DUAL\" />	</table>";
			System.out.printf(xml, token, token + "_SEQ");
			System.out.println();
		}
	}

}
