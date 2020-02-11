//拓展模块配置
//layui.config({
//	base : '/manage/back/lib/layui-v2.5.4/lay/extend/' //拓展模块的根目录
//}).extend({
//	treeSelect : 'treeSelect' //拓展模块别名
//});








//var CommonUtil = {
//	openIndex:'0',
//	/**
//	 * ajax post提交
//	 */
//	sendPost : function(reqData, url, successFun,errorFun) {
//		layui.jquery.ajax({
//			// 提交数据的类型 POST GET
//			type : "POST",
//			// 提交的网址
//			url : url,
//			//async: false,
//			// 提交的数据
//			data : {
//				//"reqData" : JSON.stringify(reqData)
//				"reqData" :  reqData
//			},
//			// 返回数据的格式"xml", "html", "script", "json", "jsonp","text"
//			datatype : "json",
//			// 请求成功返回后回调此方法
//			success : function(data){
//				if(successFun != null){
//					successFun(data);
//				}
//			},
//			// 请求出错时回调些方法
//			error : function(e){
//				if(errorFun != null){
//					errorFun(e);
//				}
//				
//			}
//		});
//	},
//	
//	
//	
//	sendPostFile : function(reqData, url, successFun,errorFun) {
//		layui.jquery.ajax({
//			// 提交数据的类型 POST GET
//			type : "POST",
//			// 提交的网址
//			url : url,
//			contentType: "multipart/form-data",
//			//async: false,
//			// 提交的数据
//			data : {
//				//"reqData" : JSON.stringify(reqData)
//				"reqData" :  reqData
//			},
//			// 返回数据的格式"xml", "html", "script", "json", "jsonp","text"
//			datatype : "json",
//			// 请求成功返回后回调此方法
//			success : function(data){
//				if(successFun != null){
//					successFun(data);
//				}
//			},
//			// 请求出错时回调些方法
//			error : function(e){
//				if(errorFun != null){
//					errorFun(e);
//				}
//				
//			}
//		});
//	},
//	/**
//	 * ajax post提交
//	 */
//	sendPostWithBlob : function(reqData, url, successFun,errorFun) {
//		layui.jquery.ajax({
//			// 提交数据的类型 POST GET
//			type : "POST",
//			// 提交的网址
//			url : url,
//			//async: false,
//			// 提交的数据
//			data : reqData,
//			// 返回数据的格式"xml", "html", "script", "json", "jsonp","text"
//			datatype : "json",
//			// 请求成功返回后回调此方法
//			success : function(data){
//				if(successFun != null){
//					successFun(data);
//				}
//				
//			},
//			// 请求出错时回调些方法
//			error : function(e){
//				if(errorFun != null){
//					errorFun(e);
//				}
//				
//			}
//		});
//	},
//	/**
//	 * ajax get提交
//	 */
//	sendGet : function(reqData, url, successFun,errorFun) {
//		layui.jquery.ajax({
//			// 提交数据的类型 POST GET
//			type : "GET",
//			// 提交的网址
//			url : url,
//			// 提交的数据
//			data : {
//				"reqData" : JSON.stringify(reqData)
//			},
//			// 返回数据的格式"xml", "html", "script", "json", "jsonp","text"
//			datatype : "json",
//			// 请求成功返回后回调此方法
//			success : function(data) {
//				if(successFun != null){
//					successFun(data);
//				}
//			},
//			// 请求出错时回调些方法
//			error : function(e) {
//				if(errorFun != null){
//					errorFun(e);
//				}
//			}
//		});
//	},
//	/**
//	 * 生成当前时间戳
//	 * 
//	 * @returns 返回当前时间戳
//	 */
//	genTimestamp : function() {
//		var time = new Date();
//		return time.getTime();
//	},
//	/**
//	 * 空判断
//	 * 
//	 * @param str
//	 *            字符串
//	 * @returns {Boolean} true 字符串为空， false 字符串不为空
//	 */
//	isEmpty : function(str) {// 
//		// 判断字符串是否为空
//		if (str == null || str == undefined || str == '') {
//			return true;
//		} else {
//			return false;
//		}
//	},
//	/**
//	 * 手机号正则验证
//	 * 
//	 * @param str
//	 *            手机号
//	 * @returns {Boolean} true 是手机格式 false 不是手机格式
//	 */
//	isPhone : function(str) {// 验证手机格式
//		if ((/^1(3|4|5|7|8)\d{9}$/.test(str))) {
//			return true;
//		}
//	}
//};
//
//var reqParams = {
//	sign : "",
//	reqData : "",
//	appKey : "",
//	timeStamp : ""
//};