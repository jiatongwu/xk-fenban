




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
					url: "/student/idcardUnique",
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




	/** 渲染表格 */
	table.render({
		elem: '#table',
		height: 'full-300',
		url: contextPath + '/xm/table',
		where: {
			name: ''
		},
		page: {
			layout: ['prev', 'page', 'next', 'skip', 'count', 'limit', 'refresh'],
			limit: 10
		},
		cols: [[{
			field: 'xm',
			title: '名称',
			width:300
		}, {
			field: 'grade',
			title: '班级',
			width:200
		}, {
			field: 'curtime',
			title: '时间',
			width:200,
			templet: function (d) {
				var haomiao = d.curtime;
				if (haomiao != null && haomiao !== undefined) {
					var date = new Date(parseInt(haomiao));
					Y = date.getFullYear() + '-';
					M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
					D = date.getDate() + ' ';
					h = date.getHours() + ':';
					m = date.getMinutes() + ':';
					s = date.getSeconds();
					return Y + M + D + h + m + s;
				} else {
					return "";
				}
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
		console.log(data);
		/**显示修改弹出层页面 */
		if (event == 'importSportScore') {
			layer.open({
				type: 2,
				area: ['600px', '600px'],
				title: '导入体育成绩',
				content:  contextPath +'/xm/importSportScorePage?xmid=' + data.xmid,
				end: function () {
					//tableReload(table,data);
				}
			});
		} else 	if (event == 'exportBjRank') {
			layer.open({
				type: 2,
				area: ['600px', '600px'],
				title: '导出考生各科排名',
				content:  contextPath +'/xm/exportBjRankPage?xmid=' + data.xmid,
				end: function () {
					//tableReload(table,data);
				}
			});
		} else 	if (event == 'exportSubPercent') {
			layer.open({
				type: 2,
				area: ['600px', '600px'],
				title: '导出各科各级别中各班占比',
				content:  contextPath +'/xm/exportSubPercentPage?xmid=' + data.xmid,
				end: function () {
				}
			});
		} else if (event == 'view') {
			layer.open({
				type: 2,
				area: ['600px', '600px'],
				title: '查看考生信息',
				content: '/student/viewPage?id=' + data.id,
				end: function () {
					//tableReload(table,data);
				}
			});
		} else if (event == 'delete') {
			layer.confirm('您确定要删除该考生吗？', function (index) {
				$.ajax({
					url: '/student/delete?id=' + data.id,
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
			area: ['600px', '600px'],
			title: '增加考生',
			content: '/student/addPage'
		});
	});

	/**弹出 增加页面 */
	$('#import').on('click', function () {
		layer.open({
			type: 2,
			area: ['700px', '600px'],
			title: '批量导入考生',
			content: '/student/importExcelPage',
			end: function () {
				$(".layui-laypage-btn").click();
			}
		});
	});

	/**新增页面　表单提交执行函数 */
	form.on('submit(save)', function (data) {
		//console.log(data.field);
		$.ajax({
			url: '/student/save',
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
			error: function (data) {
				layer.closeAll('loading');
				layer.alert('请求错误！');
			}
		});
		return false;
	});

});

