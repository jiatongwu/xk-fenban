var contextPath=$("#contextPath").val();
function nation(form) {
	$.getJSON("/student/nation",
		function (data) {
			var optionstring = "";
			//console.log(data);
			$.each(data, function (i, item) {
				optionstring += "<option value=\"" + item.id + "\" >"
					+ item.name + "</option>";
			});
			$("#nation").html(
				'<option value=""></option>' + optionstring);
			//console.log(optionstring)
			//console.log("ksdfj");
			form.render('select'); //这个很重要
		});
}
function studentType(form) {
	$.getJSON("/student/studentType",
		function (data) {
			var optionstring = "";
			//console.log(data);
			$.each(data, function (i, item) {
				optionstring += "<option value=\"" + item.id + "\" >"
					+ item.name + "</option>";
			});
			$("#studentType").html(
				'<option value=""></option>' + optionstring);
			//console.log(optionstring)
			//console.log("ksdfj");
			form.render('select'); //这个很重要
		});
}
function biyeSchool(form) {
	$.getJSON("/student/allBiyeSchool",
		function (data) {
			var optionstring = "";
			//console.log(data);
			$.each(data, function (i, item) {
				optionstring += "<option value=\"" + item.id + "\" >"
					+ item.name + "</option>";
			});
			$("#biyeSchool").html(
				'<option value=""></option>' + optionstring);
			//console.log(optionstring)
			//console.log("ksdfj");
			form.render('select'); //这个很重要
		});
}





function nationEdit(form) {
	var nationIdHiddenValue = $("#nationHiddenId").val();
	$.getJSON("/student/nation",
		function (data) {
			var optionstring = "";
			//console.log(data);
			$.each(data, function (i, item) {
				if (item.id == nationIdHiddenValue) {
					optionstring += "<option value=\"" + item.id + "\" selected=\"selected\" >" + item.name + "</option>";
				} else {
					optionstring += "<option value=\"" + item.id + "\" >" + item.name + "</option>";
				}
			});
			$("#nationEdit").html(
				'<option value=""></option>' + optionstring);
			//console.log(optionstring)
			//console.log("ksdfj");
			form.render('select'); //这个很重要
		});
}
function studentTypeEdit(form) {
	var studentTypeIdHiddenValue = $("#studentTypeHiddenId").val();
	$.getJSON("/student/studentType",
		function (data) {
			var optionstring = "";
			//console.log(data);
			$.each(data, function (i, item) {
				if (item.id == studentTypeIdHiddenValue) {
					optionstring += "<option value=\"" + item.id + "\"  selected=\"selected\">" + item.name + "</option>";
				} else {
					optionstring += "<option value=\"" + item.id + "\" >" + item.name + "</option>";
				}


			});
			$("#studentTypeEdit").html(
				'<option value=""></option>' + optionstring);
			//console.log(optionstring)
			//console.log("ksdfj");
			form.render('select'); //这个很重要
		});
}
function biyeSchoolEdit(form) {
	var biyeSchoolIdHiddenValue = $("#biyeSchoolHiddenId").val();
	$.getJSON("/student/allBiyeSchool",
		function (data) {
			var optionstring = "";
			//console.log(data);
			$.each(data, function (i, item) {
				if (biyeSchoolIdHiddenValue == item.id) {
					optionstring += "<option value=\"" + item.id + "\" selected=\"selected\">" + item.name + "</option>";
				} else {
					optionstring += "<option value=\"" + item.id + "\" >" + item.name + "</option>";
				}

			});
			$("#biyeSchoolEdit").html(
				'<option value=""></option>' + optionstring);
			//console.log(optionstring)
			//console.log("ksdfj");
			form.render('select'); //这个很重要
		});
}


/**身份证校验规则*/
function checkIdcard(ID) {
	if (typeof ID !== 'string') return '非法字符串';
	var city = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江 ", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北 ", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏 ", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外" };
	var birthday = ID.substr(6, 4) + '/' + Number(ID.substr(10, 2)) + '/' + Number(ID.substr(12, 2));
	var d = new Date(birthday);
	var newBirthday = d.getFullYear() + '/' + Number(d.getMonth() + 1) + '/' + Number(d.getDate());
	var currentTime = new Date().getTime();
	var time = d.getTime();
	var arrInt = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
	var arrCh = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
	var sum = 0, i, residue;

	if (!/^\d{17}(\d|x)$/i.test(ID)) return '非法身份证';
	if (city[ID.substr(0, 2)] === undefined) return "非法地区";
	if (time >= currentTime || birthday !== newBirthday) return '非法生日';
	for (i = 0; i < 17; i++) {
		sum += ID.substr(i, 1) * arrInt[i];
	}
	residue = arrCh[sum % 11];
	if (residue !== ID.substr(17, 1)) return '非法身份证哦';

	//return city[ID.substr(0,2)]+","+birthday+","+(ID.substr(16,1)%2?" 男":"女")
	return "ok";
}

