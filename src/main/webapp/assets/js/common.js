var contextPath=$("#contextPath").val();
layui.use([ 'jquery','layer', 'element' ], function() {
	var $ = layui.$;
	var form = layui.form;
	var layer = layui.layer;
	// layui-nav-itemed
	var obj = $('.layui-this');
	if($.isArray(obj)){
		for (var i = 0; i < obj.length; i++) {
			obj[i].parents(".layui-nav-item").addClass("layui-nav-itemed");
		}
	}else{
		$(obj).parents(".layui-nav-item").addClass("layui-nav-itemed");
	}
});
/** 成功提示 */
function showOkMsg(msg) {
	layer.msg(msg, {
		icon : 6
	});
}

/** 父页面成功提示 */
function parentShowOkMsg(msg) {
	parent.layer.msg(msg, {
		icon : 6
	});
	parent.layer.closeAll('iframe');
}

/** 失败提示 */
function showFailMsg(msg) {
	layer.msg(msg, {
		icon : 5,
		shift : 6
	});
}

/** 网页跳转 */
function jumpToUrl(url) {
	top.location = url;
}

/** 修改密码 **/
function modifyPwd(){
	layer.open({
		type : 2,
		area : [ '500px', '550px' ],
		title : '修改密码',
		content : '/pwd/modifyPwd'
	});
}