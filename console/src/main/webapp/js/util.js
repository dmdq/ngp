/**********************************************************************************
 *                                      head                                      *
 *                              javascript prototype                              *
 **********************************************************************************/
 
 /**
 * 判断数组中是否含有指定的字符串
 */
Array.prototype.strContains=arrayStrContains;
function arrayStrContains(value) {
	for(var i = 0; i < this.length; i++) {
		if(this[i] == value) {
			return true;
		}
	}
	return false;
}
/**********************************************************************************
 *                                      footer                                      *
 *                              javascript prototype                              *
 **********************************************************************************/

/**********************************************************************************
 *                                      head                                      *
 *                              jquery dialog util                                *
 *                                                                                *
 *  deploy:                                                                       *
 *                                                                                *
 *  jquery.ui.all.css                                                             *
 *	demos.css                                                                     *
 *                                                                                *
 *	jquery-1.4.2.js                                                               *
 *	jquery.bgiframe-2.1.1.js                                                      *
 *	jquery.ui.core.js                                                             *
 *	jquery.ui.widget.js                                                           *
 *	jquery.ui.mouse.js                                                            *
 *	jquery.ui.draggable.js                                                        *
 *	jquery.ui.position.js                                                         *
 *	jquery.ui.resizable.js                                                        *
 *	jquery.ui.dialog.js                                                           *
 *	jquery.effects.core.js                                                        *
 *	jquery.effects.blind.js                                                       *
 *	jquery.effects.explode.js                                                     *
 *	jquery.ui.button.js                                                           *
 **********************************************************************************/

var defaultHeight = 450;
var defaultWidth = 600;
var defaultResizable = false;
var defaultShow = 'blind';
var defaultHide = 'blind';

/**
 * 创建一个查看信息类型的弹出窗口
 * @param 
 *{
 *  dialogId : dialog Id Value   (required)
 *  openerId : button Id Value   (required)
 *  height : height Value        (not required)
 *  width : width Value          (not required)
 *  ok : call back function      (not required)
 * }
 * @return
 */
function createViewDialog(settings) {
	//dialog div tag id
	var dialogId = settings.dialogId;
	//open button id
	var openerId = settings.openerId;
	//dialog's height
	var height = settings.height;
	if(height == '' || height == null) {
		height = defaultHeight;
	}
	//dialog's width
	var width = settings.width;
	if(width == '' || width == null) {
		width = defaultWidth;
	}
	
	$("#" + dialogId).dialog({
		resizable: defaultResizable,
		height: height,
		width: width,
		autoOpen: false,
		show: defaultShow,
		hide: defaultHide,
		modal: true,
		open: function() {
			$("select").css("display", "none");
		},
		close: function() {
			$("select").css("display", "");
		},
		buttons: {
			Ok: function() {
				if(settings.ok != '' && settings.ok != null) {
					settings.ok.call();
				}
				$(this).dialog('close');
			}
		}
	});
	
	$('#' + openerId).button().click(function() {
		$('#' + dialogId).dialog('open');
		return false;
	});
}

/**
 * 创建一个普通的弹出窗口
 * @param 
 *{
 *  dialogId : dialog Id Value   (required)
 *  openerId : button Id Value   (required)
 *  height : height Value        (not required)
 *  width : width Value          (not required)
 * }
 * @return
 */
function createDefaultDialog(settings) {
	//dialog div tag id
	var dialogId = settings.dialogId;
	//open button id
	var openerId = settings.openerId;
	//dialog's height
	var height = settings.height;
	if(height == '' || height == null) {
		height = defaultHeight;
	}
	//dialog's width
	var width = settings.width;
	if(width == '' || width == null) {
		width = defaultWidth;
	}
	
	$("#" + dialogId).dialog({
		resizable: defaultResizable,
		height: height,
		width: width,
		autoOpen: false,
		show: defaultShow,
		hide: defaultHide,
		modal: true,
		open: function() {
			$("select").css("display", "none");
		},
		close: function() {
			$("select").css("display", "");
		}
	});
	
	$('#' + openerId).button().click(function() {
		$('#' + dialogId).dialog('open');
		return false;
	});
}

/**
 * 直接打开一个普通的弹出窗口
 * @param 
 *{
 * 	dialogId : dialog Id Value   (required)
 *  height : height Value        (not required)
 *  width : width Value          (not required)
 * }
 * @return
 */
function openDefaultDialog(settings) {
	//dialog div tag id
	var dialogId = settings.dialogId;
	//dialog's height
	var height = settings.height;
	if(height == '' || height == null) {
		height = defaultHeight;
	}
	//dialog's width
	var width = settings.width;
	if(width == '' || width == null) {
		width = defaultWidth;
	}
	
	$("#" + dialogId).dialog({
		resizable: defaultResizable,
		height: height,
		width: width,
		autoOpen: true,
		show: defaultShow,
		hide: defaultHide,
		modal: true,
		open: function() {
			$("select").css("display", "none");
		},
		close: function() {
			$("select").css("display", "");
		}
	});
}