function tableReload(table, data) {
	table.reload('table', {
		where: data.field,
		done: function (res, curr, count) {
			layer.closeAll('loading');
			if (res.state == 'over') {
				jumpToUrl(res.url);
			}
		}
	});
}

var studentTypeListData = null;

function studentTypeForVariable() {
	$.getJSON("/student/studentType",
		function (data) {
			studentTypeListData = data;
			//var optionstring = "";
			//console.log(data);
			//$.each(data, function(i, item) {
			//optionstring += "<option value=\"" + item.id + "\" >"							+ item.name + "</option>";
			//});
		});
}
//studentTypeForVariable();

function studentTypeReturnTypeName(typeId) {
	var result = "";
	$.each(studentTypeListData, function (i, item) {


		var tmpId = item.id;
		var tmpTypeId = typeId.toString();
		//console.log(typeof(tmpId),tmpId)
		//console.log(typeof(tmpTypeId),tmpTypeId)
		if (tmpId == tmpTypeId) {
			//console.log(item.name);
			result = item.name;
			return;
		}
		//optionstring += "<option value=\"" + item.id + "\" >"							+ item.name + "</option>";
	});
	return result;

}




function city(form) {
	var selectSchoolId = $("#city").val();
	if (selectSchoolId != null && selectSchoolId != "") {
		$.getJSON("/student/getClassesBySchoolId?selectSchoolId=" + selectSchoolId,
			function (data) {
				var optionstring = "";
				//console.log(data);
				$.each(data, function (i, item) {
					optionstring += "<option value=\"" + item.id + "\" >"
						+ item.name + "</option>";
				});
				$("#county").html(
					'<option value=""></option>' + optionstring);
				//console.log(optionstring)
				//console.log("ksdfj");
				form.render('select'); //这个很重要
			});
	}

}



