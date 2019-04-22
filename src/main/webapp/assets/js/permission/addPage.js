

var contextPath=$("#contextPath").val();
var addFirstMenu=$("#addFirstMenu").val();
//.config({
//base: "<%=basePath%>common/larrylib/",
  //  version: '201708031'
//})
layui.use(['jquery', 'form', 'upload'], function () {
    var $ = layui.jquery, form = layui.form;


    form.verify({
        number: [
            /^[0-9]{0,5}$/
            ,'请填写正确的数字！'
        ],
        phone:[
            /^1[34578]\d{9}$/
            ,'请填写正确的手机号！'
        ],
        money:[
            /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/
            ,'请填写正确格式的价格！'
        ],
        range:[
            /(^[1-9]{1,10}~[1-9]{1,10}$)/
            ,'请填写正确格式的范围！'
        ]
    });


    $("input[id='code']").blur(function(){
        var code = $("#code").val();
        //var menuId = $("#menuId").val();
        if(code != ""){
            $.ajax({
                url: contextPath+'/permission/checkCode',
                type: 'post',
                async: false,
                data: {code:code},
                dataType: "json",
                success: function (data) {
                    if(data.state==200){                    
                           
                       
                    }else{
                        layer.msg("权限代码已存在，或模糊存在！", {icon: 5});
                    }
                }, error: function () {
                    layer.msg("系统数据处理异常！", {icon: 2});
                }
            });
        }
    });


    //监听提交
    form.on("submit(ok)", function (data) {
       var type = $("input[name='type']:checked").val();
        var url = $("#url").val();
        var code = $("#code").val();
        if(type==1 || type==2){
            if(url.trim().length==0){
                top.layer.msg("菜单URL不能为空", {icon: 5});
                $("#url").focus();
                return false;
            }
            if(code.trim().length==0){
                top.layer.msg("权限代码不能为空", {icon: 5});
                $("#code").focus();
                return false;
            }

       }
       

        var loginLoading = top.layer.msg('提交中，请稍候...', {icon: 16, time: false, shade: 0.8});
        $.ajax({
            url: contextPath+'/permission/add',
            type: 'post',
            async: false,
            data: data.field,
            dataType: "json",
            success: function (data) {
                if (data.state == 200) {
                   // window.setInterval(function(){},1500);
                   top.layer.msg(data.message, {icon: 6});
                       var index = parent.layer.getFrameIndex(window.name); //当前iframe层的索引
                    parent.layer.close(index); //再执行关闭
                    var father = parent.layui.jquery;
                    father("#getSelect").click();
                    return false;
                } else {
                    top.layer.msg(data.message, {icon: 5});
                    return false;
                }
                top.layer.close(loginLoading);
            }, error: function () {
                top.layer.msg("系统数据处理异常！", {icon: 2});
                top.layer.close(loginLoading);
                return false;
            }
        });
        return false;
    });

    //取消
    $("#cancle").click(function () {
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });

});