/**
 * 创建一个确认弹出窗口
 * @param 
 * {
 * 	dialogId : dialog Id Value   (required)
 *  openerId : button Id Value   (required)
 *  height : height Value        (not required)
 *  width : width Value          (not required)
 *  ok : call back function      (required)
 *  cancel : call back function  (not required)
 * }
 * @return
 */
function createConfirmDialog(settings) {
	//dialog div tag id
	var dialogId = settings.dialogId;
	//open button id
	var openerId = settings.openerId;
	//dialog's height
	var height = settings.height;
	if(height == '' || height == null) {
		height = defaultHeight;
	}
	//dialog's width
	var width = settings.width;
	if(width == '' || width == null) {
		width = defaultWidth;
	}
	
	$("#" + dialogId).dialog({
		resizable: defaultResizable,
		height: height,
		width: width,
		autoOpen: false,
		show: defaultShow,
		hide: defaultHide,
		modal: true,
		open: function() {
			$("select").css("display", "none");
		},
		close: function() {
			$("select").css("display", "");
		},
		buttons: {
			'确定': function() {
				settings.ok.call();
				$(this).dialog('close');
			},
			'取消': function() {
				if(settings.cancel != '' && settings.cancel != null) {
					settings.cancel.call();
				}
				$(this).dialog('close');
			}
		}
	});
	
	$('#' + openerId).button().click(function() {
		$('#' + dialogId).dialog('open');
		return false;
	});
}
/**********************************************************************************
 *                                     footer                                     *
 *                              jquery dialog util                                *
 **********************************************************************************/

/**
 * JS对象转JSON
 * @param {} o
 * @return {String}
 */
function obj2str(o){   
    var r = [];   
    if(typeof o =="string") return "\""+o.replace(/([\'\"\\])/g,"\\$1").replace(/(\n)/g,"\\n").replace(/(\r)/g,"\\r").replace(/(\t)/g,"\\t")+"\"";   
    if(typeof o =="undefined") return "";   
    if(typeof o == "object"){   
        if(o===null) return "null";   
        else if(!o.sort){   
            for(var i in o)   
                r.push(i+":"+obj2str(o[i]))   
            r="{"+r.join()+"}"  
        }else{   
            for(var i =0;i<o.length;i++)   
                r.push(obj2str(o[i]))   
            r="["+r.join()+"]"  
        }   
        return r;   
    }   
    return o.toString();   
}

/**
 * 过滤页面权限
 * @param [] names 
 * 		页面中需过滤的功能按钮
 * 		或者超链接的name属性值
 * 		数组([name1, name2, ...])
 */
function filterPrivilege(names, subFunNames) {
	var validFuns = subFunNames.split(",");
	for(var i = 0; i < names.length; i++) {
		var $buttons = $("*[name='" + names[i] + "']");
		if(validFuns.strContains(names[i])) {
			$buttons.each(function() {
				//$(this).css("display", "");			
			});
		} else {
			$buttons.each(function() {
				$(this).css("display", "none");			
			});
		}
		
		$buttons = $("#" + names[i]);
		if(validFuns.strContains(names[i])) {
			$buttons.css("display", "");			
		} else {
			$buttons.css("display", "none");			
		}
	}
}

function openNewWindow(url, wwidth, wheight) {
     var WWidth=400;
     var WHeight=400;
     if(wwidth != null & wwidth != '') {
     	WWidth = wwidth;
     }
     if(wheight != null & wheight != '') {
     	WHeight = wheight;
     }
     var   WLeft   =   Math.ceil((window.screen.width   -   WWidth)   /   2   );     
     var   WTop   =   Math.ceil((window.screen.height   -   WHeight)   /   2   );     
     var   features   =   
     'width='     +   WWidth    +   'px,'   +   
     'height='    +   WHeight   +   'px,'   +   
     'left='      +   WLeft     +   'px,'   +   
     'top='       +   WTop      +   'px,'   +   
     'fullscreen=no, toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes';
     window.open(url,  "",   features);
}

function openTopDialog(url) {
    var WWidth=600;
    var WHeight=400;
    var   WLeft   =   Math.ceil((window.screen.width   -   WWidth)   /   2   );     
    var   WTop   =   Math.ceil((window.screen.height   -   WHeight)   /   2   );     
    var   features   =   
    'dialogWidth='     +   WWidth    +   'px,'   +   
    'dialogHeight='    +   WHeight   +   'px,'   +   
    'dialogLeft='      +   WLeft     +   'px,'   +   
    'dialogTop='       +   WTop      +   'px,'   +   
    'fullscreen=no, toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes';
    return window.showModalDialog(url,  "",   features);
}