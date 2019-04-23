package cn.xvkang.utils.mybatis;

public class TableNameGenerateMybatisGeneratorXML {
	public static void main(String[] args) {

		String[] arr = { 
				
				"kemu       "   ,
				"kemuzuhe   "   ,
				"student    "   ,
				"user       "   
		};

		for (String ar : arr) {
			String token = ar.trim();
			String xml = "<table tableName=\"%s\">\r\n" + 
					"			<property name=\"useActualColumnNames\" value=\"false\" />\r\n" + 
					"			<generatedKey column=\"id\" sqlStatement=\"Mysql\" identity=\"true\" />\r\n" + 
					"		</table>";
			System.out.printf(xml, token);
			System.out.println();
		}
	}

}
