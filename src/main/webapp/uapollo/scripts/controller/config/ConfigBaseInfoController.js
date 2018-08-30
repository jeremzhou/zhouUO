application_module.controller("ConfigBaseInfoController",
                              ['$rootScope', '$scope','$window', '$location', 'toastr', 'AppUtil', 'ProjectService','ApplicationService',
                            	'NodeService','ServerService','ApplicationMetaService', 'PrivateConfigService', 'NodeConfigService','GlobalConfigService', ConfigBaseInfoController]);

function ConfigBaseInfoController($rootScope, $scope, $window, $location, toastr, AppUtil, ProjectService, ApplicationService, NodeService,ServerService,ApplicationMetaService,
									PrivateConfigService, NodeConfigService, GlobalConfigService) {

	var urlParams = AppUtil.parseParams($location.$$url);
	var applicationMetaid = urlParams.applicationMetaId;
	var projectid = urlParams.projectId;
	$(".J_appFound").removeClass("hidden");
	$('.config-item-container').removeClass('hide');
	
	$rootScope.applicationMetaId =applicationMetaid;
	$scope.goToServerList = goToServerList; 
	$rootScope.goToConfig = goToConfig;
	$rootScope.preDeleteItem = preDeleteItem;
	$rootScope.editItem = editItem;
	$rootScope.createItem = createItem;
	$rootScope.publish = publish;
	
	$rootScope.applicationMetaConfigs = [];
	$scope.nodeNames = [];
	$rootScope.nodeConfigs = [];
	$rootScope.privateConfigs = [];
	$scope.globalConfigs = [];

	
	var applicationID = "";
	var nodeConfigId = "";
	var privateConfigId = "";
    init();
    
    function init(){
    	loadConfig();
    }
    
    function loadConfig(){
    	/*$rootScope.globalConfigs = [];*/
    	/* 查询应用全局配置信息*/
        GlobalConfigService.find_globalconfig_applicationMetaId(applicationMetaid)
        				   .then(function(result){
        					   if(!result){
        						   $rootScope.hasModifyPermission = false;
        						   return;
        					   }
        					   result.forEach(function (globalconfig){
        						   $scope.globalConfigs.push(globalconfig);
        						   $rootScope.hasModifyPermission = true;
        					   });
        				   });
        
        NodeService.findNodeByProjectId(projectid).then(function(result){
        	if(!result){
        		return;	
        	}
        	result.forEach(function(node){
        		$scope.nodeNames.push(node);
        	});
        });
        /* 查询应用默认配置信息   */
        ApplicationMetaService.findApplicationMetaById(applicationMetaid)
        					 .then(function(result){
    					    	if(!result){
    					    		return;
    					    	}
    					    	$rootScope.applicationMetaConfigs = JSON.parse(result.configContent);
        });
    }
    
    
    function goToServerList(nodeId){
    	nodeConfigId = nodeId;
    	if(nodeConfigId != null || nodeConfigId != ""){
    		$rootScope.releaseNodeConfigStyle = "btn btn-success btn-sm";
    		$rootScope.createNodeConfigStyle = "btn btn-primary btn-sm";
    	}
    	$scope.serverNames = [];
    	$rootScope.nodeId = nodeId;
    	$rootScope.nodeConfigs = [];
  		ServerService.findServerByApplicationMetaIdAndNodeId(applicationMetaid, nodeId)
    				.then(function (result){
    					if(!result){
    						toastr.info("没有添加服务器");
    						return;
    				     }
    					result.forEach(function(server){
    						$scope.serverNames.push(server);
    						$rootScope.flag = true;
    					});
    			});
		$rootScope.flag = false;

  				
    	/* 查询节点应用配置信息  */
    	NodeConfigService.findNodeConfigByApplicationMetaIdAndNodeId(applicationMetaid, nodeId)
    					 .then(function(result){
    						 if(!result){
    							 $rootScope.hasModifyPermission = false;
    							 return;
    						 }
    						 
    						 result.forEach(function(nodeconfig){
    							 $rootScope.nodeConfigs.push(nodeconfig);
    							 $rootScope.hasModifyPermission = true;
    						 });
    					 });
    }
    
    /* 查询私有配置信息  */
    function goToConfig(serverId){
    	$rootScope.privateConfigs = [];
    	ApplicationService.findApplicationByApplicationMetaIdServerId(applicationMetaid, serverId)
    					  .then(function(result){
    						 $rootScope.applicationId = result.id;
    						 applicationID = result.id;
    						 if(applicationID != null || applicationID != ""){
    							 $rootScope.releasStyle = "btn btn-success btn-sm";	
    							 $rootScope.createStyle = "btn btn-primary btn-sm";
    								
    						 }
    						 if(!result){
    							 return;
    						 }
    						 PrivateConfigService.find_privateconfig_by_applicationId(result.id)
    						 					 .then(function(result){
    						 						privateConfigId = result.id;
    						 						 if(!result){
    						 							$rootScope.hasModifyPermission = false;
    						 							 return;
    						 						 }
    						 						 result.forEach(function(privateconfig){
    						 							$rootScope.privateConfigs.push(privateconfig);
    						 							$rootScope.hasModifyPermission = true;
    						 						 });
    						 					 });
    					  });
    }

    $scope.showNode = function (){
    	$window.location.href = '/uapollo/node.html?#/applicationMetaId=' + applicationMetaid + "&projectId=" +projectid;
    };
    
    $scope.showServer = function (){
    	$window.location.href = "/uapollo/server.html?#/applicationMetaId=" +applicationMetaid + "&projectId=" +projectid;
    };
    
    $scope.showApplication = function (){
    	$window.location.href = "/uapollo/application.html?#/applicationMetaId=" +applicationMetaid + "&projectId=" +projectid;
    }
    
    $rootScope.tableViewOperType = '';
    $rootScope.configViewType = '';
    
    function preDeleteItem(configId, num) {
        if(num == 1){
        	PrivateConfigService.delete_privateconfig(configId)
        						.then(function(result){
        							toastr.success("删除私有配置成功");
        							setTimeout(function () {
       				                 $window.location.reload();
       				            	}, 1000);
        						},function(result){
        							toastr.error(AppUtil.showErrorMsg(result),"删除私有配置失败");
        						});
        }else if(num == 2){
        	NodeConfigService.delete_nodeconfig(configId)
        					 .then(function(result){
        						 toastr.success("删除节点配置成功");
        						 setTimeout(function () {
    				                 $window.location.reload();
    				             }, 1000);
        					 },function(result){
        						 toastr.error(AppUtil.showErrorMsg(result),"删除节点配置失败");
        					 });
        }else if(num == 3){
        	GlobalConfigService.delete_globalconfig(configId)
        						.then(function(result){
        							toastr.success("删除全局配置成功");
        							 setTimeout(function () {
        				                 $window.location.reload();
        				             }, 1000);
        						},function(result){
        							toastr.error(AppUtil.showErrorMsg(result), "删除全局配置失败");
        						});
        }else if(num == 4){
        	toastr.info("默认配置不支持删除");
        }
        
    }

    //修改配置
    function editItem(editConfig, num) {
    	$rootScope.judge2 = true;
    	$rootScope.judge1 = false;
    		$rootScope.tableViewOperType = 'update';
        	if(num == 1){
        		$rootScope.configViewType = 'updatePrivateConfig';
        		$rootScope.config = editConfig;
        		$rootScope.configskey = editConfig.key;
        		$rootScope.configsValue = editConfig.value;
        	}else if(num ==2){
        		$rootScope.configViewType = 'updateNodeConfig';
        		$rootScope.config = editConfig;
        		$rootScope.configskey = editConfig.key;
        		$rootScope.configsValue = editConfig.value;
        	}else if(num ==3){
        		$rootScope.config = editConfig;
        		$rootScope.configskey = editConfig.key;
        		$rootScope.configsValue = editConfig.value;
        		$rootScope.configViewType = 'updateGlobalConfig';
        		
        	}else if(num == 4){
        		toastr.info("默认配置不支持修改");
        		return;
        	}
        	AppUtil.showModal('#itemModal');
        }
        
    //新增配置
    function createItem(num) {
    	$rootScope.judge2 = false;
    	$rootScope.judge1 = true;
    	$rootScope.tableViewOperType = 'create';
    	$rootScope.configkey = "";
    	if(num == 1){
    		if( applicationID == null || applicationID == "" ){
    			toastr.info("请选择服务器");
    			return;
    		}
    			$rootScope.configViewType = 'createPirvateConfig';
    	}else if(num == 2){
    		if( nodeConfigId == null || nodeConfigId == ""){
    			toastr.info("请选择节点");
    			return;
    		}
    		$rootScope.configViewType = 'createNodeConfig';
    	}else if(num == 3){
    		$rootScope.configViewType = 'createGlobalConfig';
    	}else if(num == 4){
    		toastr.info("默认配置不支持添加");
    		return;
    	}
	    	$rootScope.config = [];
	    	AppUtil.showModal('#itemModal');
    }
    
    
    //发布配置
	function publish(num){
    	if(num ==1 ){
    		if( privateConfigId == "undefind"  || privateConfigId == "" || 
    			$rootScope.privateConfigs == null || $rootScope.privateConfigs == "" ){
    			toastr.info("请选择关联服务器ip和添加配置");
    		}else{
	    		GlobalConfigService.release_globalconfig(applicationMetaid)
				   .then(function(result){
					   toastr.success("发布配置成功");
				   },function(result){
					   AppUtil.showErrorMsg(result);
					   toastr.error(AppUtil.showErrorMsg(result), "发布配置失败");
				   });
    		}
    	}else if (num ==2){
    		if( nodeConfigId == null || nodeConfigId =="" || 
    			$rootScope.nodeConfigs == "" || $rootScope.nodeConfigs == null){
    			toastr.info("请选择关联节点和添加配置");
    			return;
    		};
    		NodeConfigService.releaseNodeConfigByApplicationMetaIdAndNodeId(applicationMetaid, nodeConfigId)
						.then(function (result){
							toastr.success("发布配置成功");
						}, function (result){
							AppUtil.showErrorMsg(result);
							toastr.error(AppUtil.showErrorMsg(result), "发布配置失败");
						});
    	}else if (num == 3){
    		if($scope.globalConfigs == null || $rootScope.globalConfigs == ""){
    			toastr.info("请添加配置");
    			return;
    		}
    		
    		GlobalConfigService.release_globalconfig(applicationMetaid)
			   .then(function(result){
				   toastr.success("发布配置成功");
			   },function(result){
				   AppUtil.showErrorMsg(result);
				   toastr.error(AppUtil.showErrorMsg(result), "发布配置失败");
			   });
    	}else if (num == 4){
    		GlobalConfigService.release_globalconfig(applicationMetaid)
			   .then(function(result){
				   toastr.success("发布配置成功");
			   },function(result){
				   AppUtil.showErrorMsg(result);
				   toastr.error(AppUtil.showErrorMsg(result), "发布配置失败");
			   });
    	}
    }
    
}

