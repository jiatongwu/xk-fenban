package cn.xvkang.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.xvkang.dao.CalcZfScoreAllMapper;
import cn.xvkang.dao.ScoreAllMapper;
import cn.xvkang.dao.SportScoreMapper;
import cn.xvkang.dao.StuInfoMapper;
import cn.xvkang.dao.SubExtMapper;
import cn.xvkang.dao.SubMapper;
import cn.xvkang.dao.TXmsMapper;
import cn.xvkang.dto.bjsubidpercent.BjPercent;
import cn.xvkang.dto.bjsubidpercent.BjSubidPercent;
import cn.xvkang.dto.subidLevelScore.SubidLevelScore;
import cn.xvkang.entity.TXms;
import cn.xvkang.entity.TXmsExample;
import cn.xvkang.service.TxmsService;
import cn.xvkang.utils.Constants;
import cn.xvkang.utils.NumberUtils;
import cn.xvkang.utils.StringUtil;
import cn.xvkang.utils.WordExcelUtils;
import cn.xvkang.utils.page.Page;
import cn.xvkang.utils.page.PageImpl;
import cn.xvkang.utils.page.PageRequest;

@Service
public class TxmsServiceImpl implements TxmsService {
	@Autowired
	private TXmsMapper tXmsMapper;
	@Autowired
	private SportScoreMapper sportScoreMapper;
	@Autowired
	private ScoreAllMapper scoreAllMapper;
	@Autowired
	private StuInfoMapper stuInfoMapper;
	@Autowired
	private SubMapper subMapper;
	@Autowired
	private SubExtMapper subExtMapper;
	@Autowired
	private CalcZfScoreAllMapper calcZfScoreAllMapper;

	@Override
	public Page<TXms> table(String name, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		TXmsExample txmsExample = new TXmsExample();
		txmsExample.createCriteria().andXmLike("%" + name + "%");
		List<TXms> txms = tXmsMapper.selectByExample(txmsExample);
		PageImpl<TXms> pageImpl = new PageImpl<TXms>(txms, new PageRequest(pageNum - 1, pageSize),
				((com.github.pagehelper.Page<TXms>) txms).getTotal());
		return pageImpl;
	}

