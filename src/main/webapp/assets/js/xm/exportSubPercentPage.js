
var contextPath=$("#contextPath").val();

var xmid=$("#xmid").val();
function downloadFileByForm(url, fieldData) {
	var form = $("<form></form>").attr("action", url).attr("method", "get");
	form.append($("<input></input>").attr("type", "hidden").attr("name", "school").attr("value", fieldData.areaId));
	
	form.append($("<input></input>").attr("type", "hidden").attr("name", "xmid").attr("value", xmid));
	
	form.appendTo('body').submit().remove();
}
 

layui.use(['form','layer'], function () {
	var form = layui.form;
	var layer = layui.layer;
	
	
	$('#exportConfirmedStudent').on('click', function () {

		var areaId = $('[name="areaId"]').val();
		if(areaId==''||areaId==undefined){
			console.log("null");
			layer.msg("请选择学校");		
			return;
		}
		var fieldData = {};
		fieldData.areaId = areaId; 
		//downloadFileByForm(contextPath +"/xm/exportBjRank", fieldData);
		var url=contextPath +"/xm/exportSubPercent";　
		$.fileDownload(url,{
		 data:{school:areaId,xmid:xmid},
		prepareCallback:function(url){
		  //alert("正在导出,请稍后...");
		  layer.load(2); 
		 },
		 successCallback:function(url){
		  //alert("导出完成！");
		  layer.closeAll('loading');
		 }, 
		 failCallback:function (html, url) {
		  //alert("导出失败，未知的异常。");
		  layer.closeAll('loading');
		  layer.msg("导出失败请联系管理员");
		  } 
		 }); 

	});
	// form.on('select(city)', function (data) {
	// 	var selectSchoolId = data.value;
	// 	if (selectSchoolId != null && selectSchoolId != "") {
	// 		$.getJSON(contextPath+"/xm/getClassesByXmidAndSchool?xmid="+xmid+"&school=" + selectSchoolId, function (data) {
	// 			var optionstring = "";
	// 			$.each(data, function (i, item) {
	// 				optionstring += "<option value=\"" + item + "\" >"
	// 					+ item + "</option>";
	// 			});
	// 			$("#county").html('<option value=""></option>' + optionstring);
	// 			form.render('select'); //这个很重要
	// 		});
	// 	}
	// });
});