
layui.use(['jquery', 'form', 'table', 'layer', 'element'], function () {

	var $ = layui.$;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	var element = layui.element;

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
					url: contextPath + "/student/idcardUnique",
					method: "get",
					async: false,
					data: { idcard: idcard, id: editId },
					success: function (data) {
						result = data;
					},
					error: function (data) {
						layer.alert('请求错误！');
					}
				});
				if (result == 'false') {
					return '身份证已存在，不能新增';
				}
			}

		},
		number: [/^[0-9]*$/, '必须输入数字啊']
	});

	/**初始化下拉列表 */



	/** 渲染表格 */
	table.render({
		elem: '#table',
		height: 'full-300',
		url: contextPath + '/student/table',
		where: {
			name: ''
		},
		page: {
			layout: ['prev', 'page', 'next', 'skip', 'count', 'limit', 'refresh'],
			limit: 10
		},
		cols: [[{
			field: 'name',
			title: '姓名'
		}, {
			field: 'idcard',
			title: '身份证号'
		}, {
			field: 'phone',
			title: '手机号'
		}, , {
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
				area: ['600px', '600px'],
				title: '编辑学生',
				content: contextPath + '/student/editPage?id=' + data.id,
				end: function () {
					//tableReload(table,data);
				}
			});
		} else if (event == 'view') {
			layer.open({
				type: 2,
				area: ['600px', '600px'],
				title: '查看学生信息',
				content: contextPath + '/student/viewPage?id=' + data.id,
				end: function () {
					//tableReload(table,data);
				}
			});
		} else if (event == 'delete') {
			layer.confirm('您确定要删除该学生吗？', function (index) {
				$.ajax({
					url: contextPath + '/student/delete?id=' + data.id,
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
					error: function (data) {
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
		var code = data.field.select;
		if (code == null || code == undefined) {
			layer.alert("请选择一项");
			return false;
		}
		var canSubmit = true;
		var xuankeFullName = "";
		$.ajax({
			url: contextPath + '/xuanke/getKemuzuheByCode',
			type: 'post',
			async: false,
			data: data.field,
			dataType: 'json',
			beforeSend: function () {
				layer.load(2);
			},
			success: function (data) {
				layer.closeAll('loading');
				if (data != null) {
					//parentShowOkMsg(data.msg);
					//layer.alert(data.msg);
					xuankeFullName = data.fullname;
					//$('button[lay-filter = "query"]', window.parent.document).click();
					//location.reload();
				}
			},
			error: function (data) {
				layer.closeAll('loading');
				layer.alert('请求错误！');
			}
		});
		if (canSubmit) {
			layer.confirm('你的选科是：' + xuankeFullName + '，请确认?',
				function (index) {
					$.ajax({
						url: contextPath + '/xuanke/setXuanke',
						type: 'post',
						data: data.field,
						dataType: 'json',
						beforeSend: function () {
							layer.load(2);
						},
						success: function (data) {
							layer.closeAll('loading');
							if (data.state == 'ok') {
								//parentShowOkMsg(data.msg);
								layer.confirm(data.msg, { btn: ['确定'] }, function () {
									location.reload();
								});

								//$('button[lay-filter = "query"]', window.parent.document).click();

							} else if (data.state == 'fail') {
								showFailMsg(data.msg);
							} else if (data.state == 'over') {
								jumpToUrl(data.url);
							}
						},
						error: function (data) {
							layer.closeAll('loading');
							layer.alert('请求错误！');
						}
					});

				});
		}


		return false;
	});
	form.on('checkbox()', function (data) {
		console.log(data);
		var allCheckbox = $('input[type="checkbox"]');
		allCheckbox.each(function (index, item) {
			if (item === data.elem) {

			} else {
				item.checked = false;
			}
		});
		form.render('checkbox');
		return false;
	});

	/**弹出 增加页面 */
	$('#add').on('click', function () {
		layer.open({
			type: 2,
			area: ['600px', '600px'],
			title: '增加学生',
			content: contextPath + '/student/addPage'
		});
	});

	/**弹出 增加页面 */
	$('#import').on('click', function () {
		layer.open({
			type: 2,
			area: ['700px', '600px'],
			title: '批量导入学生',
			content: contextPath + '/student/importExcelPage',
			end: function () {
				$(".layui-laypage-btn").click();
				table.reload('table', {
					where: {},
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
			}
		});
	});

	/**新增页面　表单提交执行函数 */
	form.on('submit(save)', function (data) {
		//console.log(data.field);
		$.ajax({
			url: contextPath + '/student/save',
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
			error: function (data) {
				layer.closeAll('loading');
				layer.alert('请求错误！');
			}
		});
		return false;
	});

	/** 修改页面　编辑按钮执行函数 */
	form.on('submit(update)', function (data) {
		$.ajax({
			url: contextPath + '/student/update',
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
			error: function (data) {
				layer.closeAll('loading');
				layer.alert('请求错误！');
			}
		});
		return false;
	});

});