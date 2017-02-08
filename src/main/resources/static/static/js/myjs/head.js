var locat = (window.location + '').split('/');
$(function() {
	if ('main' == locat[3]) {
		locat = locat[0] + '//' + locat[2];
	} else {
		locat = locat[0] + '//' + locat[2] + '/' + locat[3];
	}
	;
});

var fmid = "fhindex"; // 菜单点中状态
var mid = "fhindex"; // 菜单点中状态
var fhsmsCount = 0; // 站内信总数
var USER_ID; // 用户ID
var user = "FH"; // 用于即时通讯（ 当前登录用户）
var TFHsmsSound = '1'; // 站内信提示音效
var websocket; // websocket对象
var wimadress = ""; // 即时聊天服务器IP和端口
var oladress = ""; // 在线管理和站内信服务器IP和端口

function siMenu(id, fid, MENU_NAME, MENU_URL) {
	if (id != mid) {
		$("#" + mid).removeClass();
		mid = id;
	}
	if (fid != fmid) {
		$("#" + fmid).removeClass();
		fmid = fid;
	}
	$("#" + fid).attr("class", "active open");
	$("#" + id).attr("class", "active");
	top.mainFrame.tabAddHandler(id, MENU_NAME, MENU_URL);
	//jzts();不开启加载框
}

// 清除加载进度
function hangge() {
	$("#jzts").hide();
}

// 显示加载进度
function jzts() {
	$("#jzts").show();
}