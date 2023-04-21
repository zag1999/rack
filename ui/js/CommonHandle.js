/* 删除弹框 */
function DelDialog(Vue, title, param, interface, type, response) {
	let that = this
	Vue.$confirm('此操作将删除该' + title + '是否继续?', '提示', {
		confirmButtonText: '确定',
		cancelButtonText: '取消',
		type: 'warning'
	}).then(() => {
		Network(interface, type, param, function(res) {
			Vue.$message({
				type: 'success',
				message: '删除成功'
			});
			response(res)
		}, function(msg) {
			that.$message.error(msg);
		})
	}).catch(() => {
		Vue.$message({
			type: 'info',
			message: '已取消删除'
		});
	});
}
