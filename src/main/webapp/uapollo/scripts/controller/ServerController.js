server_module.controller('ServerController', ['$rootScope', '$scope', '$window','$location' ,'toastr', 'AppUtil',
    					 'NodeService', 'ProjectService','ServerService', 
    					servercontroller]);

function servercontroller($rootScope, $scope, $window, $location, toastr, AppUtil, NodeService, ProjectService,
						 ServerService){
			
			var urlParams = AppUtil.parseParams($location.$$url);
			var applicationMetaid = urlParams.applicationMetaId;
			var projectid = urlParams.projectId;
			$scope.applicationId = applicationMetaid;
			$scope.projectid = projectid;
			
			
			$scope.createServer = createServer;
			$scope.editServer = editServer;
			$scope.preDeleteServer = preDeleteServer;
			$scope.changeselect = changeselect;
			$scope.doServer = doServer;
			$scope.changeShowSelect = changeShowSelect;
			
			$scope.nodeNames = [];
			$scope.projectNames = [];
			$scope.servers = [];
			$scope.server = [];
			$scope.serverIp = [];
			$scope.nodes = [];
			$scope.projects = [];
			init();
			
			function init(){
				refreshAllServer();
				initSelect();
					
			}

			function changeselect(){
				if($scope.serverManagement.projectName == null){
					toastr.info("请先选择项目名称");
				}
			}
			/* 查询出所有的服务器、节点名称、项目名称 */
			function refreshAllServer(){
				ServerService.find_allserver().then(function(result){
					if(!result){
						return;
					}
					result.forEach(function(server){
						$scope.servers.push(server);
					});
				});
			}
			
			/*提供选择*/
			function initSelect(){
				$scope.projectNames = [];
				ProjectService.find_project().then(function(result){
					if(!result){
						return;
					}
					result.forEach(function(project){
						$scope.projectNames.push(project);
					});
				});
			}
			
			function changeShowSelect(){
				$scope.nodeNames = [];
				ProjectService.find_project_by_name($scope.serverManagementProjectName.name)
							 .then(function(result){
								 if(!result){
										return;
								 }
								 NodeService.findNodeByProjectId(result.id)
								 			.then(function(result){
								 				if(!result){
								 					return;
								 				}
								 				result.forEach(function(node){
								 					$scope.nodeNames.push(node);
								 				});
								 			});
							 });
			}
					  
			function createServer(){
				$scope.serverManagementProjectName = "";
				$scope.serverManagementNodeName ="";
				$scope.serverManagementIp = "";
				$scope.serverNodeName = "";
				$scope.tableViewType = 'create';
				
				AppUtil.showModal("#serverModal");
				$scope.serverManagement = [];
			}
			
			function editServer(server){
				$scope.tableViewType = 'update';
				$scope.serverManagementId = server.id;
				$scope.serverManagementIp = server.ip;
				var i = 0;
				for(i in $scope.projectNames){
					if(server.projectName == $scope.projectNames[i].name){
						$scope.serverManagementProjectName =  $scope.projectNames[i];
					}
				}
				$scope.serverNodeName = server.nodeName;
				AppUtil.showModal("#serverModal");
			}
			
			function doServer(){
				var type = $scope.tableViewType;
				var temporaryNodeName = "";
				ProjectService.find_project_by_name($scope.serverManagementProjectName.name).then(function(result){
					if(!result){
						return;
					}
					if($scope.serverManagementNodeName == undefined || $scope.serverManagementNodeName == ""){
						temporaryNodeName = $scope.serverNodeName;
					}else{
						temporaryNodeName = $scope.serverManagementNodeName.name;
					}
					NodeService.findNodeByProjectIdAndName(result.id,temporaryNodeName)
					.then(function(result){
						if(!result){
							return;
						}
						if(type == 'create'){
							if($scope.serverManagementNodeName == undefined){
								toastr.info("请选择节点名称");
							}
							var Management = {"createTime":0,"ip":$scope.serverManagementIp,"modifyTime":0,"nodeId":result.id};
							ServerService.create_server(Management)
										 .then(function(result){
											 toastr.success("添加服务器成功");
											 setTimeout(function () {
						                            $window.location.reload();
						                        }, 1000)
										 },function(result){
											AppUtil.showErrorMsg(result, '添加服务器失败');
										 });
						}else if(type == 'update'){
							 var Management = {"createTime":0,"id":$scope.serverManagementId,"ip":$scope.serverManagementIp,"modifyTime":0,"nodeId":result.id};
							 ServerService.update_server(Management)
										 .then(function(result){
											 toastr.success("修改节点成功");
											 setTimeout(function () {
						                            $window.location.reload();
						                        }, 1000)	
										 },function(result){
											 AppUtil.showErrorMsg(result, '修改服务器失败');
										 });
						}
					});
				});
				
			}
			
			function preDeleteServer(server){
				ServerService.delete_server(server.id)
							 .then(function(result){
								 toastr.success("删除服务器成功");
								 setTimeout(function () {
			                            $window.location.reload();
			                        }, 1000)
							 },function(result){
								 toastr.error(AppUtil.showErrorMsg(result), "删除服务器失败,请先删除服务器对应的应用");
							 });
			}
}