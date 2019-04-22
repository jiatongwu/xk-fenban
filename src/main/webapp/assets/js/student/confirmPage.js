
layui.
	//config({
	//	base: './'
	//}).
	extend({
		selectN: '/assets/layui/layui_extends/selectN',
		selectM: '/assets/layui/layui_extends/selectM',
	}).use(['layer', 'form', 'selectN', 'selectM', 'jquery', 'table', 'element', 'laytpl'], function () {
		// $ = layui.jquery;	
		var form = layui.form
			, selectN = layui.selectN
			, selectM = layui.selectM;


		var $ = layui.$;

		var table = layui.table;
		var layer = layui.layer;
		var element = layui.element;
		var laytpl = layui.laytpl;
		var tagData = [];

		$.ajax({
			url: "/student/getAllSportTypes",
			method: "get",
			async: false,
			success: function (data) {
				tagData = data;
			},
			error : function(data) {
				layer.alert('请求错误！');
			}
		});
		var currentSportTypesHiddenIdValue = $("#sportTypesHiddenId").val();
		var selectdArray = [];
		if (currentSportTypesHiddenIdValue != null && currentSportTypesHiddenIdValue != "") {
			selectdArray = currentSportTypesHiddenIdValue.split(",");
		}




		//多选标签-所有配置
		var tagIns2 = selectM({
			//元素容器【必填】
			elem: '#tag_ids2'

			//候选数据【必填】
			, data: tagData

			//默认值
			, selected: selectdArray

			//最多选中个数，默认5
			, max: 2

			//input的name 不设置与选择器相同(去#.)
			, name: 'sportTypeId'

			//值的分隔符
			, delimiter: ','

			//候选项数据的键名
			, field: { idName: 'value', titleName: 'name' }


		});





		/**新增页面　表单提交执行函数 */
		form.on('submit(save)', function (data) {

			if (tagIns2.values.length != 2) {
				layer.alert('至少要选择2个体育项目', { icon: 1 });
				return false;
			}

			//console.log('tagIns2 当前选中的值名：', tagIns2.selected);
			//console.log('tagIns2 当前选中的值：', tagIns2.values);
			//console.log('tagIns2 当前选中的名：', tagIns2.names);





			//console.log('');

			var formData = data.field;
			//console.log('表单对象：', formData);

			layer.confirm('请再次确认报名信息是否正确?',
				function (index) {
					layer.close(index);
					$.ajax({
						url: '/student/confirm',
						type: 'post',
						data: data.field,
						dataType: 'json',
						beforeSend: function () {
							layer.load(2);
						},
						success: function (data) {
							layer.closeAll('loading');
							if (data.state == 'ok') {
								//console.log("ok");
								location.reload();
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

				});



			return false;
		});








		// 	//通过js动态选择
		// $('.set1').click(function(){
		//   catIns1.set([6,10]);

		// });
		// 	//通过js动态选择
		// $('.set2').click(function(){
		//   tagIns1.set([12,13,14,15]);
		// });

		// $('.fly').click(function(){
		//   window.open('http://fly.layui.com/jie/26751/');
		// });

		// $('.mayun').click(function(){
		//   window.open('https://gitee.com/moretop/layui-select-ext');
		// });




		// 	//监听重置按钮
		// 	$('form').find(':reset').click(function(){
		// 		$('form')[0].reset();
		// 		catIns1.set();//默认值
		// 		catIns2.set();//默认值
		//   tagIns1.set();//默认值
		//   tagIns2.set();//默认值
		// 		return false;
		// 	});



	});

