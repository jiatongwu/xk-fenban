var contextPath = $("#contextPath").val();


layui//.config({
    //base: "<%=basePath%>common/larrylib/",
    //version: '201808031'
    //})
    .use(["layer", "form", "table", "jquery", "laydate", "upload"], function () {
        var $ = layui.jquery;
        var e = layui.$, t = layui.layer, i = layui.form, l = layui.table;
        var width = $(document).width();
        var height = $(document).height();


        var a = l.render({
            elem: "#bannerPage",
            width: width,
            height: 'full-140',
            id: 'mytable',
            size: 'lg',
            cols: [[
                { checkbox: true, type: 'checkbox', fixed: 'left', width: width * 0.05 },
                { field: 'name', title: '用户名', align: "center", width: width * 0.25 },
                { field: 'phone', title: '手机号', align: "center", width: width * 0.25 },
                { field: 'extend', title: '角色', align: "center", width: width * 0.1 },
                { title: "常用操作", width: width * 0.2, align: "center", toolbar: "#merchantBar", fixed: "right" }
            ]],
            request: {
                pageName: 'pageNumber',
                limitName: 'pageSize'
            },
            response: {
                statusName: 'code' //数据状态的字段名称，默认：code
                , statusCode: 0 //成功的状态码，默认：0
                , msgName: 'message' //状态信息的字段名称，默认：msg
                , countName: 'total' //数据总数的字段名称，默认：count
                , dataName: 'data' //数据列表的字段名称，默认：data
            },
            url: contextPath + "/user/selectAll",
            page: true,
            limit: 10,
            even: true
        });

        l.on("tool(bannerPage)", function (e) {
            var i = e.data;

            if (e.event == "user_role") {
                var e = layui.layer.open({
                    title: "分配 " + i.name + "角色",
                    type: 2,
                    move: false,
                    anim: 1,
                    skin: "larry-green",
                    offset: '80px',
                    /* area: [width / 1.73 + "px", height*0.7 + "px"],*/
                    area: [width / 2.6 + "px", height * 0.8 + "px"],
                    content: contextPath + "/user/role/page?userId=" + i.id
                });
            }

        });



        e("#add_manager").on("click", function () {
            var e = layui.layer.open({
                title: "添加用户",
                type: 2,
                move: false,
                anim: 1,
                skin: "larry-green",
                offset: '80px',
                area: [width / 2.5 + "px", height * 0.5 + "px"],
                content: contextPath + "/user/addPage"
            });
        });

        e(".larry-btn a.layui-btn").on("click", function () {
            var t = e(this).data("type");
            r[t] ? r[t].call(this) : "";
        });

        var r = {
            getSelect: function () {
                var i = t.msg("查询中，请稍候...", { icon: 16, time: false, shade: 0 });
                var userPhone = e("#userPhone").val();
                var userName = e("#userName").val();
                l.reload('mytable', {
                    where: {
                        userPhone: userPhone,
                        userName: userName,
                    }, method: 'post',
                    height: 'full-140', method: 'post'
                });
                t.close(i);
            }

        };


    });
