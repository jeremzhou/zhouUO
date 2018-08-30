node_module.controller("NodeController",
                              ['$rootScope', '$scope', '$location','$window', 'toastr', 'AppUtil', 'UserService', 'ProjectService', 'NodeService',
                               nodecontroller]);

function nodecontroller($rootScope, $scope, $location, $window, toastr, AppUtil,UserService, ProjectService, NodeService) {

	var urlParams = AppUtil.parseParams($location.$$url);
	var applicationMetaid = urlParams.applicationMetaId;
	var projectid = urlParams.projectId;
	$scope.applicationId = applicationMetaid;
	$scope.projectid = projectid;
	
	$scope.createNode = createNode;
	$scope.editNode = editNode;
	$scope.preDeleteNode = preDeleteNode;
	$scope.doNode = doNode;
	$scope.node = [];
	$scope.nodes = [];
	$scope.projects = [];
	$scope.projectNames = [];
	init();
	
	function init(){
		refreshAllNode();
		initProject();
	}
	
	function refreshAllNode(){
		NodeService.findAllNode().then(function(result){
			if(!result){
				return;
			}
			result.forEach(function(node){
				$scope.nodes.push(node);
			});
		});
	}
	
	/*查询出所有项目供节点选择*/
	function initProject(){
		ProjectService.find_project().then(function(result){
			if(!result){
				return;
			}
			result.forEach(function(project){
				$scope.projectNames.push(project);
				$scope.node.projectId = project.id;
			});
		});
	}
	
	function createNode(){
		$scope.nodeShowName = "";
		$scope.projectShowName = "";
		$scope.tableViewType = 'create';
	    $('#nodeModal').modal('show');
	    
	}
	
	function editNode(node){
		$scope.tableViewType = "update";
		$scope.nodeShow = node;
		$scope.nodeShowName = node.name;
		var i = 0;
		for( i in $scope.projectNames){
			if(node.projectName == $scope.projectNames[i].name){
				$scope.projectShowName = $scope.projectNames[i];
			}
		}
		AppUtil.showModal('#nodeModal');
	}
	
	function doNode(){
		var type = $scope.tableViewType;
		if($scope.projectShowName == undefined ){
			toastr.info("请选择项目名称");
		};
		ProjectService.find_project_by_name($scope.projectShowName.name)
		             .then(function(result){
		     var management = {'createTime':0,'modifyTime':0,'name':$scope.nodeShowName,'projectId':result.id};       	 
			 if(type == 'create'){
					NodeService.createNode(management).then(function (result){
						toastr.success("添加节点成功");
						 setTimeout(function () {
		                     $window.location.reload();
		                 }, 1000)
					},function (result){
						AppUtil.showErrorMsg(result, '添加节点失败');
					});
				}else if (type == 'update'){
					var Management = {'createTime':0,'id':$scope.nodeShow.id,'modifyTime':0,'name':$scope.nodeShowName,'projectId':result.id};
					NodeService.updateNode(Management).then(function (result){
						toastr.success("修改节点成功");
						 setTimeout(function () {
		                     $window.location.reload();
		                 }, 1000)	
					},function (result){
						AppUtil.showErrorMsg(result, '修改节点失败');
					});
				}
		  });
		
	}
	
	function preDeleteNode(node){
		NodeService.deleteNode(node.id).then(function (result){
			toastr.success("删除节点成功");
			 setTimeout(function () {
                 $window.location.reload();
             }, 1000)		
		}, function (result){
			toastr.error(AppUtil.showErrorMsg(result), "删除节点失败,请先删除节点对应的服务器和节点配置");
		});
	}

	
}