	@Override
	public Map<String, Object> importExcel(String xmid, String deleteOrigin, InputStream inputStream) {
		Map<String, Object> result = new HashMap<>();
		boolean deleteOriginStudentBoolean = new Boolean(deleteOrigin);
		String sportScoreTableName = "T" + xmid + Constants.SPORT_SCORE_SUFFIX;
		String scollAllTableName = "T" + xmid + Constants.SCOREAll_SUFFIX;
		// Set<String> allBiyeSchoolName = schoolDao.getAllBiyeSchoolName();
		sportScoreMapper.createSportScoreTable(sportScoreTableName);
		if (deleteOriginStudentBoolean) {
			// 删除项目体育成绩表中所有数据
			sportScoreMapper.deleteAll(sportScoreTableName);
		}

		List<String> errorMessages = new ArrayList<String>();

		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();

		long errorCount = 0l;
		long successCount = 0l;
		XSSFWorkbook xssfWorkbook;

		try {
			xssfWorkbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);

			for (int i = 1; i < 1048576; i++) {
				// 处理每一行
				XSSFRow row = sheetAt.getRow(i);
				if (row == null) {
					// 最后一行了，终止处理。
					break;
				}
				XSSFCell cell0 = row.getCell(0);
				String cell0String = WordExcelUtils.getCellStringValue(cell0);
				if (StringUtils.isBlank(cell0String)) {
					// 最后一行了，终止处理。
					break;
				}

				boolean isDataFormatOk = true;

				Map<String, String> dataMap = new HashMap<>();
				// 取到excel一行中的数据
				dataMap.put("examcode", cell0String);
				XSSFCell cell1 = row.getCell(1);
				String cell1String = WordExcelUtils.getCellStringValue(cell1);
				dataMap.put("score", cell1String);

				StringBuilder errorMsgStringBuilder = new StringBuilder();
				errorMsgStringBuilder.append("第" + (i + 1) + "行：");

				Map<String, Object> findByExamcode = scoreAllMapper.findByExamcode(scollAllTableName, cell0String);
				if (!(findByExamcode != null && findByExamcode.size() > 0)) {
					isDataFormatOk = false;
					errorMsgStringBuilder.append(" 没有此考生编号的信息 ");
				}
				// 判断格式是否正确
				if (!StringUtil.isNumber(cell1String)) {
					isDataFormatOk = false;
					errorMsgStringBuilder.append(" 体育成绩必须是数字 ");
				}

				if (isDataFormatOk) {
					datas.add(dataMap);
					// 一行一行地进行数据处理 插入或更新数据库表
					sportScoreMapper.saveOrUpdate(sportScoreTableName, dataMap.get("examcode"), dataMap.get("score"));
					successCount++;
				} else {
					// 有格式不正确的行 ，提示给用户
					errorMessages.add(errorMsgStringBuilder.toString());
					errorCount++;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		result.put("state", "ok");
		result.put("msg", "导入成功" + successCount + "个考生体育成绩");
		result.put("errorMessages", errorMessages);
		result.put("successCount", successCount);
		result.put("errorCount", errorCount);
		return result;
	}

	public static void main(String[] args) {

		int a = 178;
		int b = 1801;
		double aa = (a + 0.00);
		System.out.println(aa / b);
	}

	@Override
	public List<String> getSchoolsByXmid(String xmid) {
		String stuInfoTableName = "T" + xmid + Constants.STUINFO_SUFFIX;
		List<String> findAllSchool = stuInfoMapper.findAllSchool(stuInfoTableName);
		return findAllSchool;
	}

	@Override
	public List<String> getClassesByXmidAndSchool(String xmid, String school) {
		String stuInfoTableName = "T" + xmid + Constants.STUINFO_SUFFIX;
		List<String> findAllClazz = stuInfoMapper.findAllClazz(stuInfoTableName, school);
		return findAllClazz;
	}

	@Override
	public Resource exportBjRank(String xmid, String school, String bj) {
		TXms xm = tXmsMapper.selectByPrimaryKey(xmid);
		String sportScoreTableName = "T" + xmid + Constants.SPORT_SCORE_SUFFIX;
		sportScoreMapper.createSportScoreTable(sportScoreTableName);
		String stuInfoTableName = "T" + xmid + Constants.STUINFO_SUFFIX;
		String scollAllTableName = "T" + xmid + Constants.SCOREAll_SUFFIX;
		String subTableName = "T" + xmid + Constants.SUB_SUFFIX;

		List<Map<String, Object>> bjStudentScoreRanks = scoreAllMapper.findAllBySchoolAndBj(stuInfoTableName,
				scollAllTableName, sportScoreTableName, school, bj);
		// 拿到这次考试所有科目
		List<Map<String, Object>> selectAllSubByTableName = subMapper.selectAllSubByTableName(subTableName);
		// 判断这个班有多少科目考试了 移除本班级没有考试的科目 因为有的班不考历史
		if (selectAllSubByTableName != null) {
			List<Map<String, Object>> removeList = new ArrayList<>();
			for (Map<String, Object> tmpMap : selectAllSubByTableName) {
				String subid = (String) tmpMap.get("subid");
				int findCountNotNullBySchoolAndClassAndKemuZiduan = scoreAllMapper
						.findCountNotNullBySchoolAndClassAndKemuZiduan(stuInfoTableName, scollAllTableName, school, bj,
								"x" + subid);
				if (findCountNotNullBySchoolAndClassAndKemuZiduan == 0) {
					removeList.add(tmpMap);
				}
			}
			for (Map<String, Object> tmpMap : removeList) {
				selectAllSubByTableName.remove(tmpMap);
			}
		}

		if (selectAllSubByTableName != null && bjStudentScoreRanks != null) {
			// 拿到科目数量之后 就能确定是多少列了
			int kemuSize = selectAllSubByTableName.size();
			if (kemuSize > 0) {
				Workbook workbook = new XSSFWorkbook();
				try {
					int totalCols = (kemuSize + 1) * 3 + 3;
					int totalRows = bjStudentScoreRanks.size() + 3;

					Sheet sheet = workbook.createSheet(bj + "班学生排名统计");
//					for (int i = 0; i < totalCols; i++) {
//						//sheet.setColumnWidth(i, 25 * 256);
//						 sheet.autoSizeColumn(i);
//					}
					// 居中
					CellStyle style = workbook.createCellStyle();
					style.setAlignment(HorizontalAlignment.CENTER);
					style.setVerticalAlignment(VerticalAlignment.CENTER);

					// 边框
					style.setBorderBottom(BorderStyle.THIN);
					style.setBorderTop(BorderStyle.THIN);
					style.setBorderLeft(BorderStyle.THIN);
					style.setBorderRight(BorderStyle.THIN);
					// 标题style
					// 居中
					CellStyle styleTitle = workbook.createCellStyle();
					styleTitle.setAlignment(HorizontalAlignment.CENTER);
					styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
					// 字体
					Font font = workbook.createFont();
					font.setFontName("宋体");
					font.setFontHeightInPoints((short) 12);
					style.setFont(font);
					Font titleFont = workbook.createFont();
					titleFont.setFontName("宋体");
					titleFont.setFontHeightInPoints((short) 18);
					styleTitle.setFont(titleFont);
					// 文本格式
					DataFormat format = workbook.createDataFormat();
					styleTitle.setDataFormat(format.getFormat("@"));
					style.setDataFormat(format.getFormat("@"));
					// 边框
					styleTitle.setBorderBottom(BorderStyle.THIN);

					// 标题
					// 合并单元格
					sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, totalCols - 1));
					sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
					sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
					for (int i = 0; i < kemuSize; i++) {
						sheet.addMergedRegion(new CellRangeAddress(1, 1, (i * 3) + 2, (i * 3) + 2 + 2));
					}
					sheet.addMergedRegion(new CellRangeAddress(1, 1, (kemuSize * 3) + 3, (kemuSize * 3) + 3 + 2));
					sheet.addMergedRegion(new CellRangeAddress(1, 2, (kemuSize * 3) + 2, (kemuSize * 3) + 2));
					// 将表头数据填进去
					Row row = sheet.createRow(0);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
					Cell row1Cell = row.createCell(0);
					row1Cell.setCellStyle(styleTitle);
					row1Cell.setCellValue(xm.getGrade() + "(" + bj + ")班考试成绩排名　" + sdf.format(xm.getCurtime()));
					Row row2 = sheet.createRow(1);
					Cell row2Cell1 = row2.createCell(0);
					row2Cell1.setCellStyle(style);
					row2Cell1.setCellValue("班内编号");
					Cell row2Cell2 = row2.createCell(1);
					row2Cell2.setCellStyle(style);
					row2Cell2.setCellValue("姓名");
					for (int i = 0; i < kemuSize; i++) {
						Cell createCell = row2.createCell((i * 3) + 2);
						createCell.setCellStyle(style);
						createCell.setCellValue((String) selectAllSubByTableName.get(i).get("sub"));
					}
					Cell row2CellTiyu = row2.createCell((kemuSize * 3) + 2);
					row2CellTiyu.setCellStyle(style);
					row2CellTiyu.setCellValue("体育");
					Cell row2CellZongFen = row2.createCell((kemuSize * 3) + 3);
					row2CellZongFen.setCellStyle(style);
					row2CellZongFen.setCellValue("总分");
					Row row3 = sheet.createRow(2);
					for (int i = 0; i < kemuSize; i++) {
						Cell createCell = row3.createCell((i * 3) + 2);
						createCell.setCellStyle(style);
						createCell.setCellValue("成绩");
						Cell createCell2 = row3.createCell((i * 3) + 3);
						createCell2.setCellStyle(style);
						createCell2.setCellValue("校");
						Cell createCell3 = row3.createCell((i * 3) + 4);
						createCell3.setCellStyle(style);
						createCell3.setCellValue("班");
					}
					Cell row3CellZongFenChengji = row3.createCell((kemuSize * 3) + 3);
					row3CellZongFenChengji.setCellStyle(style);
					row3CellZongFenChengji.setCellValue("成绩");
					Cell row3CellZongFenXiaoRank = row3.createCell((kemuSize * 3) + 4);
					row3CellZongFenXiaoRank.setCellStyle(style);
					row3CellZongFenXiaoRank.setCellValue("校");
					Cell row3CellZongFenBanRank = row3.createCell((kemuSize * 3) + 5);
					row3CellZongFenBanRank.setCellStyle(style);
					row3CellZongFenBanRank.setCellValue("班");
					// 一行一行插入数据
					int rowIndex = 3;
					for (Map<String, Object> tmpScore : bjStudentScoreRanks) {
						String uname = (String) tmpScore.get("uname");
						String examcode = (String) tmpScore.get("examcode");
						Float sportScore = (Float) tmpScore.get("sportScore");
						Row studentDataRow = sheet.createRow(rowIndex);

						Cell examcodeCell = studentDataRow.createCell(0);
						examcodeCell.setCellValue(examcode);
						examcodeCell.setCellStyle(style);
						Cell unameCell = studentDataRow.createCell(1);
						unameCell.setCellValue(uname);
						unameCell.setCellStyle(style);

						for (int i = 0; i < kemuSize; i++) {
							Cell createCell = studentDataRow.createCell((i * 3) + 2);
							createCell.setCellStyle(style);
							Map<String, Object> map = selectAllSubByTableName.get(i);
							String subid = (String) map.get("subid");
							Map<String, Object> findByExamcode = scoreAllMapper.findByExamcode(scollAllTableName,
									examcode);
							Float x = (Float) findByExamcode.get("x" + subid);
							int nx = (Integer) findByExamcode.get("Nx" + subid);
							int bx = (Integer) findByExamcode.get("Bx" + subid);

							createCell.setCellValue((x == null ? 0 : x.intValue()));
							Cell createCell2 = studentDataRow.createCell((i * 3) + 3);
							createCell2.setCellStyle(style);
							createCell2.setCellValue(nx);
							Cell createCell3 = studentDataRow.createCell((i * 3) + 4);
							createCell3.setCellStyle(style);
							createCell3.setCellValue(bx);
						}
						Cell sportScoreCell = studentDataRow.createCell((kemuSize * 3) + 2);
						sportScoreCell.setCellStyle(style);
						sportScoreCell.setCellValue(sportScore.intValue());
						Map<String, Map<String, Object>> findStudentClassRank = scoreAllMapper.findStudentClassRank(
								stuInfoTableName, scollAllTableName, sportScoreTableName, school, bj);
						Map<String, Map<String, Object>> findStudentSchoolRank = scoreAllMapper.findStudentSchoolRank(
								stuInfoTableName, scollAllTableName, sportScoreTableName, school);
						Cell studentDataRowCellZongFenChengji = studentDataRow.createCell((kemuSize * 3) + 3);
						studentDataRowCellZongFenChengji.setCellStyle(style);
						studentDataRowCellZongFenChengji
								.setCellValue(((Float) findStudentClassRank.get(examcode).get("sumScore")).intValue());
						Cell studentDataRowCellZongFenXiaoRank = studentDataRow.createCell((kemuSize * 3) + 4);
						studentDataRowCellZongFenXiaoRank.setCellStyle(style);
						studentDataRowCellZongFenXiaoRank
								.setCellValue((Long) findStudentSchoolRank.get(examcode).get("rank"));
						Cell studentDataRowCellZongFenBanRank = studentDataRow.createCell((kemuSize * 3) + 5);
						studentDataRowCellZongFenBanRank.setCellStyle(style);
						studentDataRowCellZongFenBanRank
								.setCellValue((Long) findStudentClassRank.get(examcode).get("rank"));

						rowIndex++;
					}

					// 添加合并单元格的框线
					setBordersToMergedCells(sheet);
					for (int i = 1; i < totalRows - 1; i++) {
						Row createRow = sheet.getRow(i);
						if (createRow == null) {
							createRow = sheet.createRow(i);
						}
						for (int j = 0; j < totalCols; j++) {
							Cell cell = createRow.getCell(j);

							if (cell == null) {
								cell = createRow.createCell(j);
							}
							cell.setCellStyle(style);
						}
					}

					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					workbook.write(byteArrayOutputStream);

					InputStreamResource isr = new InputStreamResource(
							new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
					return isr;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						workbook.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}
		return null;
	}

	@Override
	public Resource exportSubPercent(String xmid, String school) {
		TXms xm = tXmsMapper.selectByPrimaryKey(xmid);
		String grade = xm.getGrade();
		String sportScoreTableName = "T" + xmid + Constants.SPORT_SCORE_SUFFIX;
		sportScoreMapper.createSportScoreTable(sportScoreTableName);
		String stuInfoTableName = "T" + xmid + Constants.STUINFO_SUFFIX;
		String scollAllTableName = "T" + xmid + Constants.SCOREAll_SUFFIX;
		String subTableName = "T" + xmid + Constants.SUB_SUFFIX;
		String subExtTableName = "T" + xmid + Constants.SUB_EXT_SUFFIX;

		// 查询出所有班级
		List<String> findAllClazz = stuInfoMapper.findAllClazz(stuInfoTableName, school);
		// 查询这个学校有多少科考试了
		List<String> allSubByXmidAndSchool = getAllSubByXmidAndSchool(xmid, school);
		Map<String, Map<String, Object>> selectAllSubByTableNameMap = subMapper
				.selectAllSubByTableNameMap(subTableName);
		// subExtMapper.selectNjAvgAndMaxScore(subExtTableName, school, subid)
		// 1. 查询各班级的人数 和 整个年级的人数
		Map<String, Integer> bjStuCountMap = new HashMap<>();
		for (String tmpClass : findAllClazz) {
			int findStuCount = stuInfoMapper.findStuCount(stuInfoTableName, school, tmpClass);
			bjStuCountMap.put(tmpClass, findStuCount);
		}
		int findNjStuCount = stuInfoMapper.findNjStuCount(stuInfoTableName, school);
		bjStuCountMap.put(grade, findNjStuCount);

		// 2. 查询计算出各科 各等级 最高分 最低分
		Map<String, Map<String, SubidLevelScore>> levelScoreMap = getLevelMaxScoreAndMinScore(school, stuInfoTableName,
				scollAllTableName, allSubByXmidAndSchool);

		// 3. 查询计算出 各个班在 某一科中 人数多少个 平均分多少 最高分多少 最后也算一下年级的情况
		Map<String, Map<String, BjSubidPercent>> kemuBjSubidPercent = getBjPercentOnLevel(school, grade,
				stuInfoTableName, scollAllTableName, sportScoreTableName, subExtTableName, findAllClazz, bjStuCountMap,
				levelScoreMap);

		// 4. 计算总分各级别 最高分 最低分
		Map<String, SubidLevelScore> calcZfLevelMaxScoreMinScore = calcZfLevelMaxScoreMinScore(school, stuInfoTableName,
				scollAllTableName);

		// 5. 计算各班 总分 占比 用各班平均分 最高分
		Map<String, BjSubidPercent> calcZfLevelPercent = calcZfLevelPercent(school, grade, sportScoreTableName,
				stuInfoTableName, scollAllTableName, findAllClazz, bjStuCountMap, calcZfLevelMaxScoreMinScore);

		// 6.开始根据数据动态导出excel
		Resource resource = exportSubPercentExcel(xm, findAllClazz, allSubByXmidAndSchool, selectAllSubByTableNameMap,
				bjStuCountMap, levelScoreMap, kemuBjSubidPercent, calcZfLevelMaxScoreMinScore, calcZfLevelPercent);
		return resource;
	}

	public Resource exportSubPercentExcel(TXms xm, List<String> findAllClazz, List<String> allSubByXmidAndSchool,
			Map<String, Map<String, Object>> selectAllSubByTableNameMap, Map<String, Integer> bjStuCountMap,
			Map<String, Map<String, SubidLevelScore>> levelScoreMap,
			Map<String, Map<String, BjSubidPercent>> kemuBjSubidPercent,
			Map<String, SubidLevelScore> calcZfLevelMaxScoreMinScore, Map<String, BjSubidPercent> calcZfLevelPercent) {
		String xmName = xm.getXm();
		String grade = xm.getGrade();
		Workbook workbook = new XSSFWorkbook();
		int bjCount = bjStuCountMap.size() - 1;
		int kemuCount = kemuBjSubidPercent.size();
		try {
			// 一共有多少列
			int totalCols = (bjCount + 1) * 2 + 3;
			// 一共有多少行
			int totalRows = kemuCount * 7 + 3 + 1 + 10;
			Sheet sheet = workbook.createSheet(xmName);
			// 居中
			CellStyle style = workbook.createCellStyle();
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setVerticalAlignment(VerticalAlignment.CENTER);
			// 边框
			style.setBorderBottom(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);

			// 底部双划线样式
			CellStyle doubleBottomBorderStyle = workbook.createCellStyle();
			doubleBottomBorderStyle.setAlignment(HorizontalAlignment.CENTER);
			doubleBottomBorderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			// 边框
			doubleBottomBorderStyle.setBorderBottom(BorderStyle.DOUBLE);
			doubleBottomBorderStyle.setBorderTop(BorderStyle.THIN);
			doubleBottomBorderStyle.setBorderLeft(BorderStyle.THIN);
			doubleBottomBorderStyle.setBorderRight(BorderStyle.THIN);

			// 标题style
			// 居中
			CellStyle styleTitle = workbook.createCellStyle();
			styleTitle.setAlignment(HorizontalAlignment.CENTER);
			styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
			CellStyle lastRowStyle = workbook.createCellStyle();
			lastRowStyle.setAlignment(HorizontalAlignment.CENTER);
			lastRowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			// 字体
			Font font = workbook.createFont();
			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 12);

			Font titleFont = workbook.createFont();
			titleFont.setFontName("宋体");
			titleFont.setFontHeightInPoints((short) 18);
			Font bootomFont = workbook.createFont();
			bootomFont.setFontName("宋体");
			bootomFont.setFontHeightInPoints((short) 12);

			lastRowStyle.setFont(bootomFont);

			styleTitle.setFont(titleFont);
			style.setFont(font);
			doubleBottomBorderStyle.setFont(font);
			// 文本格式
			DataFormat format = workbook.createDataFormat();
			styleTitle.setDataFormat(format.getFormat("@"));
			style.setDataFormat(format.getFormat("@"));
			lastRowStyle.setDataFormat(format.getFormat("@"));
			doubleBottomBorderStyle.setDataFormat(format.getFormat("@"));
			// 边框
			styleTitle.setBorderBottom(BorderStyle.THIN);

			// 合并单元格
			// 标题
			CellRangeAddress titleCellRangeAddress = new CellRangeAddress(0, 0, 0, totalCols - 1);
			sheet.addMergedRegion(titleCellRangeAddress);
			// 合并班级和人数那二行
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 2));
			for (int i = 0; i < (bjCount + 1); i++) {
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 3 + i * 2, 3 + i * 2 + 1));
				sheet.addMergedRegion(new CellRangeAddress(2, 2, 3 + i * 2, 3 + i * 2 + 1));
			}
			// 循环合并各科目那一大块单元格
			for (int i = 0; i < kemuCount; i++) {
				// 合并第一列
				sheet.addMergedRegion(new CellRangeAddress(3 + i * 7, 3 + i * 7 + 6, 0, 0));
				// 合并平均分那个单元格
				sheet.addMergedRegion(new CellRangeAddress(3 + i * 7, 3 + i * 7, 1, 2));
				// 合并最高分那个单元格
				sheet.addMergedRegion(new CellRangeAddress(3 + i * 7 + 1, 3 + i * 7 + 1, 1, 2));
				// 循环合并各班 平均分 最高分那几个单元格
				for (int j = 0; j < (bjCount + 1); j++) {
					sheet.addMergedRegion(new CellRangeAddress(3 + i * 7, 3 + i * 7, 3 + j * 2, 3 + j * 2 + 1));
					sheet.addMergedRegion(new CellRangeAddress(3 + i * 7 + 1, 3 + i * 7 + 1, 3 + j * 2, 3 + j * 2 + 1));
				}
			}
			// 合并体育平均分那一单元格
			sheet.addMergedRegion(new CellRangeAddress(3 + (kemuCount) * 7, 3 + (kemuCount) * 7, 0, 2));
			// 循环合并各班体育平均分那几个单元格
			for (int j = 0; j < (bjCount + 1); j++) {
				sheet.addMergedRegion(
						new CellRangeAddress(3 + (kemuCount) * 7, 3 + (kemuCount) * 7, 3 + j * 2, 3 + j * 2 + 1));
			}
			// 合并总分那一列
			sheet.addMergedRegion(new CellRangeAddress(4 + (kemuCount) * 7, 4 + (kemuCount) * 7 + 8, 0, 0));
			// 合并总分平均分最高分
			sheet.addMergedRegion(new CellRangeAddress(4 + (kemuCount) * 7, 4 + (kemuCount) * 7, 1, 2));
			sheet.addMergedRegion(new CellRangeAddress(4 + (kemuCount) * 7 + 1, 4 + (kemuCount) * 7 + 1, 1, 2));
			// 循环合并总分 平均分 最高分那几个单元格
			for (int j = 0; j < (bjCount + 1); j++) {
				sheet.addMergedRegion(
						new CellRangeAddress(4 + (kemuCount) * 7, 4 + (kemuCount) * 7, 3 + j * 2, 3 + j * 2 + 1));
				sheet.addMergedRegion(new CellRangeAddress(4 + (kemuCount) * 7 + 1, 4 + (kemuCount) * 7 + 1, 3 + j * 2,
						3 + j * 2 + 1));
			}
			// 合并最底下那一行
			sheet.addMergedRegion(
					new CellRangeAddress(4 + (kemuCount) * 7 + 9, 4 + (kemuCount) * 7 + 9, 0, totalCols - 1));

