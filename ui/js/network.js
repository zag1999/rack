const BASEIP = "127.0.0.1"

const COMMONURL = "http://"+BASEIP+":8088/"
const EXCELURL = "http://"+BASEIP+":8088/"
const BASEURL = "http://"+BASEIP+":8088/"


//外网
// const COMMONURL = "http://82.156.176.146:8088/"
// const EXCELURL = "http://82.156.176.146:8088/"
// const BASEURL = "http://82.156.176.146:8088/"
const URLlIST = [
	'rack_info',
	'esign_bind',
	'people_info',
	'picking_record',
	'replenishment_record',
	'plan_setup',
	'outmaintain'
]
function Network(Vue,invoke, type, data, success, fail) {
	let uid = localStorage.getItem('uid')
	$.ajax({
		url: COMMONURL+invoke,
		type: type,
		dataType: "JSON",
		data: data,
		headers:{
			"userId":uid
		},
		async : false,
		timeout: 10000,
		success: function(res) {
			if (res.result == "success") {
				success(res);
			} else { //还有失败、未登录、没权限、维护中
				fail(res.msg);
			}
		},
		error: function(data, type) {
			console.log(data.status)
			if(data.status==401){
				window.parent.location.replace("login.html");
			}
			if (data.status) {
				Vue.$message({
					type: 'error',
					message: '已取消删除'
				});
				alert(data.status + data.responseText);
			} else {
				Vue.$message({
					type: 'error',
					message: '未知错误'
				});
			}	
		 
		}
	})
}
 const MyNetwork = function(invoke,type,data){
	 return new Promise((resolved,rejected)=>{
		 $.ajax({
		 		url: COMMONURL+invoke,
		 		type: type,
		 		dataType: "JSON",
		 		headers:{
		 			"userId":1
		 		},
		 		async : false,
		 		timeout: 10000,
		 		success: function(res) {
		 			if (res.result == "success") {
		 				resolved(res);
		 			} else { //还有失败、未登录、没权限、维护中
		 				rejected(res.msg);
		 			}
		 		},
		 		error: function(data, type) {
					console.log()
		 			if (data.status) {
		 				rejected(data.status + data.responseText);
		 			} else {
		 				rejected("未知错误");
		 			}
		 		}
		 	})
		 
	 })
 }
 

function UTCtoDate(UTCDateString, type) {
	if (!UTCDateString) {
		return '-';
	}

	function formatFunc(str) { //格式化显示
		return str > 9 ? str : '0' + str
	}
	var date2 = new Date(UTCDateString); //这步是关键
	var year = date2.getFullYear();
	var mon = formatFunc(date2.getMonth() + 1);
	var day = formatFunc(date2.getDate());
	var hour = date2.getHours();
	hour = formatFunc(hour);
	var min = formatFunc(date2.getMinutes());
	var sec = formatFunc(date2.getSeconds());
	var dateStr = "";
	if ("year" == type) {
		dateStr = year + '-' + mon + '-' + day;
	} else if ("time" == type) {
		dateStr = hour + ':' + min + ':' + sec;
	} else if ("excel" == type) {
		dateStr = year + mon + day + '_' + hour + min+ sec;
	} else {
		dateStr = year + '-' + mon + '-' + day + ' ' + hour + ':' + min + ':' + sec;
	}
	return dateStr;
}
