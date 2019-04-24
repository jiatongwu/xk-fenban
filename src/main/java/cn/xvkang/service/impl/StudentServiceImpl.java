package cn.xvkang.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.github.pagehelper.PageHelper;

import cn.xvkang.dao.StudentCustomMapper;
import cn.xvkang.dao.StudentMapper;
import cn.xvkang.dao.UserCustomMapper;
import cn.xvkang.dao.UserMapper;
import cn.xvkang.dto.student.StudentExtendDto;
import cn.xvkang.entity.Student;
import cn.xvkang.entity.StudentExample;
import cn.xvkang.entity.User;
import cn.xvkang.entity.UserExample;
import cn.xvkang.service.StudentService;
import cn.xvkang.utils.IdcardValidatorUtils;
import cn.xvkang.utils.PhoneFormatCheckUtils;
import cn.xvkang.utils.WordExcelUtils;
import cn.xvkang.utils.page.Page;
import cn.xvkang.utils.page.PageImpl;
import cn.xvkang.utils.page.PageRequest;

@Service
public class StudentServiceImpl implements StudentService {
	@Autowired
	private StudentMapper studentMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private StudentCustomMapper studentCustomMapper;
	@Autowired
	private UserCustomMapper userCustomMapper;
	@Autowired
	private TransactionTemplate transactionTemplate;

	@Override
	public PageImpl<Student> selectAll(Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		List<Student> selectByExample = studentMapper.selectByExample(null);
		PageImpl<Student> pageImpl = new PageImpl<Student>(selectByExample, new PageRequest(pageNum - 1, pageSize),
				((com.github.pagehelper.Page<Student>) selectByExample).getTotal());
		return pageImpl;
	}

	@Override
	public int deleteById(Integer id) {
		// 删除用户表和学生表数据
		// TODO 目前没有其他表数据需要删除
		Student selectByPrimaryKey = studentMapper.selectByPrimaryKey(id);
		if (selectByPrimaryKey != null) {
			return transactionTemplate.execute(new TransactionCallback<Integer>() {
				@Override
				public Integer doInTransaction(TransactionStatus status) {
					studentMapper.deleteByPrimaryKey(id);
					String phone = selectByPrimaryKey.getPhone();
					UserExample userExample = new UserExample();
					userExample.createCriteria().andUsernameEqualTo(phone);
					userMapper.deleteByExample(userExample);
					return 1;
				}
			});
		}
		return 0;
	}

	@Override
	public int add(Student p) {
		return saveOrUpdateOneStudent(p);

	}

	@Override
	public Student findById(Integer id) {
		return studentMapper.selectByPrimaryKey(id);
	}

	@Override
	public Page<StudentExtendDto> table(String name, String idcard, String phone, String kemuzuheId,String isSelectKemuzuhe, int pageNum,
			int pageSize) {

		PageHelper.startPage(pageNum, pageSize);
		if(StringUtils.isNotBlank(name)) {
			name=("%"+name+"%");
		}
		if(StringUtils.isNotBlank(idcard)) {
			name=("%"+idcard+"%");
		}
		if(StringUtils.isNotBlank(phone)) {
			name=("%"+phone+"%");
		}
		
		List<StudentExtendDto> selectByExample = studentCustomMapper.findAll(name, idcard, phone, kemuzuheId,isSelectKemuzuhe);

		PageImpl<StudentExtendDto> pageImpl = new PageImpl<StudentExtendDto>(selectByExample,
				new PageRequest(pageNum - 1, pageSize),
				((com.github.pagehelper.Page<StudentExtendDto>) selectByExample).getTotal());
		return pageImpl;
	}

