layui.use([ 'jquery', 'form', 'layer' ], function() {

	var $ = layui.$;
	var form = layui.form;
	var layer = layui.layer;

	/** 自定义验证规则 */
	form.verify({
		username : [ /[\S]+/, '请输入用户名' ],
		password : [ /[\S]+/, '请输入密码' ],
		captcha : [ /[\S]+/, '请输入验证码' ]
	});

	/** 登录 */
	// 默认焦点
	$('.box input[name = "username"]').focus();
	// 登录
	form.on('submit(login)', function(data) {
		$.ajax({
			url : '/login',
			type : 'post',
			data : data.field,
			dataType : 'json',
			beforeSend : function() {
				layer.load(2);
			},
			success : function(data) {
				layer.closeAll('loading');
				if (data.state == 'ok') {
					jumpToUrl(data.url);
				} else if (data.state == 'fail') {
					showFailMsg(data.msg);
					$('.box .captcha').click();
					$('.box input[name = "captcha"]').val('');
				}
			}
		});
		return false;
	});

});