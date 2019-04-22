var contextPath=$("#contextPath").val();
function downloadFile(urlToSend) {
	// var req = new XMLHttpRequest();
	// req.open("GET", urlToSend, true);
	// req.responseType = "blob";
	// req.onload = function (event) {
	// 	var blob = req.response;
	// 	var fileName = req.getResponseHeader("fileName") //if you have the fileName header available
	// 	var link=document.createElement('a');
	// 	link.href=window.URL.createObjectURL(blob);
	// 	link.download=fileName;
	// 	link.click();
	// };

	// req.send();
	window.location = urlToSend;
}
var tmpFiles = [];
layui.use(['jquery', 'upload', 'form', 'table', 'layer', 'element'], function () {

	upload = layui.upload;
	var $ = layui.$;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	var element = layui.element;

	$(document).on('click', '#downloadTemplate', function () {
		downloadFile( contextPath +"/xm/importTemplateDownload");
	});


	function uploadListenReset(uploadListen) {

	}

	//选完文件后不自动上传
	var uploadListen = upload.render({
		elem: '#test8'
		, url: contextPath + '/xm/importExcel'
		, auto: false,
		//,multiple: true
		accept: 'file'
		,field:'excel'
		, data: {
			xmid: function () {
				var schoolId = $('[name="xmid"]').val();
				return schoolId;
			}, deleteOrigin: function () {
				var deleteOriginStudent = $('#deleteOriginStudent').is(":checked");
				return deleteOriginStudent;
			}
		}
		, bindAction: '#test9',
		before: function (obj) { //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
			//console.log(tmpFiles)
			var hasFile = false;
			for (var key in tmpFiles) {
				hasFile = true;
			}
			if (hasFile == true) {
				layer.load(2); //上传loading
			}else{
				layer.msg("请先选择要上传的文件");
			}
		}
		, done: function (res, index, upload) {
			delete tmpFiles[index];
			layer.closeAll('loading');

			var successCount = res.successCount;
			var errorCount = res.errorCount;
			var messageList = res.errorMessages;
			var messagestring = "";
			$.each(messageList, function (i, item) {
				messagestring += (item + "<br/>");
			});
			if (res.state == 'ok') {
				$("#fileNameDiv").html('无');
				$("#messageDiv").html(`
				导入成功`+ successCount + `条<br/>
				导入失败`+ errorCount + `条 <br/>
				`+ messagestring + `
			
			`);
				showOkMsg(res.msg);
				$('.layui-laypage-btn').click();
			} else if (res.state == 'fail') {
				$("#fileNameDiv").html('无');
				$("#messageDiv").html(``);
				showFailMsg(res.msg);
			} else if (res.state == 'over') {
				jumpToUrl(res.url);
			}
		},
		choose: function (obj) {
			//将每次选择的文件追加到文件队列
			tmpFiles = this.files = obj.pushFile();
			//预读本地文件，如果是多文件，则会遍历。(不支持ie8/9)
			obj.preview(function (index, file, result) {
				var schoolId = $('[name="schoolId"]').val();
				if (schoolId == "") {
					for (var key in tmpFiles) {
						delete tmpFiles[key];
					}
					layer.msg("请选择学校");
					return;
				}

				for (var key in tmpFiles) {
					if (index == key) {
					} else {
						delete tmpFiles[key];
					}
				}
				//console.log(index); //得到文件索引
				//console.log(file); //得到文件对象
				var fileName = file.name;
				$("#fileNameDiv").html(fileName)
				//console.log(result); //得到文件base64编码，比如图片
			});
		}


	});




});