			// 合并完单元格后开始填充数据
			// 第一行
			Row firstRow = sheet.createRow(0);
			Cell titleCell = firstRow.createCell(0);
			titleCell.setCellStyle(styleTitle);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
			titleCell.setCellValue(xmName + sdf.format(xm.getCurtime()));
			// 班级名称 班级人数那一行
			Row bjNameRow = sheet.createRow(1);
			Cell bjNameFirstCell = bjNameRow.createCell(0);
			bjNameFirstCell.setCellStyle(style);
			bjNameFirstCell.setCellValue("班级");
			Row renshuNameRow = sheet.createRow(2);
			Cell renshuNameFirstCell = renshuNameRow.createCell(0);
			renshuNameFirstCell.setCellStyle(style);
			renshuNameFirstCell.setCellValue("人数");
			for (int i = 0; i < (bjCount + 1); i++) {
				Cell bjNameCell = bjNameRow.createCell(3 + i * 2);
				bjNameCell.setCellStyle(style);
				Cell renShuNameCell = renshuNameRow.createCell(3 + i * 2);
				renShuNameCell.setCellStyle(style);
				if (i < bjCount) {
					bjNameCell.setCellValue(xm.getGrade() + "(" + findAllClazz.get(i) + ")");
					renShuNameCell.setCellValue(bjStuCountMap.get(findAllClazz.get(i)));
				} else {
					bjNameCell.setCellValue(xm.getGrade());
					renShuNameCell.setCellValue(bjStuCountMap.get(grade));
				}
			}
			// 循环填充 各科目那一大块单元格数据
			for (int i = 0; i < kemuCount; i++) {
				String subid = allSubByXmidAndSchool.get(i);
				String subName = (String) selectAllSubByTableNameMap.get(subid).get("sub");

				Row avgScoreRow = sheet.createRow(3 + i * 7);
				Row maxScoreRow = sheet.createRow(3 + i * 7 + 1);
				Cell subNameCell = avgScoreRow.createCell(0);
				subNameCell.setCellStyle(style);
				subNameCell.setCellValue(subName);
				Cell avgScoreCell = avgScoreRow.createCell(1);
				avgScoreCell.setCellStyle(style);
				Cell maxScoreCell = maxScoreRow.createCell(1);
				maxScoreCell.setCellStyle(style);
				avgScoreCell.setCellValue("平均分");
				maxScoreCell.setCellValue("最高分");

				// 循环填充各班 平均分 最高分那几个单元格
				for (int j = 0; j < (bjCount + 1); j++) {
					Cell avgScoreTmpCell = avgScoreRow.createCell(3 + j * 2);
					avgScoreTmpCell.setCellStyle(style);
					Cell maxScoreTmpCell = maxScoreRow.createCell(3 + j * 2);
					maxScoreTmpCell.setCellStyle(style);
					Map<String, BjSubidPercent> map = kemuBjSubidPercent.get(subid);
					if (j < bjCount) {
						BjSubidPercent bjSubidPercent = map.get(findAllClazz.get(j));
						avgScoreTmpCell.setCellValue(bjSubidPercent.getAvg());
						maxScoreTmpCell.setCellValue(bjSubidPercent.getMax());
					} else {
						BjSubidPercent bjSubidPercent = map.get(grade);
						if (bjSubidPercent != null) {
							avgScoreTmpCell.setCellValue(bjSubidPercent.getAvg());
							maxScoreTmpCell.setCellValue(bjSubidPercent.getMax());
						}
					}
				}
				// 循环填充各个级别 各个班的单科数据
				Map<String, BjSubidPercent> levelBjZhanBiMap = kemuBjSubidPercent.get(subid);
				Map<String, SubidLevelScore> keMulevelScoreMap = levelScoreMap.get(subid);
				for (int zz = 0; zz < keMulevelScoreMap.size(); zz++) {
					String levelName = levelNumberCodeMap.get(zz + 1);
					//
					SubidLevelScore subidLevelScore = keMulevelScoreMap.get(levelName);
					if (subidLevelScore != null) {
						double maxScore = subidLevelScore.getMaxScore();
						double minScore = subidLevelScore.getMinScore();
						// 填充各个级别 分数最高分 最低分
						Row levelRow = sheet.createRow(3 + i * 7 + 1 + zz + 1);
						Cell levelNameCell = levelRow.createCell(1);
						levelNameCell.setCellStyle(style);
						levelNameCell.setCellValue(levelName);
						Cell levelMaxScoreMinScoreCell = levelRow.createCell(2);
						levelMaxScoreMinScoreCell.setCellStyle(style);
						levelMaxScoreMinScoreCell.setCellValue(maxScore + "-" + minScore);

						// 填充各个班级 在各个级别中占比
						for (int j = 0; j < (bjCount + 1); j++) {
							Cell bjStuCountTmpCell = levelRow.createCell(3 + j * 2);
							bjStuCountTmpCell.setCellStyle(style);
							Cell bjStuCountPercentTmpCell = levelRow.createCell(3 + j * 2 + 1);
							bjStuCountPercentTmpCell.setCellStyle(style);
							if (j < bjCount) {
								BjSubidPercent bjSubidPercent = levelBjZhanBiMap.get(findAllClazz.get(j));
								Map<String, BjPercent> levelBjPercentMap = bjSubidPercent.getLevelBjPercentMap();
								BjPercent bjPercent = levelBjPercentMap.get(levelName);
								bjStuCountTmpCell.setCellValue(bjPercent.getStudentCount());
								bjStuCountPercentTmpCell.setCellValue(bjPercent.getPercent());
							} else {
								BjSubidPercent bjSubidPercent = levelBjZhanBiMap.get(grade);
								if (bjSubidPercent != null) {
									Map<String, BjPercent> levelBjPercentMap = bjSubidPercent.getLevelBjPercentMap();
									BjPercent bjPercent = levelBjPercentMap.get(levelName);
									bjStuCountTmpCell.setCellValue(bjPercent.getStudentCount());
									bjStuCountPercentTmpCell.setCellValue(bjPercent.getPercent());
								}
							}
						}

					}
				}

			}
			// 填充体育平均分那一单元格
			Row sportRow = sheet.createRow(3 + (kemuCount) * 7);
			Cell sportNameCell = sportRow.createCell(0);
			sportNameCell.setCellStyle(style);
			sportNameCell.setCellValue("体育平均分");
			// 循环填充各班体育平均分那几个单元格
			for (int j = 0; j < (bjCount + 1); j++) {
				Cell sportTmpCell = sportRow.createCell(3 + j * 2);
				sportTmpCell.setCellStyle(style);
				if (j < bjCount) {
					String className = findAllClazz.get(j);
					BjSubidPercent bjSubidPercent = calcZfLevelPercent.get(className);
					sportTmpCell.setCellValue(bjSubidPercent.getSportAvgScore());
				} else {
					BjSubidPercent bjSubidPercent = calcZfLevelPercent.get(grade);
					sportTmpCell.setCellValue(bjSubidPercent.getSportAvgScore());
				}

			}
			// 填充总分那一列
			Row zongFengAvgRow = sheet.createRow(4 + (kemuCount) * 7);
			Cell zongFengNameCell = zongFengAvgRow.createCell(0);
			zongFengNameCell.setCellStyle(style);
			zongFengNameCell.setCellValue("总分");
			Row zongFengMaxRow = sheet.createRow(4 + (kemuCount) * 7 + 1);
			Cell avgNameCell = zongFengAvgRow.createCell(1);
			avgNameCell.setCellStyle(style);
			avgNameCell.setCellValue("平均分");
			Cell maxNameCell = zongFengMaxRow.createCell(1);
			maxNameCell.setCellStyle(style);
			maxNameCell.setCellValue("最高分");
			// 填充总分平均分最高分
			// 循环填充总分 平均分 最高分那几个单元格
			for (int j = 0; j < (bjCount + 1); j++) {
				Cell zongFengAvgTmpCell = zongFengAvgRow.createCell(3 + j * 2);
				zongFengAvgTmpCell.setCellStyle(style);
				Cell zongFengMaxTmpCell = zongFengMaxRow.createCell(3 + j * 2);
				zongFengMaxTmpCell.setCellStyle(style);
				if (j < bjCount) {
					String className = findAllClazz.get(j);
					BjSubidPercent bjSubidPercent = calcZfLevelPercent.get(className);
					zongFengAvgTmpCell.setCellValue(bjSubidPercent.getAvg());
					zongFengMaxTmpCell.setCellValue(bjSubidPercent.getMax());
				} else {
					BjSubidPercent bjSubidPercent = calcZfLevelPercent.get(grade);
					zongFengAvgTmpCell.setCellValue(bjSubidPercent.getAvg());
					zongFengMaxTmpCell.setCellValue(bjSubidPercent.getMax());
				}
			}
			// 循环填充总分 各个级别 各个班的总分数据
			for (int zz = 0; zz < zfLevelList.size(); zz++) {
				// A1 A2 A3 B C D E
				String levelName = zfLevelList.get(zz);
				SubidLevelScore subidLevelScore = calcZfLevelMaxScoreMinScore.get(levelName);
				if (subidLevelScore != null) {
					double maxScore = subidLevelScore.getMaxScore();
					double minScore = subidLevelScore.getMinScore();
					// 填充总分 各个级别 分数最高分 最低分
					Row levelRow = sheet.createRow(4 + (kemuCount) * 7 + 1 + 1 + zz);
					Cell levelNameCell = levelRow.createCell(1);
					levelNameCell.setCellStyle(style);
					levelNameCell.setCellValue(levelName);
					Cell levelMaxScoreMinScoreCell = levelRow.createCell(2);
					levelMaxScoreMinScoreCell.setCellStyle(style);
					levelMaxScoreMinScoreCell.setCellValue(maxScore + "-" + minScore);
					// 填充总分 各个班级 在各个级别中占比
					for (int j = 0; j < (bjCount + 1); j++) {
						Cell bjStuCountTmpCell = levelRow.createCell(3 + j * 2);
						bjStuCountTmpCell.setCellStyle(style);
						Cell bjStuCountPercentTmpCell = levelRow.createCell(3 + j * 2 + 1);
						bjStuCountPercentTmpCell.setCellStyle(style);
						if (j < bjCount) {
							BjSubidPercent bjSubidPercent = calcZfLevelPercent.get(findAllClazz.get(j));
							Map<String, BjPercent> levelBjPercentMap = bjSubidPercent.getLevelBjPercentMap();
							BjPercent bjPercent = levelBjPercentMap.get(levelName);
							bjStuCountTmpCell.setCellValue(bjPercent.getStudentCount());
							bjStuCountPercentTmpCell.setCellValue(bjPercent.getPercent());
						} else {
							BjSubidPercent bjSubidPercent = calcZfLevelPercent.get(grade);
							Map<String, BjPercent> levelBjPercentMap = bjSubidPercent.getLevelBjPercentMap();
							BjPercent bjPercent = levelBjPercentMap.get(levelName);
							bjStuCountTmpCell.setCellValue(bjPercent.getStudentCount());
							bjStuCountPercentTmpCell.setCellValue(bjPercent.getPercent());
						}
					}
				}
			}