	@Override
	public Map<String, Object> importExcel(String deleteOrigin, InputStream inputStream) {

		boolean deleteOriginStudentBoolean = new Boolean(deleteOrigin);
		// Set<String> allBiyeSchoolName = schoolDao.getAllBiyeSchoolName();

		if (deleteOriginStudentBoolean) {
			deleteAllStudent();
		}

		List<String> errorMessages = new ArrayList<String>();

		List<Student> datas = new ArrayList<>();

		// List<String> messageList = new ArrayList<>();
		Set<String> tmpIdcardUnique = new HashSet<>();
		Set<String> tmpPhoneUnique = new HashSet<>();
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
				XSSFCell xujiCell = row.getCell(0);
				String xujiCellString = WordExcelUtils.getCellStringValue(xujiCell);
				if (StringUtils.isBlank(xujiCellString)) {
					// 最后一行了，终止处理。
					break;
				}
				Student excelStudent = new Student();

				XSSFCell nameCell = row.getCell(1);
				// XSSFCell genderCell = row.getCell(2);
				XSSFCell idcardCell = row.getCell(3);
//				XSSFCell nianjiNameCell = row.getCell(4);
//				XSSFCell banjiNameCell = row.getCell(5);
//				XSSFCell jibuNameCell = row.getCell(6);
//				XSSFCell banjiCodeCell = row.getCell(7);
//				XSSFCell currentAddressCell = row.getCell(8);
//				XSSFCell contactAddressCell = row.getCell(9);
//				XSSFCell contactPhoneCell = row.getCell(10);
//				XSSFCell homeAddressCell = row.getCell(11);
//				XSSFCell fatherPhoneCell = row.getCell(12);
//				XSSFCell motherPhoneCell = row.getCell(13);
//				XSSFCell yunxiaoyuanNameCell = row.getCell(14);
//				XSSFCell yunxiaoyuanBanjiCell = row.getCell(15);
				XSSFCell uniqueCell = row.getCell(4);
				XSSFCell yunxiaoyuanPhoneCell = row.getCell(5);
				XSSFCell gonfeiZifeiCell = row.getCell(6);
				XSSFCell zizhushenCell = row.getCell(7);
				XSSFCell jiandanlikahuCell = row.getCell(8);

				String nameCellString = WordExcelUtils.getCellStringValue(nameCell);
				// String genderCellString = WordExcelUtils.getCellStringValue(genderCell);
				String idcardCelltString = WordExcelUtils.getCellStringValue(idcardCell);
//				String nianjiNameCellString = WordExcelUtils.getCellStringValue(nianjiNameCell);
//				String banjiNameCellString = WordExcelUtils.getCellStringValue(banjiNameCell);
//				String jibuNameCellString = WordExcelUtils.getCellStringValue(jibuNameCell);
//				String banjiCodeCellString = WordExcelUtils.getCellStringValue(banjiCodeCell);
//				String currentAddressCellString = WordExcelUtils.getCellStringValue(currentAddressCell);
//				String contactAddressCellString = WordExcelUtils.getCellStringValue(contactAddressCell);
//				String contactPhoneCellString = WordExcelUtils.getCellStringValue(contactPhoneCell);
//				String homeAddressCellString = WordExcelUtils.getCellStringValue(homeAddressCell);
//				String fatherPhoneCellString = WordExcelUtils.getCellStringValue(fatherPhoneCell);
//				String motherPhoneCellString = WordExcelUtils.getCellStringValue(motherPhoneCell);
//				String yunxiaoyuanNameCellString = WordExcelUtils.getCellStringValue(yunxiaoyuanNameCell);
//				String yunxiaoyuanBanjiCellString = WordExcelUtils.getCellStringValue(yunxiaoyuanBanjiCell);
				String uniqueCellString = WordExcelUtils.getCellStringValue(uniqueCell);
				String yunxiaoyuanPhoneCellString = WordExcelUtils.getCellStringValue(yunxiaoyuanPhoneCell);
				String gonfeiZifeiCellString = WordExcelUtils.getCellStringValue(gonfeiZifeiCell);
				String zizhushenCellString = WordExcelUtils.getCellStringValue(zizhushenCell);
				String jiandanlikahuCellString = WordExcelUtils.getCellStringValue(jiandanlikahuCell);

				boolean isDataFormatOk = true;

				StringBuilder errorMsgStringBuilder = new StringBuilder();
				errorMsgStringBuilder.append("第" + (i + 1) + "行：");

				excelStudent.setXuejihao(xujiCellString);
				excelStudent.setName(nameCellString);
//				excelStudent.setNijiName(nianjiNameCellString);
//				excelStudent.setBanjiName(banjiNameCellString);
//				excelStudent.setJibuName(jibuNameCellString);
//				excelStudent.setBanjiCode(banjiCodeCellString);
//				excelStudent.setCurrentAddress(currentAddressCellString);
//				excelStudent.setContactAddress(contactAddressCellString);
//				excelStudent.setContactTelphone(contactPhoneCellString);
//				excelStudent.setHomeAddress(homeAddressCellString);
//				excelStudent.setFatherPhone(fatherPhoneCellString);
//				excelStudent.setMotherPhone(motherPhoneCellString);
//				excelStudent.setYunxiaoyuanName(yunxiaoyuanNameCellString);
//				excelStudent.setYunxiaoyuanBanjicode(yunxiaoyuanBanjiCellString);
				excelStudent.setUniqueCode(uniqueCellString);
				excelStudent.setYunxiaoyuanPhone(yunxiaoyuanPhoneCellString);
				excelStudent.setGongfeiZifei(gonfeiZifeiCellString);
				excelStudent.setZizhusheng(zizhushenCellString);
				excelStudent.setJiandanglikahu(jiandanlikahuCellString);
				excelStudent.setPhone(yunxiaoyuanPhoneCellString);
				excelStudent.setIdcard(idcardCelltString);

				// 判断身份证格式是否正确
				if (IdcardValidatorUtils.isIdcardFormat(idcardCelltString)) {
					// 校验身份证号是否唯一
					if (StringUtils.isNotBlank(idcardCelltString)) {
						boolean containsIdcard = tmpIdcardUnique.contains(idcardCelltString);
						// Record findByIdcard = studentDao.findByIdcard(cell1String);
						if ((containsIdcard) || (idcardCelltString.length() < 18)) {
							isDataFormatOk = false;
							errorMsgStringBuilder.append(" 身份证号已存在 ");
						} else {
							// 身份证号格式正确并且 数据库不存在此身份证号
							// 从身份证中提取出出生年月 年龄 性别

							Map<String, Object> checkIdcard = IdcardValidatorUtils.checkIdcard(idcardCelltString);
							String gender = (String) checkIdcard.get("gender");
							excelStudent.setGender(Integer.parseInt(gender));
							tmpIdcardUnique.add(idcardCelltString);

						}

					} else {
						isDataFormatOk = false;
						errorMsgStringBuilder.append(" 身份证号不能为空 ");
					}
				} else {
					isDataFormatOk = false;
					errorMsgStringBuilder.append(" 身份证号格式不正确 ");
				}

				XSSFCell cell2 = row.getCell(2);
				String cell2String = WordExcelUtils.getCellStringValue(cell2);
				if (StringUtils.isNotBlank(cell2String)) {
					cell2String = cell2String.trim();
				}

				if (PhoneFormatCheckUtils.isChinaPhoneLegal(yunxiaoyuanPhoneCellString)) {
					// 校验身份证号是否唯一
					if (StringUtils.isNotBlank(yunxiaoyuanPhoneCellString)) {
						boolean containsPhone = tmpPhoneUnique.contains(yunxiaoyuanPhoneCellString);
						// Record findByIdcard = studentDao.findByIdcard(cell1String);
						if (containsPhone) {
							isDataFormatOk = false;
							errorMsgStringBuilder.append(" 手机号已存在 ");
						} else {
							// 身份证号格式正确并且 数据库不存在此身份证号
							// 从身份证中提取出出生年月 年龄 性别
							Student findByPhone = findByPhone(yunxiaoyuanPhoneCellString);
							if (findByPhone != null) {
								String idcard = findByPhone.getIdcard();
								if (!idcard.equals(idcardCelltString)) {
									isDataFormatOk = false;
									errorMsgStringBuilder.append(" 手机号已存在 ");
								} else {
									tmpPhoneUnique.add(yunxiaoyuanPhoneCellString);
								}
							} else {
								tmpPhoneUnique.add(yunxiaoyuanPhoneCellString);
							}
						}

					} else {
						isDataFormatOk = false;
						errorMsgStringBuilder.append(" 身份证号不能为空 ");
					}
				} else {
					isDataFormatOk = false;
					errorMsgStringBuilder.append(" 手机号格式不正确 ");
				}

				if (isDataFormatOk) {
					datas.add(excelStudent);
				} else {
					// 有格式不正确的行 ，提示给用户
					errorMessages.add(errorMsgStringBuilder.toString());
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 批量插入学生信息
		List<Map<String, String>> batchImportExcelStudent = batchImportExcelStudent(datas);
		successCount = datas.size() - batchImportExcelStudent.size();
		errorCount = errorMessages.size() + batchImportExcelStudent.size();

		for (Map<String, String> tmp : batchImportExcelStudent) {
			String name = tmp.get("name");
			String idcard = tmp.get("idcard");
			errorMessages.add("学生：" + name + ",身份证号" + idcard + " 导入失败，请联系管理员。");
		}
		Map<String, Object> result = new HashMap<>();
		result.put("state", "ok");
		result.put("msg", "导入成功" + successCount + "个学生");
		result.put("errorMessages", errorMessages);
		result.put("successCount", successCount);
		result.put("errorCount", errorCount);
		return result;
	}

	private List<Map<String, String>> batchImportExcelStudent(List<Student> datas) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (Student s : datas) {
			int saveOrUpdateOneStudent = 0;
			try {
				saveOrUpdateOneStudent = saveOrUpdateOneStudent(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (saveOrUpdateOneStudent == 0) {
				Map<String, String> errorStudent = new HashMap<String, String>();
				errorStudent.put("name", s.getName());
				errorStudent.put("idcard", s.getIdcard());
				result.add(errorStudent);
			}
		}

		return result;
	}

	private void deleteAllStudent() {
		transactionTemplate.execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(TransactionStatus status) {
				// 目前没有用户角色关联数据 只删除用户就可以了
				userCustomMapper.deleteStudentAllUser();
				studentCustomMapper.deleteAllStudent();
				return null;
			}
		});
	}

