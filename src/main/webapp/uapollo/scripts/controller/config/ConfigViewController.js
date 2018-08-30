application_module.controller("ConfigViewController",
                              ['$scope', '$location' ,'toastr', 'AppUtil', 'ConfigService', 'ApplicationMetaService',
                              'NodeConfigService', 'PrivateConfigService', 'GlobalConfigService',
                              controller]);

function controller($scope, $location, toastr, AppUtil, ConfigService, ApplicationMetaService,
						NodeConfigService, PrivateConfigService, GlobalConfigService ) {
	$('.config-item-container').removeClass('hide');
	
	var urlParams = AppUtil.parseParams($location.$$url);
	var applicationMetaid = urlParams.applicationMetaId;
    $scope.preDeleteItem = preDeleteItem;
    $scope.deleteItem = deleteItem;
    $scope.editItem = editItem;
    $scope.createItem = createItem;
	$scope.config = 'delete';
	 
	$scope.privateConfigs = [];
	$scope.applicationMetaConfigs = [];
    ApplicationMetaService.findApplicationMetaById(applicationMetaid)
    					 .then(function(result){
					    	if(!result){
					    		return;
					    	}
					    	$scope.applicationMetaConfigs = JSON.parse(result.configContent);
    });
    
    PrivateConfigService.find_privateconfig_by_applicationId(applicationMetaid)
    					.then(function(result){
    						if(!result){
    							return;
    						}
    						result.forEach(function (privateConfig){
    							$scope.privateConfigs.push(provateConfig);
    						});
    					});

    NodeConfigService.findNodeConfigByApplicationMetaIdAndNodeId

    $scope.tableViewOperType = '',  $scope.item = {};
    $scope.toOperationNamespace;

    var toDeleteItemId = 0;

    function preDeleteItem(namespace, itemId, num) {
        if(num = 1){}
        
        $scope.toOperationNamespace = namespace;
        toDeleteItemId = itemId;

        $("#deleteConfirmDialog").modal("show");
    }

    function deleteItem() {
        ConfigService.delete_item($rootScope.pageContext.appId,
                                  $rootScope.pageContext.env,
                                  $rootScope.pageContext.clusterName,
                                  $scope.toOperationNamespace.baseInfo.namespaceName,
                                  toDeleteItemId).then(
            function (result) {
                toastr.success("删除成功!");
                EventManager.emit(EventManager.EventType.REFRESH_NAMESPACE,
                                  {
                                      namespace: $scope.toOperationNamespace
                                  });
            }, function (result) {
                toastr.error(AppUtil.errorMsg(result), "删除失败");
            });
    }

    //修改配置
    function editItem(namespace, toEditItem, num) {
        $scope.item = _.clone(toEditItem);

        if (namespace.isBranch || namespace.isLinkedNamespace) {
            var existedItem = false;
            namespace.items.forEach(function (item) {
                if (!item.isDeleted && item.item.key == toEditItem.key) {
                    existedItem = true;
                }
            });
            if (!existedItem) {
                $scope.item.lineNum = 0;
                $scope.item.tableViewOperType = 'create';
            } else {
               /* $scope.item.tableViewOperType = 'update';*/
            	if(num == 1){
            		$scope.item.configViewType = 'updatePrivateConfig';
            	}else if(num ==2){
            		$scope.item.configViewType = 'updateNodeConfig';
            	}else if(num ==3){
            		$scope.item.configViewType = 'updateGlobalConfig';
            	}
            }
        } else {
        	if(num == 1){
        		$scope.item.configViewType = 'updatePrivateConfig';
        	}else if(num ==2){
        		$scope.item.configViewType = 'updateNodeConfig';
        	}else if(num ==3){
        		$scope.item.configViewType = 'updateGlobalConfig';
        	}
        	$scope.item.tableViewOperType = 'update';
        	$scope.tableViewType = 'updatePrivateConfig';
        	
        	$scope.toOperationNamespace = namespace;
            AppUtil.showModal('#itemModal');
        }
        
    }

    //新增配置
    function createItem(num) {
    	$scope.item = {
	            tableViewOperType: 'create'
	        };
    	if(num == 1) {
    		$scope.item.configViewType = 'createPirvateConfig';
    	}else if(num == 2){
    		$scope.item.configViewType = 'createNodeConfig';
    	}else if(num == 3){
    		$scope.item.configViewType = 'createGlobalConfig';
    	}
		
		AppUtil.showModal('#itemModal');
    	
    }

    var selectedClusters = [];
    $scope.collectSelectedClusters = function (data) {
        selectedClusters = data;
    };

}

