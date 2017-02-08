/**
 * 表格展示数据
 * 
 * @param jsons
 * 样式默认center，可以不用写
 *            [{"columnName":"姓名","propertyName":"name","styleName":"center"}]
 * @param tableId 表单ID
 * @param idProperty 填充数据的每条记录的id属性。例如：roleId
 * @param optButtonId 操作按钮的id
 * @param loadCheckBox 是否加载复选框
 * 
 */
function Datagrid(jsons,tableId,idProperty,optButtonId,loadCheckBox) {
	this.jsons = jsons;
	this.tableId = tableId;
	this.idProperty = idProperty;
	this.optButtonId = optButtonId;
	this.loadCheckBox = loadCheckBox;
	var columnNames = [];
	var propertyNames = [];
	var styleNames = [];
	for (var i = 0; i < jsons.length; i++) {
		columnNames.push(jsons[i].columnName);
		propertyNames.push(jsons[i].propertyName);
		styleNames.push(jsons[i].style?jsons[i].style:"");
	}
	this.propertyNames = propertyNames;
	this.columnNames = columnNames;
	this.styleNames = styleNames;
	this.loadData = function(data) {
		/**
		 * data 填充数据数组格式
		 */
		var theadHtml = "<thead><tr>";
		if (this.loadCheckBox) {
			theadHtml += '<th class="center" style="width: 35px;"><label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label></th>';
		}
		theadHtml += '<th class="center" style="width: 50px;">序号</th>';
		for (var i = 0; i < columnNames.length; i++) {
			theadHtml += '<th class="center" style="'+this.styleNames[i]+'">'+this.columnNames[i]+'</th>';
		}
		if(this.optButtonId && isNotEmpty(this.optButtonId)){
			theadHtml += '<th class="center">操作</th>';
		}
		theadHtml += "</tr></thead>";
		var tbodyHtml = "<tbody>";
		if(data.length > 0){
			for (var i = 0; i < data.length; i++) {
				var trHtml = "<tr>";
				if (this.loadCheckBox) {
					trHtml += "<td class='center'><label class='pos-rel'><input type='checkbox' name='ids' value='"+data[i][idProperty]+"' class='ace' /><span class='lbl'></span></label></td>";
				}
				trHtml += '<td class="center" style="width: 50px;">'+(i+1)+'</td>';
				for (var j = 0; j < propertyNames.length; j++) {
					trHtml += '<td class="center" style="'+this.styleNames[j]+'">'+(isNotEmpty(data[i][propertyNames[j]])?data[i][propertyNames[j]]:"")+'</td>';
				}
				if(this.optButtonId && isNotEmpty(this.optButtonId)){
					var optHtml = document.getElementById(this.optButtonId).innerHTML;
					trHtml += '<td class="center"><div class="btn-group" id="'+(this.optButtonId+i)+'">';
					$.each($("#"+optButtonId).children(),function(index,dom){
						$(dom).attr("onclick",$(dom).attr("handle")+"("+JSON.stringify(data[i])+");");
						if($(dom).attr("handle") == "invalid" && !data[i].valid){
							$(dom).remove();
						}
						if($(dom).attr("handle") == "revalid" && data[i].valid){
							$(dom).remove();
						}
					});
					trHtml += document.getElementById(this.optButtonId).innerHTML;
					document.getElementById(this.optButtonId).innerHTML = optHtml;
					trHtml +='</div></td>';
				}
				trHtml += "</tr>";
				tbodyHtml += trHtml;
			}
		}else{
			tbodyHtml += '<tr class="main_info"><td colspan="100" class="center">没有相关数据</td></tr>';
		}
		tbodyHtml += "</tbody>";
		document.getElementById(tableId).innerHTML = theadHtml+tbodyHtml;
		bindCheck(this.tableId);
		setFrameHeight();//数据加载完毕后重新设置frame大小
	}
}