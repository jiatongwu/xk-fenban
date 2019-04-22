
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
var tmpFiles = {};
layui.use(['jquery', 'upload', 'form', 'table', 'layer', 'element'], function () {

	upload = layui.upload;
	var $ = layui.$;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	var element = layui.element;

	$(document).on('click', '#downloadTemplate', function () {
		downloadFile("/student/importTemplateDownload");
	});

	//选完文件后不自动上传
	upload.render({
		elem: '#test8'
		, url: '/student/uploadStudentImageZip'
		, auto: false,
		//,multiple: true
		data: {
			schoolId: function () {
				var schoolId=$('[name="schoolId"]').val();
				return schoolId;
			}
		},
		accept: 'file'
		, bindAction: '#test9'
		, done: function (res, index, upload) {
			var successCount = res.successCount;
			var errorCount = res.errorCount;

			delete tmpFiles[index];

			var successMessageList = res.successMessageList;
			var errorMessageList = res.errorMessageList;
			var successmessagestring = "";
			// //console.log(data);
			$.each(successMessageList, function (i, item) {
				successmessagestring += (item + "<br/>");
			});
			var errormessagestring = "";
			// //console.log(data);
			$.each(errorMessageList, function (i, item) {
				errormessagestring += (item + "<br/>");
			});

			if (res.state == 'ok') {
				$("#fileNameDiv").html('无');
				$("#messageDiv").html(`
			 	导入成功`+ successCount + `条<br/>
				导入失败`+ errorCount + `条 <br/>
				导入成功的数据有：<br/>
				 `+ successmessagestring + `	
				 导入失败的数据有：<br/>
			 	`+ errormessagestring + `			
		 `);

				showOkMsg(res.msg);
				$('.layui-laypage-btn').click();
			} else if (res.state == 'fail') {
				$("#fileNameDiv").html('无');
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
				//console.log(index); //得到文件索引
				//console.log(file); //得到文件对象
				for (var key in tmpFiles) {
					if (index == key) {
					} else {
						delete tmpFiles[key];
					}
				}
				var fileName = file.name;
				$("#fileNameDiv").html(fileName)
				//console.log(result); //得到文件base64编码，比如图片
			});
		}


	});



});