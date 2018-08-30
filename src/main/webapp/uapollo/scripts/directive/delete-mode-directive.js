/*directive_module.directive('deletenodemodal', deleteNodeDirective);

function deleteNodeDirective($window, $q, toastr, AppUtil, EventManager,NodeService){
	return{
		restrict: 'E',
		templateUrl: '../../views/component/delete-node-modal.html',
		transclude: true,
		replace: true,
		link: function(scope){
			scope.doDeleteNodeModal = doDeleteNodeModal;
			function doDeleteNodeModal(){
				NodeService.delete_node(scope.nodeName).then(
						function (result){
							toastr.success("删除成功!");
							AppUtil.hideModal('#deleteNodeModal');
						},function(result){
							toastr.error(AppUtil.errorMsg(result), "删除失败");
						})
			}
		}
	}
}

*/