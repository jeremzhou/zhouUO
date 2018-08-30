applicationmeta_module.controller('ApplicationController', ['$rootScope', '$scope', '$window', '$location','toastr', 'AppUtil',
						  'ServerService', 'NodeService', 'ProjectService', 'ApplicationMetaService','ApplicationService', 
						applicationcontroller]);

function applicationcontroller($rootScope, $scope, $window, $location, toastr, AppUtil, ServerService,NodeService,
								ProjectService, ApplicationMetaService, ApplicationService){
	
	var urlParams = AppUtil.parseParams($location.$$url);
	var applicationMetaid = urlParams.applicationMetaId;
	var projectid = urlParams.projectId;
	$scope.applicationId = applicationMetaid;
	$scope.projectid = projectid;
	
	$scope.serverNames = [];
	$scope.nodeNames = [];
	$scope.projectNames = []; 
	$scope.applicationMetaNames = [];
	$scope.applications = [];
	$scope.application = [];
	$scope.applicationMetas = [];
	
	$scope.createApplication = createApplication;
	$scope.editApplication = editApplication;
	$scope.preDeleteApplication = preDeleteApplication;
	$scope.doApplication = doApplication;
	$scope.changeSelectProject = changeSelectProject;
	$scope.changeSelectNode = changeSelectNode;
	$scope.changeSelectApplicationMeta = changeSelectApplicationMeta;
	$scope.changeSelectServer = changeSelectServer; 
	$scope.changeServer = changeServer;
	$scope.changeNode = changeNode;
	
	
	init();
	
	var projectIds = "";
	var applicationMetaIds = "";
	var serverIds = "";
	
	var getApplicationMetaID = "";
	var getServerID = "";
	
	function init(){
		refreshAllApplication();
		initProject();
	}
	
	function changeSelectApplicationMeta(){
		if($scope.applicationManagementProjectName == null){
			toastr.info("请先选择项目名称");
		}
	}
	
	function changeServer(){
		if($scope.applicationManagementNodeName == null && $scope.applicationNodeName == null ){
			toastr.info("请先选择节点名称和应用名称");
		}
	
	}
	
	function changeNode(){
		if($scope.applicationManagementProjectName == null){
			toastr.info("请先选择项目名称");
		}
	}
	
	/* 查询出所有的应用名称、服务器ip、节点名称、项目名称   */
	function refreshAllApplication(){
		
		ApplicationService.find_allApplication().then(function(result){
			if(!result){
				return;
			}
			result.forEach(function(application){
				$scope.applications.push(application);
				ApplicationMetaService.findApplicationMetaById(application.applicationMetaId).then(function(result){
					if(!result){
						return;
					}
					$scope.applicationMetas.push(result);
				});
			});
		});
	}
	
	/* 查询出所有的供应用选择   */
	function initProject(){
		ProjectService.find_project().then(function(result){
			result.forEach(function(project){
				$scope.projectNames.push(project);
			});
		});
	}
	
	function changeSelectProject(){
		$scope.nodeNames = [];
		$scope.applicationMetaNames = [];
		if($scope.tableViewType == 'update'){
			$scope.applicationpageMetaName = "";
			$scope.applicationNodeName = "";
			$scope.applicationServerName = "";
		}
		ProjectService.find_project_by_name($scope.applicationManagementProjectName.name)
					  .then(function(result){
						  if(!result){
			 					return;
			 			  }
						  projectIds = result.id;
						  NodeService.findNodeByProjectId(result.id)
						  			 .then(function(result){
						  				if(!result){
						 					return;
						 				}
						  				result.forEach(function(node){
						  					$scope.nodeNames.push(node);
						  				});
						  			 });
						  ApplicationMetaService.findApplicationMetaByProjectId(result.id)
						  						.then(function(result){
						  							if(!result){
									 					return;
									 				}
						  							result.forEach(function(applicationMeta){
						  								$scope.applicationMetaNames.push(applicationMeta);
//						  								applicationMetaIds = applicationMeta.id;
						  							});
						  						});
					  });
		changeSelectServer();
	}
	
	function changeSelectNode(){
		$scope.serverNames = [];
		var temporaryApplicationMetaName = "";
		if($scope.applicationManagementApplicationMetaName == undefined || $scope.applicationManagementApplicationMetaName == ""){
			temporaryApplicationMetaName = $scope.applicationpageMetaName;
		}else{
			temporaryApplicationMetaName = $scope.applicationManagementApplicationMetaName.name;
		}
		ApplicationMetaService.findApplicationMetaByProjectIdAndName(projectIds,temporaryApplicationMetaName).then(function(result){
			if(!result){
				return;
			}
			applicationMetaIds = result.id;
		})
		
		NodeService.findNodeByProjectIdAndName(projectIds, $scope.applicationManagementNodeName.name)
					.then(function(result){
						if(!result){
							return;
						}
						ServerService.findServerByApplicationMetaIdAndNodeId(applicationMetaIds, result.id)
									 .then(function(result){
										 if(!result){
											return;
										 }
										 result.forEach(function(server){
											 $scope.serverNames.push(server); 
											 serverIds = server.id;
										 });
									 });
					});
		changeSelectServer();
	}
	
	
	function createApplication(application){
		$scope.applicationManagementProjectName = "";
		$scope.applicationNodeName = "";
		$scope.applicationServerName = "";
		$scope.applicationpageMetaName = "";
		$scope.tableViewType = 'create' ;
		
		AppUtil.showModal("#applicationModal");
		$scope.applicationManagement = [];
		
	}
	
	function editApplication(application){
		$scope.applicationMetaNames = [];
		$scope.serverNames = [];
		$scope.tableViewType = 'update';
		$scope.applicationManagementId = application.id;
		var i = 0;
		for(i in $scope.projectNames){
			if(application.projectName == $scope.projectNames[i].name){
				$scope.applicationManagementProjectName =  $scope.projectNames[i];
			}
		}
		$scope.applicationpageMetaName = application.applicationMetaName;
		$scope.applicationNodeName = application.nodeName;
		$scope.applicationServerName = application.serverIp;
		AppUtil.showModal("#applicationModal");
		ProjectService.find_project_by_name($scope.applicationManagementProjectName.name).then(function(result){
			if(!result){
				return;
			}
			 ApplicationMetaService.findApplicationMetaByProjectId(result.id)
				.then(function(result){
					if(!result){
						return;
					}
					result.forEach(function(applicationMeta){
						$scope.applicationMetaNames.push(applicationMeta);
					});
				});
			 ServerService.find_server_by_project(result.id).then(function(result){
				 if(!result){
					 return;
				 }
				 result.forEach(function(server){
					 $scope.serverNames.push(server);
				 })
				 
			 })
			 
		})
		
	}
	
	function preDeleteApplication(application){
		ApplicationService.delete_application(application.id)
						  .then(function(result){
							  toastr.success("删除应用成功");
							  setTimeout(function () {
		                            $window.location.reload();
		                        }, 1000)
						  },function(result){
							  AppUtil.showErrorMsg(result, '删除应用失败');
						  });
	}
	
	function changeSelectServer(){
		var temporaryNodeName = "";
		var temporaryApplicationMetaName = "";
		var temporaryServerName = "";
		if($scope.applicationManagementApplicationMetaName == undefined || $scope.applicationManagementApplicationMetaName == ""){
			temporaryApplicationMetaName = $scope.applicationpageMetaName;
		}else{
			temporaryApplicationMetaName = $scope.applicationManagementApplicationMetaName.name;
		}
		if($scope.applicationManagementNodeName == undefined || $scope.applicationManagementNodeName == "" 
		||	$scope.applicationManagementNodeName == null ){
			temporaryNodeName = $scope.applicationNodeName;
		}else{
			temporaryNodeName = $scope.applicationManagementNodeName.name;
		}
		if($scope.applicationManagementServerName == undefined || $scope.applicationManagementServerName == ""){
			temporaryServerName = $scope.applicationServerName;
		}else{
			temporaryServerName = $scope.applicationManagementServerName.ip;
		}
		ProjectService.find_project_by_name($scope.applicationManagementProjectName.name).then(function(result){
			if(!result){
				return;
			}
			NodeService.findNodeByProjectIdAndName(result.id, temporaryNodeName).then(function(result){
				if(!result){
					return;
				}
				ServerService.findServerByNodeIdAndIp(result.id, temporaryServerName).then(function(result){
					 getServerID = result.id;
				});
			});
			ApplicationMetaService.findApplicationMetaByProjectIdAndName(result.id, temporaryApplicationMetaName).then(function(result){
				if(!result){
				}
				 getApplicationMetaID = result.id;
			});
		});
	}
	
	function doApplication(){
		changeSelectServer();
		var type = $scope.tableViewType;
		 if(type == 'create'){
				var management = {"applicationMetaId":getApplicationMetaID ,"createTime":0,"modifyTime":0,"serverId": getServerID};
				ApplicationService.create_application(management)
								  .then(function(result){
									  toastr.success("添加应用成功");
									  setTimeout(function () {
				                            $window.location.reload();
				                        }, 1000)
								  },function(result){
									  AppUtil.showErrorMsg(result, '添加应用失败');
								  })
			}else if(type == 'update'){
				if(getApplicationMetaID == "" || getServerID == ""){
					toastr.info("请重新修改");
				}
				var management = {"applicationMetaId":getApplicationMetaID ,"createTime":0, "id":$scope.applicationManagementId ,"modifyTime":0,"serverId":getServerID};
				ApplicationService.update_application(management)
								  .then(function(result){
									  toastr.success("修改应用成功");
									  setTimeout(function () {
				                            $window.location.reload();
				                        }, 1000)
								  },function(result){
									  AppUtil.showErrorMsg(result, '修改应用失败');
								  });
			}
	}
	
	
}