var contextPath=$("#contextPath").val();
layui
//.config({
  //  base:"<%=basePath%>common/larrylib/",
//})
.extend({
	treetable: contextPath+'/assets/layui/treetable-lay/treetable'
}).use(['table', 'treetable'], function () {
	var $ = layui.jquery;
	var e = layui.$;
	var table =  layui.table;
	var t = layui.layer;
	var treetable = layui.treetable;
	var width = $(document).width();
	var height = $(document).height();
   // enter("getSelect");
	// 渲染表格
	var renderTable = function(){
		layer.load(2);
		treetable.render({
			id: 'auth-table',
			treeColIndex: 0,
			treeSpid: '-1',
			treeIdName: 'authorityId',
			treePidName: 'parentId',
			elem: '#auth-table',
			url: contextPath+'/permission/selectAll',
			page: false,
			cols: [[
				//{field: 'num', title: '序号',align: "center", width: width * 0.07},
				//{field: 'icon', title: '图标',align: "center", width: width * 0.07},
				{field: 'name', title: '菜单名称', width: width * 0.18},
				{field: 'url', title: '菜单URL地址', width: width * 0.22},
				{field: 'code', title: '菜单权限代码', width: width * 0.16},
				{
					field: 'type', width: width* 0.06, align: 'center', templet: function (d) {
						if (d.type == 0) {
							return '<span class="layui-badge layui-bg-blue">目录</span>&nbsp;';
						}
						if (d.type == 1) {
							return '<span class="layui-badge-rim">菜单</span>&nbsp;';
						}
						if (d.type == 2) {
							return '<span class="layui-badge layui-bg-gray">按钮</span>&nbsp;';
						}
					}, title: '类型'
				},
				{templet: '#oper-col', title: '常用操作',align: "center", width: width * 0.13}
			]],
			done: function ( res,curr,count) {
				//console.log(res);
				//console.log(curr);
				//console.log(count);
				count<=0?$("#insert_menu").show():$("#insert_menu").hide();
				layer.closeAll('loading');
			}
		});
	};
	renderTable();
   


	$('#btn-expand').click(function () {
		treetable.expandAll('#auth-table');
	});

	$('#btn-fold').click(function () {
		treetable.foldAll('#auth-table');
	});

	$('#btn-search').click(function () {
		var keyword = $('#edt-search').val();
		var searchCount = 0;
		$('#auth-table').next('.treeTable').find('.layui-table-body tbody tr td').each(function () {
			$(this).css('background-color', 'transparent');
			var text = $(this).text();
			if (keyword != '' && text.indexOf(keyword) >= 0) {
				$(this).css('background-color', 'rgba(250,230,160,0.5)');
				if (searchCount == 0) {
					treetable.expandAll('#auth-table');
					$('html,body').stop(true);
					$('html,body').animate({scrollTop: $(this).offset().top - 150}, 500);
				}
				searchCount++;
			}
		});
		if (keyword == '') {
			layer.msg("请输入搜索内容", {icon: 5});
		} else if (searchCount == 0) {
			layer.msg("没有匹配结果", {icon: 5});
		}
	});

	table.on("tool(auth-table)", function (e) {
		var i = e.data;
		if (e.event == "del") {
			t.confirm("该数据将被清除,确认吗？", {
				icon: 3,
				skin: 'larry-green',
				title: "删除提示",
				offset: '200px',
				closeBtn: 0,
				skin: 'layui-layer-molv',
				anim: Math.ceil(Math.random() * 6),
				btn: ['确定', '取消']
			}, function (index) {
				$.ajax({
					url:  contextPath+"/permission/delete?id="+i.id,
					type: 'post',
					async: false,
					data: {menuId:i.menuId},
					dataType: "json",
					success: function (data) {
						if (data.state == 200) {
							t.msg(data.message, {icon: 6});
							renderTable();//刷新父页面
							return true;
						} else {
							t.msg(data.message, {icon: 5});
							return false;
						}
					}, error: function () {
						top.layer.msg("系统数据处理异常！", {icon: 2});
						return false;
					}
				});
				layer.close(index);
			}, function (index) {

				layer.close(index);
			});
		}

		if (e.event  == "edit"){
			var e = layui.layer.open({
				title: "编辑菜单",
				type: 2,
				move: false,
				anim: 1,
				skin: "larry-green",
				offset: '80px',
				area: [width / 1.5 + "px", height*0.77 + "px"],
				content:  contextPath+"/permission/editPage"
			});
		}


		if (e.event  == "add"){
			var e = layui.layer.open({
				title: "新增菜单",
				type: 2,
				move: false,
				anim: 1,
				skin: "larry-green",
				offset: '80px',
				area: [width / 1.5 + "px", height*0.77 + "px"],
				content:  contextPath+"/permission/addPage?parentId="+i.id
			});
		}



	});

	e("#insert_menu").on("click", function () {
		var e = layui.layer.open({
			title: "菜单信息",
			type: 2,
			move: false,
			anim: 1,
			skin: "larry-green",
			offset: '80px',
			area: [width / 1.5 + "px", height*0.77 + "px"],
			content: contextPath+"/permission/addPage"
		});
	});

	e("#getSelect").on("click", function () {
		var t = e(this).data("type");
		r[t] ? r[t].call(this) : "";
	});

	var r = {
		getSelect: function () {
			var i = t.msg("查询中，请稍候...", {icon: 16, time: false, shade: 0});
			renderTable();
			t.close(i);
		}
	};
});