	private int saveOrUpdateOneStudent(Student s) {
		if (s != null) {
			return transactionTemplate.execute(new TransactionCallback<Integer>() {
				@Override
				public Integer doInTransaction(TransactionStatus status) {
					// 目前没有用户角色关联数据 只向用户表中添加一条数据即可
					StudentExample example = new StudentExample();
					example.createCriteria().andIdcardEqualTo(s.getIdcard());
					List<Student> selectByExample = studentMapper.selectByExample(example);
					if (selectByExample.size() == 1) {
						// 更新
						Student student = selectByExample.get(0);
						s.setId(student.getId());
						String oldUsername = student.getPhone();
						if (!oldUsername.equals(s.getPhone())) {
							// 更新user表username
							UserExample userExample = new UserExample();
							userExample.createCriteria().andUsernameEqualTo(oldUsername);
							List<User> tmpUsers = userMapper.selectByExample(userExample);
							if (tmpUsers.size() == 1) {
								User oldUser = tmpUsers.get(0);
								oldUser.setUsername(s.getPhone());
								userMapper.updateByPrimaryKey(oldUser);
							}
						}
						studentMapper.updateByPrimaryKeySelective(s);
						return 1;
					} else if (selectByExample.size() == 0) {
						// 新增student
						String idcard2 = s.getIdcard();
						Map<String, Object> checkIdcard = IdcardValidatorUtils.checkIdcard(idcard2);
						String gender = (String) checkIdcard.get("gender");
						s.setGender(Integer.parseInt(gender));
						studentMapper.insert(s);
						// 新增user
						User studentUser = new User();
						studentUser.setStudentid(s.getId());
						String salt = UUID.randomUUID().toString();
						studentUser.setSalt(salt);
						studentUser.setUsername(s.getPhone());
						String idcard = s.getIdcard();
						String passwordSource = idcard.substring(idcard.length() - 8, idcard.length());
						String md5Hex = DigestUtils.md5Hex(passwordSource + salt);
						studentUser.setPassword(md5Hex);
						userMapper.insert(studentUser);
						return 1;
					}
					return 0;
				}
			});
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKeySelective(Student s) {
		int updateByPrimaryKeySelective = studentMapper.updateByPrimaryKeySelective(s);
		return updateByPrimaryKeySelective;
	}

	@Override
	public int edit(Student s) {
		Student selectByPrimaryKey = studentMapper.selectByPrimaryKey(s.getId());
		return transactionTemplate.execute(new TransactionCallback<Integer>() {
			@Override
			public Integer doInTransaction(TransactionStatus status) {
				// 更新
				String oldUsername = selectByPrimaryKey.getPhone();
				if (!oldUsername.equals(s.getPhone())) {
					// 更新user表username
					UserExample userExample = new UserExample();
					userExample.createCriteria().andUsernameEqualTo(oldUsername);
					List<User> tmpUsers = userMapper.selectByExample(userExample);
					if (tmpUsers.size() == 1) {
						User oldUser = tmpUsers.get(0);
						oldUser.setUsername(s.getPhone());
						userMapper.updateByPrimaryKey(oldUser);
					}
				}
				String idcard2 = s.getIdcard();
				Map<String, Object> checkIdcard = IdcardValidatorUtils.checkIdcard(idcard2);
				String gender = (String) checkIdcard.get("gender");
				s.setGender(Integer.parseInt(gender));
				studentMapper.updateByPrimaryKeySelective(s);
				return 1;

			}
		});
	}

	@Override
	public Student findByIdcard(String idcard) {
		StudentExample example = new StudentExample();
		example.createCriteria().andIdcardEqualTo(idcard);
		List<Student> selectByExample = studentMapper.selectByExample(example);
		if (selectByExample.size() == 1) {
			return selectByExample.get(0);
		}
		return null;
	}

	public Student findByPhone(String phone) {
		StudentExample example = new StudentExample();
		example.createCriteria().andPhoneEqualTo(phone);
		List<Student> selectByExample = studentMapper.selectByExample(example);
		if (selectByExample.size() == 1) {
			return selectByExample.get(0);
		}
		return null;
	}

}