layui.extend({
	selectN: '/assets/layui/layui_extends/selectN',
	selectM: '/assets/layui/layui_extends/selectM',
}).use(['jquery', 'form', 'table', 'layer', 'element'], function () {

	var $ = layui.$;
	var tagData = [];

	// $.ajax({
	// 	url: "/student/getAllSportTypes",
	// 	method: "get",
	// 	async: false,
	// 	success: function (data) {
	// 		tagData = data;
	// 	},
	// 	error : function(data) {
	// 		layer.alert('请求错误！');
	// 	}
	// });
	// var selectdArray = [];

	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	var element = layui.element;
	var selectN = layui.selectN
	var selectM = layui.selectM;




	//学校 班级二级联运

	//city(form);
	form.on('select(city)', function (data) {
		var selectSchoolId = data.value;
		if (selectSchoolId != null && selectSchoolId != "") {
			$.getJSON("/student/getClassesBySchoolId?selectSchoolId=" + selectSchoolId, function (data) {
				var optionstring = "";
				$.each(data, function (i, item) {
					optionstring += "<option value=\"" + item.id + "\" >"
						+ item.name + "</option>";
				});
				$("#county").html('<option value=""></option>' + optionstring);
				form.render('select'); //这个很重要
			});
		}
	});

	//多选标签-所有配置
	// var tagIns2 = selectM({
	// 	//元素容器【必填】
	// 	elem: '#tag_ids2'

	// 	//候选数据【必填】
	// 	, data: tagData

	// 	//默认值
	// 	, selected: selectdArray

	// 	//最多选中个数，默认5
	// 	, max: 2

	// 	//input的name 不设置与选择器相同(去#.)
	// 	, name: 'sportTypeId'

	// 	//值的分隔符
	// 	, delimiter: ','

	// 	//候选项数据的键名
	// 	, field: { idName: 'value', titleName: 'name' }


	// });

	// 自定义校验规则
	//这个里面的title、number就写在Html 代码中lay-verify的属性值，即可
	//例如：lay-verify="title"
	form.verify({
		idcard: function (idcard) {
			var msg = checkIdcard(idcard);
			if (msg != 'ok') {
				return msg;
			} else {
				var result;
				var editId = $("#inputId").val();
				$.ajax({
					url:contextPath+ "/student/idcardUnique",
					method: "get",
					async: false,
					data: { idcard: idcard },
					success: function (data) {
						result = data;
					},
					error : function(data) {
						layer.alert('请求错误！');
					}
				});
				if (result == 'false') {
					return '身份证已存在，不能新增';
				}
			}

		},
		number: [/^[0-9]*$/, '必须输入数字啊'],
		phone:function (phone) {
			var reg = /^1[3|4|5|7|8][0-9]{9}$/; //验证规则

			
			
			var flag = reg.test(phone); //true			
			if (!flag) {
				return "手机号格式不正确";
			} else {
				var result;
				var editId = $("#inputId").val();
				$.ajax({
					url:contextPath+ "/student/phoneUnique",
					method: "get",
					async: false,
					data: { phone: phone },
					success: function (data) {
						result = data;
					},
					error : function(data) {
						layer.alert('请求错误！');
					}
				});
				if (result == 'false') {
					return '手机号已存在，不能新增';
				}
			}

		}
	});

	/**初始化下拉列表 */
//nation(form);
	//studentType(form);
	//biyeSchool(form);
//nationEdit(form);
	//studentTypeEdit(form);
	//biyeSchoolEdit(form);


	/** 渲染表格 */
	table.render({
		elem: '#table',
		height: 'full-300',
		url: '/student/table',
		where: {
			name: ''
		},
		page: {
			layout: ['prev', 'page', 'next', 'skip', 'count', 'limit', 'refresh'],
			limit: 10
		},
		cols: [[{
			field: 'name',
			title: '考生姓名'
		}, {
			field: 'idcard',
			title: '身份证号'
		}, {
			field: 'schoolName',
			title: '毕业学校'
		}, {
			field: 'regCode',
			title: '报名号'
		}, {
			field: 'examNumber',
			title: '准考证号'
		}, {
			field: 'type',
			title: '考生类型',
			templet: function (d) {
				//console.log(d);
				return studentTypeReturnTypeName(d.type);
			}
		}, {
			title: '操作',
			toolbar: '#bar'
		}]]
	});
	/**表格中的操作列的按钮点击函数 */
	table.on('tool(table)', function (obj) {
		var data = obj.data;
		var event = obj.event;
		/**显示修改弹出层页面 */
		if (event == 'edit') {
			layer.open({
				type: 2,
				area: ['500px', '600px'],
				title: '编辑考生',
				content: '/student/editPage?id=' + data.id,
				end: function () {
					//tableReload(table,data);
				}
			});
		} else if (event == 'view') {
			layer.open({
				type: 2,
				area: ['500px', '600px'],
				title: '查看考生信息',
				content: '/student/viewPage?id=' + data.id,
				end: function () {
					//tableReload(table,data);
				}
			});
		} else if (event == 'delete') {
			layer.confirm('您确定要删除该角色吗？', function (index) {
				$.ajax({
					url: '/student/delete',
					type: 'post',
					data: {
						id: data.id
					},
					dataType: 'json',
					beforeSend: function () {
						layer.load(2);
					},
					success: function (data) {
						layer.closeAll('loading');
						if (data.state == 'ok') {
							showOkMsg(data.msg);
							$('.layui-laypage-btn').click();
						} else if (data.state == 'fail') {
							showFailMsg(data.msg);
						} else if (data.state == 'over') {
							jumpToUrl(data.url);
						}
					},
					error : function(data) {
						layer.closeAll('loading');
						layer.alert('请求错误！');
					}
				});
				layer.close(index);
			});
		}
	});

	/**  点击搜索按钮时　查询　重新渲染表格*/
	form.on('submit(query)', function (data) {
		layer.load(2);
		table.reload('table', {
			where: data.field,
			page: {
				curr: 1
			},
			done: function (res, curr, count) {
				layer.closeAll('loading');
				if (res.state == 'over') {
					jumpToUrl(res.url);
				}
			}
		});
		return false;
	});

	/**弹出 增加页面 */
	$('#add').on('click', function () {
		layer.open({
			type: 2,
			area: ['500px', '600px'],
			title: '增加考生',
			content: '/student/addPage'
		});
	});

	/**弹出 增加页面 */
	$('#import').on('click', function () {
		layer.open({
			type: 2,
			area: ['500px', '600px'],
			title: '批量导入考生',
			content: '/student/importExcelPage'
		});
	});

	/**新增页面　表单提交执行函数 */
	form.on('submit(save)', function (data) {
		console.log(data.field);
		$.ajax({
			url: contextPath+'/student/add',
			type: 'post',
			data: data.field,
			dataType: 'json',
			beforeSend: function () {
				layer.load(2);
			},
			success: function (data) {
				layer.closeAll('loading');
				if (data.state == 'ok') {
					parentShowOkMsg(data.msg);
					$('button[lay-filter = "query"]', window.parent.document).click();
				} else if (data.state == 'fail') {
					showFailMsg(data.msg);
				} else if (data.state == 'over') {
					jumpToUrl(data.url);
				}
			},
			error : function(data) {
				layer.closeAll('loading');
				layer.alert('请求错误！');
			}
		});
		return false;
	});

	/** 修改页面　编辑按钮执行函数 */
	form.on('submit(update)', function (data) {
		$.ajax({
			url: '/student/update',
			type: 'post',
			data: data.field,
			dataType: 'json',
			beforeSend: function () {
				layer.load(2);
			},
			success: function (data) {
				layer.closeAll('loading');
				if (data.state == 'ok') {
					parentShowOkMsg(data.msg);
					$('.layui-laypage-btn', window.parent.document).click();
				} else if (data.state == 'fail') {
					showFailMsg(data.msg);
				} else if (data.state == 'over') {
					jumpToUrl(data.url);
				}
			},
			error : function(data) {
				layer.closeAll('loading');
				layer.alert('请求错误！');
			}
		});
		return false;
	});

});