			// 填充最底下那一行
			Row lastRow = sheet.createRow(4 + (kemuCount) * 7 + 9);
			Cell lastRowCell = lastRow.createCell(0);
			lastRowCell.setCellStyle(lastRowStyle);
			lastRowCell
					.setCellValue("说明：①A20%(排名总分A前30，100，150名，B前305)，B30％，C30％，D10％，E10％（总分E后50名）；②各档数据：左（人数）右（百分率％）");
			// 添加合并单元格的框线
			setBordersToMergedCells(sheet);
			// 设置各级别分数线那一列宽度
			sheet.setColumnWidth(2, 15 * 256);
			for (int i = 1; i < totalRows - 1; i++) {
				Row createRow = sheet.getRow(i);
				if (createRow == null) {
					createRow = sheet.createRow(i);
				}
				// 如果是人数那一行 底部加双划线
				if (i == 2) {
					for (int j = 0; j < totalCols; j++) {
						Cell cell = createRow.getCell(j);

						if (cell == null) {
							cell = createRow.createCell(j);
						}
						cell.setCellStyle(doubleBottomBorderStyle);
					}
					continue;
				}
				// 每一科目E级别 那一行下边有双划线
				boolean continueFlag = false;
				for (int index = 0; index < kemuCount; index++) {
					int doubleLineRowNumber = 3 + index * 7 + 6;
					if (i == doubleLineRowNumber) {
						for (int j = 0; j < totalCols; j++) {
							Cell cell = createRow.getCell(j);

							if (cell == null) {
								cell = createRow.createCell(j);
							}
							cell.setCellStyle(doubleBottomBorderStyle);
						}
						continueFlag = true;
					}
				}
				if (continueFlag) {
					continue;
				}

				// 体育平均分那一行下面有双划线 合并体育平均分那一单元格
				if (i == (3 + (kemuCount) * 7)) {
					for (int j = 0; j < totalCols; j++) {
						Cell cell = createRow.getCell(j);

						if (cell == null) {
							cell = createRow.createCell(j);
						}
						cell.setCellStyle(doubleBottomBorderStyle);
					}
					continue;
				}
				for (int j = 0; j < totalCols; j++) {
					Cell cell = createRow.getCell(j);

					if (cell == null) {
						cell = createRow.createCell(j);
					}
					cell.setCellStyle(style);
				}
			}
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			workbook.write(byteArrayOutputStream);

