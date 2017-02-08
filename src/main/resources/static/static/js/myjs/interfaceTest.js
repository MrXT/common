var locat = (window.location+'').split('/'); 
$(function(){if('tool'== locat[3]){locat =  locat[0]+'//'+locat[2];}else{locat =  locat[0]+'//'+locat[2]+'/'+locat[3];};});

$(top.hangge());

//重置
function gReload(){
	top.jzts();
	$("#serverUrl").val('');
	$("#json-field").val('');
	$("#S_TYPE_S").val('');
	self.location.reload();
}

//请求类型
function setType(value){
	$("#S_TYPE").val(value);
}

function sendSever(){
	
	if($("#serverUrl").val()==""){
		$("#serverUrl").tips({
			side:3,
            msg:'输入请求地址',
            bg:'#AE81FF',
            time:2
        });
		$("#serverUrl").focus();
		return false;
	}
	
	//加密方式  (取其中一个参数名+当前日期[格式 20150405]+混淆码",fh," 然后md5加密 的值作为 参数FKEY的值提交)
	var paraname = $("#S_TYPE_S").val();	//要加密的参数
	var nowtime = date2str(new Date(),"yyyyMMdd");
	//alert($.md5(paraname+nowtime+',fh,'));
	
	var startTime = new Date().getTime(); //请求开始时间  毫秒
	top.jzts();
	$.ajax({
		type: $("#S_TYPE").val(),
		url: $("#serverUrl").val(),
		dataType:'json',
		cache: false,
		success: function(data){
			 $(top.hangge());
			 if(data.status == 200){
				 $("#serverUrl").tips({
						side:1,
			            msg:'服务器请求成功',
			            bg:'#75C117',
			            time:10
			     });
				 var endTime = new Date().getTime();  //请求结束时间  毫秒 
				 $("#ctime").text(endTime-startTime);
			 }else{
				 $("#serverUrl").tips({
						side:3,
			            msg:'请求失败,检查URL正误',
			            bg:'#FF5080',
			            time:10
			     });
				 return;
			 }
			 $("#json-field").val(formatJson(JSON.stringify(data)));
			 $("#json-field").tips({
					side:2,
		            msg:'返回结果',
		            bg:'#75C117',
		            time:10
		     });
		}
	});
}

function intfBox(){
	var intfB = document.getElementById("json-field");
	var intfBt = document.documentElement.clientHeight;
	intfB .style.height = (intfBt  - 320) + 'px';
}
intfBox();
window.onresize=function(){  
	intfBox();
};

//js  日期格式
function date2str(x,y) {
     var z ={y:x.getFullYear(),M:x.getMonth()+1,d:x.getDate(),h:x.getHours(),m:x.getMinutes(),s:x.getSeconds()};
     return y.replace(/(y+|M+|d+|h+|m+|s+)/g,function(v) {return ((v.length>1?"0":"")+eval('z.'+v.slice(-1))).slice(-(v.length>2?v.length:2))});
 	};
 	
 	var formatJson = function(json, options) {
 		var reg = null,
 			formatted = '',
 			pad = 0,
 			PADDING = '    '; // one can also use '\t' or a different number of spaces
 	 
 		// optional settings
 		options = options || {};
 		// remove newline where '{' or '[' follows ':'
 		options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
 		// use a space after a colon
 		options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;
 	 
 		// begin formatting...
 		if (typeof json !== 'string') {
 			// make sure we start with the JSON as a string
 			json = JSON.stringify(json);
 		} else {
 			// is already a string, so parse and re-stringify in order to remove extra whitespace
 			json = JSON.parse(json);
 			json = JSON.stringify(json);
 		}
 	 
 		// add newline before and after curly braces
 		reg = /([\{\}])/g;
 		json = json.replace(reg, '\r\n$1\r\n');
 	 
 		// add newline before and after square brackets
 		reg = /([\[\]])/g;
 		json = json.replace(reg, '\r\n$1\r\n');
 	 
 		// add newline after comma
 		reg = /(\,)/g;
 		json = json.replace(reg, '$1\r\n');
 	 
 		// remove multiple newlines
 		reg = /(\r\n\r\n)/g;
 		json = json.replace(reg, '\r\n');
 	 
 		// remove newlines before commas
 		reg = /\r\n\,/g;
 		json = json.replace(reg, ',');
 	 
 		// optional formatting...
 		if (!options.newlineAfterColonIfBeforeBraceOrBracket) {			
 			reg = /\:\r\n\{/g;
 			json = json.replace(reg, ':{');
 			reg = /\:\r\n\[/g;
 			json = json.replace(reg, ':[');
 		}
 		if (options.spaceAfterColon) {			
 			reg = /\:/g;
 			json = json.replace(reg, ':');
 		}
 	 
 		$.each(json.split('\r\n'), function(index, node) {
 			var i = 0,
 				indent = 0,
 				padding = '';
 	 
 			if (node.match(/\{$/) || node.match(/\[$/)) {
 				indent = 1;
 			} else if (node.match(/\}/) || node.match(/\]/)) {
 				if (pad !== 0) {
 					pad -= 1;
 				}
 			} else {
 				indent = 0;
 			}
 	 
 			for (i = 0; i < pad; i++) {
 				padding += PADDING;
 			}
 	 
 			formatted += padding + node + '\r\n';
 			pad += indent;
 		});
 	 
 		return formatted;
 	};