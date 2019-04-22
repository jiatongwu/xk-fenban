package cn.xvkang.utils;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class WordExcelUtils {
	@SuppressWarnings("deprecation")
	public static String getCellStringValue(XSSFCell cell) {
		
		if (cell == null) {
			return null;
		}
		//如果 numberic 是日期类型需要 处理一下
		CellType oldCellTypeEnum = cell.getCellTypeEnum();
		switch (oldCellTypeEnum) {
		case NUMERIC:
		{		
		    if (DateUtil.isCellDateFormatted(cell)) {
		        Date date = cell.getDateCellValue();
		        try {
					String dateString=DateFormatUtils.format(date, "yyyy年MM月dd日");
					if(StringUtils.isNotBlank(dateString)) {
						return dateString;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		        
		    } 
			break;
		}
		default:

			break;
		}
		cell.setCellType(Cell.CELL_TYPE_STRING);
		String result = null;
		CellType cellTypeEnum = cell.getCellTypeEnum();
		switch (cellTypeEnum) {
		case NUMERIC:
			double numericCellValue = cell.getNumericCellValue();
			result = numericCellValue + "";
			break;
		case STRING:
			result = cell.getStringCellValue();
			break;
		default:

			break;
		}
		return result;
	}


}