			InputStreamResource isr = new InputStreamResource(
					new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
			return isr;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@SuppressWarnings("deprecation")
	private void setBordersToMergedCells(Sheet sheet) {
		List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
		int i = 0;
		int last = mergedRegions.size();
		for (CellRangeAddress rangeAddress : mergedRegions) {
			// 第一行是标题跳过
			if (i != 0 && i < last - 1) {
				RegionUtil.setBorderTop(BorderStyle.THIN.ordinal(), rangeAddress, sheet);
				RegionUtil.setBorderBottom(BorderStyle.THIN.ordinal(), rangeAddress, sheet);
				RegionUtil.setBorderLeft(BorderStyle.THIN.ordinal(), rangeAddress, sheet);
				RegionUtil.setBorderRight(BorderStyle.THIN.ordinal(), rangeAddress, sheet);
			}
			i++;
		}
	}

	public Map<String, BjSubidPercent> calcZfLevelPercent(String school, String grade, String sportScoreTableName,
			String stuInfoTableName, String scollAllTableName, List<String> findAllClazz,
			Map<String, Integer> bjStuCountMap, Map<String, SubidLevelScore> calcZfLevelMaxScoreMinScore) {
		Map<String, BjSubidPercent> zfBjPercentMap = new HashMap<>();
		for (String tmpClass : findAllClazz) {
			BjSubidPercent bjSubidPercent = new BjSubidPercent();
			zfBjPercentMap.put(tmpClass, bjSubidPercent);
			// TODO 体育分算不算在总分中 ??
			bjSubidPercent.setBjid(tmpClass);
			Double findZfAvgBySchoolAndBj = calcZfScoreAllMapper.findZfAvgBySchoolAndBj(stuInfoTableName,
					scollAllTableName, school, tmpClass);
			Double findZfMaxBySchoolAndBj = calcZfScoreAllMapper.findZfMaxBySchoolAndBj(stuInfoTableName,
					scollAllTableName, school, tmpClass);
			bjSubidPercent.setAvg(findZfAvgBySchoolAndBj);
			bjSubidPercent.setMax(findZfMaxBySchoolAndBj);
			bjSubidPercent.setBjStudentCount(bjStuCountMap.get(tmpClass));
			// 计算各班级体育平均分
			Double findSportAvgByBj = sportScoreMapper.findSportAvgByBj(stuInfoTableName, sportScoreTableName, school,
					tmpClass);
			findSportAvgByBj = (findSportAvgByBj == null ? 0.0 : findSportAvgByBj);
			bjSubidPercent.setSportAvgScore(findSportAvgByBj);
			Map<String, BjPercent> levelBjPercentMap = new HashMap<>();
			bjSubidPercent.setLevelBjPercentMap(levelBjPercentMap);
			// 计算本班在各个级别中的人数 a1 a2 a3 b c d e
			Set<String> keySet = calcZfLevelMaxScoreMinScore.keySet();
			for (String tmpKey : keySet) {
				SubidLevelScore subidLevelScore = calcZfLevelMaxScoreMinScore.get(tmpKey);
				double maxScore = subidLevelScore.getMaxScore();
				double minScore = subidLevelScore.getMinScore();
				int findStuCountByMaxScoreAndMinScore = calcZfScoreAllMapper.findStuCountByMaxScoreAndMinScore(
						stuInfoTableName, scollAllTableName, school, tmpClass, maxScore + "", minScore + "");
				BjPercent bjPercent = new BjPercent();
				bjPercent.setStudentCount(findStuCountByMaxScoreAndMinScore);
				int allStudentCount = bjStuCountMap.get(tmpClass);
				if (findStuCountByMaxScoreAndMinScore != 0) {
					double percent = ((findStuCountByMaxScoreAndMinScore + 0.00) / allStudentCount) * 100;
					BigDecimal bg = new BigDecimal(percent);
					double f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
					bjPercent.setPercent(f1);
				} else {
					bjPercent.setPercent(new Double(0));
				}
				levelBjPercentMap.put(tmpKey, bjPercent);
			}
		}
		Double findNjZfAvgBySchoolAndBj = calcZfScoreAllMapper.findZfAvgBySchoolAndBj(stuInfoTableName,
				scollAllTableName, school, null);
		findNjZfAvgBySchoolAndBj = (findNjZfAvgBySchoolAndBj == null ? 0.0 : findNjZfAvgBySchoolAndBj);
		Double findNjZfMaxBySchoolAndBj = calcZfScoreAllMapper.findZfMaxBySchoolAndBj(stuInfoTableName,
				scollAllTableName, school, null);
		findNjZfMaxBySchoolAndBj = (findNjZfMaxBySchoolAndBj == null ? 0.0 : findNjZfMaxBySchoolAndBj);
		// 还有一个比较特殊 某一科目 计算全年级占比
		BjSubidPercent bjSubidPercent = new BjSubidPercent();
		zfBjPercentMap.put(grade, bjSubidPercent);
		bjSubidPercent.setBjid(grade);
		bjSubidPercent.setAvg(findNjZfAvgBySchoolAndBj);
		bjSubidPercent.setMax(findNjZfMaxBySchoolAndBj);
		bjSubidPercent.setBjStudentCount(bjStuCountMap.get(grade));
		// 计算年级体育平均分
		Double findSportAvgByNj = sportScoreMapper.findSportAvgByBj(stuInfoTableName, sportScoreTableName, school,
				null);
		findSportAvgByNj = (findSportAvgByNj == null ? 0.0 : findSportAvgByNj);
		bjSubidPercent.setSportAvgScore(findSportAvgByNj);
		Map<String, BjPercent> levelBjPercentMap = new HashMap<>();
		bjSubidPercent.setLevelBjPercentMap(levelBjPercentMap);
		// 计算本年级在各个级别中的人数
		Set<String> keySet = calcZfLevelMaxScoreMinScore.keySet();
		for (String tmpKey : keySet) {
			SubidLevelScore subidLevelScore = calcZfLevelMaxScoreMinScore.get(tmpKey);
			double maxScore = subidLevelScore.getMaxScore();
			double minScore = subidLevelScore.getMinScore();
			int findStuCountByMaxScoreAndMinScore = calcZfScoreAllMapper.findStuCountByMaxScoreAndMinScore(
					stuInfoTableName, scollAllTableName, school, null, maxScore + "", minScore + "");
			BjPercent bjPercent = new BjPercent();
			bjPercent.setStudentCount(findStuCountByMaxScoreAndMinScore);
			int allStudentCount = bjStuCountMap.get(grade);
			if (findStuCountByMaxScoreAndMinScore != 0) {
				double percent = ((findStuCountByMaxScoreAndMinScore + 0.00) / allStudentCount) * 100;
				BigDecimal bg = new BigDecimal(percent);
				double f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				bjPercent.setPercent(f1);
			} else {
				bjPercent.setPercent(new Double(0));
			}
			levelBjPercentMap.put(tmpKey, bjPercent);
		}
		return zfBjPercentMap;
	}

	public Map<String, SubidLevelScore> calcZfLevelMaxScoreMinScore(String school, String stuInfoTableName,
			String scollAllTableName) {
		int findZfNotNullCountBySchool = calcZfScoreAllMapper.findZfNotNullCountBySchool(stuInfoTableName,
				scollAllTableName, school);

		int percent30 = NumberUtils.percent(findZfNotNullCountBySchool, NumberUtils.zongFenLevelC);// C级
		int percent10 = NumberUtils.percent(findZfNotNullCountBySchool, NumberUtils.zongFenLevelD);// D级

		Map<String, Object> a1levelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd = calcZfScoreAllMapper
				.findMaxScoreAndMinScoreBySchoolAndStartAndEnd(stuInfoTableName, scollAllTableName, school, 1 + "",
						NumberUtils.zongFenLevelA1 + "");
		Map<String, Object> a2levelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd = calcZfScoreAllMapper
				.findMaxScoreAndMinScoreBySchoolAndStartAndEnd(stuInfoTableName, scollAllTableName, school,
						NumberUtils.zongFenLevelA1 + 1 + "", NumberUtils.zongFenLevelA2 + "");
		Map<String, Object> a3levelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd = calcZfScoreAllMapper
				.findMaxScoreAndMinScoreBySchoolAndStartAndEnd(stuInfoTableName, scollAllTableName, school,
						NumberUtils.zongFenLevelA2 + 1 + "", NumberUtils.zongFenLevelA3 + "");
		Map<String, Object> blevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd = calcZfScoreAllMapper
				.findMaxScoreAndMinScoreBySchoolAndStartAndEnd(stuInfoTableName, scollAllTableName, school,
						NumberUtils.zongFenLevelA3 + 1 + "", NumberUtils.zongFenLevelB + "");
		Map<String, Object> clevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd = calcZfScoreAllMapper
				.findMaxScoreAndMinScoreBySchoolAndStartAndEnd(stuInfoTableName, scollAllTableName, school,
						NumberUtils.zongFenLevelB + 1 + "", NumberUtils.zongFenLevelB + percent30 + "");
		Map<String, Object> dlevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd = calcZfScoreAllMapper
				.findMaxScoreAndMinScoreBySchoolAndStartAndEnd(stuInfoTableName, scollAllTableName, school,
						NumberUtils.zongFenLevelB + percent30 + 1 + "",
						NumberUtils.zongFenLevelB + percent30 + percent10 + "");
		int elevelStart = NumberUtils.zongFenLevelB + percent30 + percent10 + 1;
		int elevelEnd = NumberUtils.zongFenLevelB + percent30 + percent10 + NumberUtils.zongFenLevelE;
		Map<String, Object> elevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd = calcZfScoreAllMapper
				.findMaxScoreAndMinScoreBySchoolAndStartAndEnd(stuInfoTableName, scollAllTableName, school,
						elevelStart + "", elevelEnd + "");
		Map<String, SubidLevelScore> zfLevelMaxScoreMinScoreMap = new HashMap<>();
		if (a1levelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd != null) {
			SubidLevelScore a1SubidLevelScore = new SubidLevelScore();
			Float a1MaxScore = (Float) a1levelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd.get("maxScore");
			a1MaxScore = (a1MaxScore == null ? 0.0f : a1MaxScore);
			Float a1MinScore = (Float) a1levelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd.get("minScore");
			a1MinScore = (a1MinScore == null ? 0.0f : a1MinScore);
			a1SubidLevelScore.setMaxScore(a1MaxScore);
			a1SubidLevelScore.setMinScore(a1MinScore);
			zfLevelMaxScoreMinScoreMap.put("A1", a1SubidLevelScore);
		}
		if (a2levelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd != null) {
			SubidLevelScore a2SubidLevelScore = new SubidLevelScore();
			Float a2MaxScore = (Float) a2levelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd.get("maxScore");
			a2MaxScore = (a2MaxScore == null ? 0.0f : a2MaxScore);
			Float a2MinScore = (Float) a2levelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd.get("minScore");
			a2MinScore = (a2MinScore == null ? 0.0f : a2MinScore);
			a2SubidLevelScore.setMaxScore(a2MaxScore);
			a2SubidLevelScore.setMinScore(a2MinScore);
			
			SubidLevelScore subidLevelScore = zfLevelMaxScoreMinScoreMap.get("A1");
			if(subidLevelScore!=null) {
				double minScore = subidLevelScore.getMinScore();
				float floatValue = new Double(minScore).floatValue();
				if(a2MaxScore.floatValue()==floatValue) {
					subidLevelScore.setMinScore(floatValue+0.5);
				}
			}
			
			zfLevelMaxScoreMinScoreMap.put("A2", a2SubidLevelScore);
		}
		if (a3levelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd != null) {
			SubidLevelScore a3SubidLevelScore = new SubidLevelScore();
			Float a3MaxScore = (Float) a3levelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd.get("maxScore");
			a3MaxScore = (a3MaxScore == null ? 0.0f : a3MaxScore);
			Float a3MinScore = (Float) a3levelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd.get("minScore");
			a3MinScore = (a3MinScore == null ? 0.0f : a3MinScore);
			a3SubidLevelScore.setMaxScore(a3MaxScore);
			a3SubidLevelScore.setMinScore(a3MinScore);
			
			
			SubidLevelScore subidLevelScore = zfLevelMaxScoreMinScoreMap.get("A2");
			if(subidLevelScore!=null) {
				double minScore = subidLevelScore.getMinScore();
				float floatValue = new Double(minScore).floatValue();
				if(a3MaxScore.floatValue()==floatValue) {
					subidLevelScore.setMinScore(floatValue+0.5);
				}
			}
			
			zfLevelMaxScoreMinScoreMap.put("A3", a3SubidLevelScore);
		}
		if (blevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd != null) {
			SubidLevelScore bSubidLevelScore = new SubidLevelScore();
			Float bMaxScore = (Float) blevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd.get("maxScore");
			bMaxScore = (bMaxScore == null ? 0.0f : bMaxScore);
			Float bMinScore = (Float) blevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd.get("minScore");
			bMinScore = (bMinScore == null ? 0.0f : bMinScore);
			bSubidLevelScore.setMaxScore(bMaxScore);
			bSubidLevelScore.setMinScore(bMinScore);
			
			
			
			SubidLevelScore subidLevelScore = zfLevelMaxScoreMinScoreMap.get("A3");
			if(subidLevelScore!=null) {
				double minScore = subidLevelScore.getMinScore();
				float floatValue = new Double(minScore).floatValue();
				if(bMaxScore.floatValue()==floatValue) {
					subidLevelScore.setMinScore(floatValue+0.5);
				}
			}
			
			
			zfLevelMaxScoreMinScoreMap.put("B", bSubidLevelScore);
		}

		if (clevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd != null) {
			SubidLevelScore cSubidLevelScore = new SubidLevelScore();
			Float cMaxScore = (Float) clevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd.get("maxScore");
			cMaxScore = (cMaxScore == null ? 0.0f : cMaxScore);
			Float cMinScore = (Float) clevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd.get("minScore");
			cMinScore = (cMinScore == null ? 0.0f : cMinScore);
			cSubidLevelScore.setMaxScore(cMaxScore);
			cSubidLevelScore.setMinScore(cMinScore);
			
			
			SubidLevelScore subidLevelScore = zfLevelMaxScoreMinScoreMap.get("B");
			if(subidLevelScore!=null) {
				double minScore = subidLevelScore.getMinScore();
				float floatValue = new Double(minScore).floatValue();
				if(cMaxScore.floatValue()==floatValue) {
					subidLevelScore.setMinScore(floatValue+0.5);
				}
			}
			
			
			
			zfLevelMaxScoreMinScoreMap.put("C", cSubidLevelScore);
		}
		if (dlevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd != null) {
			SubidLevelScore dSubidLevelScore = new SubidLevelScore();
			Float dMaxScore = (Float) dlevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd.get("maxScore");
			dMaxScore = (dMaxScore == null ? 0.0f : dMaxScore);
			Float dMinScore = (Float) dlevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd.get("minScore");
			dMinScore = (dMinScore == null ? 0.0f : dMinScore);
			dSubidLevelScore.setMaxScore(dMaxScore);
			dSubidLevelScore.setMinScore(dMinScore);
			
			
			SubidLevelScore subidLevelScore = zfLevelMaxScoreMinScoreMap.get("C");
			if(subidLevelScore!=null) {
				double minScore = subidLevelScore.getMinScore();
				float floatValue = new Double(minScore).floatValue();
				if(dMaxScore.floatValue()==floatValue) {
					subidLevelScore.setMinScore(floatValue+0.5);
				}
			}
			
			
			
			zfLevelMaxScoreMinScoreMap.put("D", dSubidLevelScore);
		}
		if (elevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd != null) {
			SubidLevelScore eSubidLevelScore = new SubidLevelScore();
			Float eMaxScore = (Float) elevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd.get("maxScore");
			eMaxScore = (eMaxScore == null ? 0.0f : eMaxScore);
			Float eMinScore = (Float) elevelFindMaxScoreAndMinScoreBySchoolAndStartAndEnd.get("minScore");
			eMinScore = (eMinScore == null ? 0.0f : eMinScore);
			eSubidLevelScore.setMaxScore(eMaxScore);
			eSubidLevelScore.setMinScore(eMinScore);
			
			
			SubidLevelScore subidLevelScore = zfLevelMaxScoreMinScoreMap.get("D");
			if(subidLevelScore!=null) {
				double minScore = subidLevelScore.getMinScore();
				float floatValue = new Double(minScore).floatValue();
				if(eMaxScore.floatValue()==floatValue) {
					subidLevelScore.setMinScore(floatValue+0.5);
				}
			}
			
			
			
			zfLevelMaxScoreMinScoreMap.put("E", eSubidLevelScore);
		}
		return zfLevelMaxScoreMinScoreMap;
	}

	public Map<String, Map<String, SubidLevelScore>> getLevelMaxScoreAndMinScore(String school, String stuInfoTableName,
			String scollAllTableName, List<String> allSubByXmidAndSchool) {
		Map<String, Map<String, SubidLevelScore>> levelScoreMap = new HashMap<>();
		for (String subid : allSubByXmidAndSchool) {
			Map<String, SubidLevelScore> tmpLevelMaxScoreMinScoreMap = new HashMap<>();
			levelScoreMap.put(subid, tmpLevelMaxScoreMinScoreMap);
			// 这一科分数不为null 考生有多少个
			int findStuCountBySchoolKemuZiduan = scoreAllMapper.findStuCountBySchoolKemuZiduan(stuInfoTableName,
					scollAllTableName, school, "x" + subid);
			//
			if (findStuCountBySchoolKemuZiduan > 0) {
				List<Integer> splitNumber = NumberUtils.splitNumber(findStuCountBySchoolKemuZiduan);
				int i = 1;
				int passedStudent = 0;

				for (Integer stuNum : splitNumber) {
					if(stuNum==0) {
						break;
					}
					// 这一级别的考生多少个
					Map<String, Object> selectNjMaxScoreMinScoreBySchoolAndKemuziduanAndStartAndEnd = scoreAllMapper
							.selectNjMaxScoreMinScoreBySchoolAndKemuziduanAndStartAndEnd(stuInfoTableName,
									scollAllTableName, school, "x" + subid, passedStudent + 1 + "",
									passedStudent + stuNum + "");
					Float maxScore = 0.0f;
					Float minScore = 0.0f;
					if (selectNjMaxScoreMinScoreBySchoolAndKemuziduanAndStartAndEnd != null) {
						maxScore = (Float) selectNjMaxScoreMinScoreBySchoolAndKemuziduanAndStartAndEnd.get("maxScore");
						minScore = (Float) selectNjMaxScoreMinScoreBySchoolAndKemuziduanAndStartAndEnd.get("minScore");
						
						if(i!=1) {
							SubidLevelScore subidLevelScore = tmpLevelMaxScoreMinScoreMap.get(levelNumberCodeMap.get(i-1));
							if(subidLevelScore!=null) {
								double minScore2 = subidLevelScore.getMinScore();
								if(maxScore==minScore2) {
									subidLevelScore.setMinScore(minScore2+0.5);
								}
							}
						}
					}
					// 算出来之后存一下
					SubidLevelScore subidLevelScore = new SubidLevelScore();
					subidLevelScore.setMaxScore(maxScore);
					subidLevelScore.setMinScore(minScore);
					tmpLevelMaxScoreMinScoreMap.put(levelNumberCodeMap.get(i), subidLevelScore);

					passedStudent = passedStudent + stuNum;
					i++;
				}
			}
		}
		return levelScoreMap;
	}

	public Map<String, Map<String, BjSubidPercent>> getBjPercentOnLevel(String school, String grade,
			String stuInfoTableName, String scollAllTableName, String sportScoreTableName, String subExtTableName,
			List<String> findAllClazz, Map<String, Integer> bjStuCountMap,
			Map<String, Map<String, SubidLevelScore>> levelScoreMap) {
		Map<String, Map<String, BjSubidPercent>> bjsubIdPercentMap = new HashMap<>();
		Set<String> keySet = levelScoreMap.keySet();
		if (keySet.size() > 0) {
			for (String subid : keySet) {
				Map<String, BjSubidPercent> bjAllLevelMap = new HashMap<>();
				bjsubIdPercentMap.put(subid, bjAllLevelMap);
				for (String tmpClass : findAllClazz) {
					BjSubidPercent bjSubidPercent = new BjSubidPercent();
					bjAllLevelMap.put(tmpClass, bjSubidPercent);

					// 计算各班级体育平均分
					// double findSportAvgByBj = sportScoreMapper.findSportAvgByBj(stuInfoTableName,
					// sportScoreTableName, school, tmpClass);
					// bjSubidPercent.setSportAvgScore(findSportAvgByBj);
					bjSubidPercent.setBjid(tmpClass);
					Double findBjAvgByXmidAndSchoolAndbjAndSubid = scoreAllMapper.findBjAvgByXmidAndSchoolAndbjAndSubid(
							stuInfoTableName, scollAllTableName, school, tmpClass, "x" + subid);
					findBjAvgByXmidAndSchoolAndbjAndSubid = (findBjAvgByXmidAndSchoolAndbjAndSubid == null ? 0.0
							: findBjAvgByXmidAndSchoolAndbjAndSubid);
					Double findBjMaxByXmidAndSchoolAndbjAndSubid = scoreAllMapper.findBjMaxByXmidAndSchoolAndbjAndSubid(
							stuInfoTableName, scollAllTableName, school, tmpClass, "x" + subid);
					findBjMaxByXmidAndSchoolAndbjAndSubid = (findBjMaxByXmidAndSchoolAndbjAndSubid == null ? 0.0
							: findBjMaxByXmidAndSchoolAndbjAndSubid);
					bjSubidPercent.setAvg(findBjAvgByXmidAndSchoolAndbjAndSubid);
					bjSubidPercent.setBjStudentCount(bjStuCountMap.get(tmpClass));
					bjSubidPercent.setMax(findBjMaxByXmidAndSchoolAndbjAndSubid);
					Map<String, BjPercent> levelBjPercentMap = new HashMap<>();
					bjSubidPercent.setLevelBjPercentMap(levelBjPercentMap);
					// 计算本班在各个级别中的人数
					Map<String, SubidLevelScore> map = levelScoreMap.get(subid);
					for (String tmpKey : map.keySet()) {
						SubidLevelScore subidLevelScore = map.get(tmpKey);
						double maxScore = subidLevelScore.getMaxScore();
						double minScore = subidLevelScore.getMinScore();
						int selectStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan = scoreAllMapper
								.selectStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan(stuInfoTableName,
										scollAllTableName, school, tmpClass, "x" + subid, maxScore + "", minScore + "");
						BjPercent bjPercent = new BjPercent();
						bjPercent.setStudentCount(selectStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan);
						int allStudentCount = bjStuCountMap.get(tmpClass);
						if (selectStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan != 0) {
							double percent = ((selectStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan + 0.00)
									/ allStudentCount) * 100;
							BigDecimal bg = new BigDecimal(percent);
							double f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
							bjPercent.setPercent(f1);
						} else {
							bjPercent.setPercent(new Double(0));
						}
						levelBjPercentMap.put(tmpKey, bjPercent);
					}
				}

				Map<String, Object> selectNjAvgAndMaxScore = subExtMapper.selectNjAvgAndMaxScore(subExtTableName,
						school, subid);
				BjSubidPercent bjSubidPercent = new BjSubidPercent();
				bjAllLevelMap.put(grade, bjSubidPercent);
				if (selectNjAvgAndMaxScore != null) {
					// 还有一个比较特殊 某一科目 计算全年级占比
					// 计算各班级体育平均分
					// double findSportAvgj = sportScoreMapper.findSportAvg(stuInfoTableName,
					// sportScoreTableName, school);
					// bjSubidPercent.setSportAvgScore(findSportAvgj);
					bjSubidPercent.setBjid(grade);
					BigDecimal njAvgScore = (BigDecimal) selectNjAvgAndMaxScore.get("tavg");
					BigDecimal njMaxScore = (BigDecimal) selectNjAvgAndMaxScore.get("tmax");
					double findNjAvgByXmidAndSchoolAndbjAndSubid = njAvgScore == null ? 0.0 : njAvgScore.doubleValue();
					double findNjMaxByXmidAndSchoolAndbjAndSubid = njMaxScore == null ? 0.0 : njMaxScore.doubleValue();
					bjSubidPercent.setAvg(findNjAvgByXmidAndSchoolAndbjAndSubid);
					bjSubidPercent.setMax(findNjMaxByXmidAndSchoolAndbjAndSubid);
				} else {
					// 计算一下本学校 本年级 这一科目的平均分和最高分
					Double findNjAvgByXmidAndSchoolAndbjAndSubid = scoreAllMapper.findBjAvgByXmidAndSchoolAndbjAndSubid(
							stuInfoTableName, scollAllTableName, school, null, "x" + subid);
					findNjAvgByXmidAndSchoolAndbjAndSubid = (findNjAvgByXmidAndSchoolAndbjAndSubid == null ? 0.0
							: findNjAvgByXmidAndSchoolAndbjAndSubid);
					Double findNjMaxByXmidAndSchoolAndbjAndSubid = scoreAllMapper.findBjMaxByXmidAndSchoolAndbjAndSubid(
							stuInfoTableName, scollAllTableName, school, null, "x" + subid);
					findNjMaxByXmidAndSchoolAndbjAndSubid = (findNjMaxByXmidAndSchoolAndbjAndSubid == null ? 0.0
							: findNjMaxByXmidAndSchoolAndbjAndSubid);
					bjSubidPercent.setAvg(findNjAvgByXmidAndSchoolAndbjAndSubid);
					bjSubidPercent.setMax(findNjMaxByXmidAndSchoolAndbjAndSubid);
				}
				bjSubidPercent.setBjStudentCount(bjStuCountMap.get(grade));
				Map<String, BjPercent> levelBjPercentMap = new HashMap<>();
				bjSubidPercent.setLevelBjPercentMap(levelBjPercentMap);
				// 计算本年级在各个级别中的人数
				Map<String, SubidLevelScore> map = levelScoreMap.get(subid);
				for (String tmpKey : map.keySet()) {
					SubidLevelScore subidLevelScore = map.get(tmpKey);
					double maxScore = subidLevelScore.getMaxScore();
					double minScore = subidLevelScore.getMinScore();
					int selectNjStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan = scoreAllMapper
							.selectNjStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan(stuInfoTableName,
									scollAllTableName, school, "x" + subid, maxScore + "", minScore + "");
					BjPercent bjPercent = new BjPercent();
					bjPercent.setStudentCount(selectNjStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan);
					int allStudentCount = bjStuCountMap.get(grade);
					if (selectNjStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan != 0) {
						double percent = ((selectNjStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan + 0.00)
								/ allStudentCount) * 100;
						BigDecimal bg = new BigDecimal(percent);
						double f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
						bjPercent.setPercent(f1);
					} else {
						bjPercent.setPercent(new Double(0));
					}
					levelBjPercentMap.put(tmpKey, bjPercent);
				}

			}

		}
		return bjsubIdPercentMap;
	}

	/**
	 * 查询一个学校在一次项目（考试）中有多少科目考试了
	 */
	public List<String> getAllSubByXmidAndSchool(String xmid, String school) {
		List<String> result = new ArrayList<>();

		String stuInfoTableName = "T" + xmid + Constants.STUINFO_SUFFIX;
		String scollAllTableName = "T" + xmid + Constants.SCOREAll_SUFFIX;
		String subTableName = "T" + xmid + Constants.SUB_SUFFIX;

		List<Map<String, Object>> selectAllSubByTableName = subMapper.selectAllSubByTableName(subTableName);
		for (Map<String, Object> tmpMap : selectAllSubByTableName) {
			String subid = (String) tmpMap.get("subid");
			// 查询这个subid 在这个学校有学生考试了吗？ 如果有那么就返回
			int selectStuCountBySchoolAndKemuZiduan = scoreAllMapper
					.selectStuCountBySchoolAndKemuZiduan(stuInfoTableName, scollAllTableName, school, "x" + subid);
			if (selectStuCountBySchoolAndKemuZiduan > 0) {
				result.add(subid);
			}

		}
		return result;
	}

	private static Map<Integer, String> levelNumberCodeMap = new HashMap<>();
	private static List<String> zfLevelList = new ArrayList<>();
	static {
		levelNumberCodeMap.put(1, "A");
		levelNumberCodeMap.put(2, "B");
		levelNumberCodeMap.put(3, "C");
		levelNumberCodeMap.put(4, "D");
		levelNumberCodeMap.put(5, "E");
		zfLevelList.add("A1");
		zfLevelList.add("A2");
		zfLevelList.add("A3");
		zfLevelList.add("B");
		zfLevelList.add("C");
		zfLevelList.add("D");
		zfLevelList.add("E");
	}

}
