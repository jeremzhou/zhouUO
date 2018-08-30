directive_module.directive('itemmodal', itemModalDirective);

function itemModalDirective(toastr, $sce, AppUtil,$location, $window, PrivateConfigService, NodeConfigService, GlobalConfigService, ApplicationMetaService) {
    return {
        restrict: 'E',
        templateUrl: '/uapollo/views/component/item-modal.html',
        link: function (scope, element, attrs) {

            var TABLE_VIEW_OPER_TYPE = {
                CREATEPRIVATE: 'createPirvateConfig',
                CREATENODE:'createNodeConfig',
                CREATEGLOBAL: 'createGlobalConfig',
                UPDATEPRIVATE: 'updatePrivateConfig',
                UPDATENODE: 'updateNodeConfig',
                UPDATEGLOBAL: 'updateGlobalConfig',
            };
            scope.doItem = doItem;
            
          
            scope.sourcekeys = {};
            scope.copykeys = {};
            
            ApplicationMetaService.findApplicationMetaById(scope.applicationMetaId).then(function(result){
		    	if(!result){
		    		return;
		    	}
		    	 scope.sourcekeys = JSON.parse(result.configContent);
		    	 scope.copykeys = angular.copy(scope.sourcekeys);
			 },function(result){
				 toastr.error(AppUtil.errorMsg(result), "load apps error");
			 });
            
            scope.shouldShowKeysList = false;
            scope.configkey = "";
            scope.configValue = "";
            
            scope.changeSearchKey = function (){
               scope.configkey= "";
         	   scope.copykeys = scope.sourcekeys;
         	   scope.shouldShowKeysList = true;
            } 
            
            $(".search-input").on("click", function (event) {
                event.stopPropagation();
            });

            $(document).on('click', function () {
                scope.$apply(function () {
                	scope.shouldShowKeysList = false;
                });
            });

            function clearkeysSelectedStatus() {
                	scope.shouldShowKeysList = false;
                    scope.copykeys = scope.sourcekeys;
                    scope.configkey = "";
            }
            var selectkey = {};
            scope.selectkeys = function (key){
            	selectkey = key;
            	scope.configkey = key;
            }
            
            //添加配置、修改配置
            function doItem() {
            	if(scope.configViewType == TABLE_VIEW_OPER_TYPE.CREATEPRIVATE){
            		var config = {'applicationId':scope.applicationId,'createTime':0,'key':scope.configkey,'modifyTime':0,'value':scope.configValue};
            		PrivateConfigService.create_privateconfig(config).then(function(result){
            			toastr.success("添加成功");
            			 setTimeout(function () {
		                     $window.location.reload();
		                 }, 1000)
            		},function(result){
            			 toastr.error(AppUtil.errorMsg(result), "添加失败");
            		});
            	}else if(scope.configViewType == TABLE_VIEW_OPER_TYPE.CREATENODE){
            		var config = {'applicationMetaId':scope.applicationMetaId,'createTime':0,'key':scope.configkey,'modifyTime':0,'nodeId':scope.nodeId,'value':scope.configValue};
            		NodeConfigService.create_nodeconfig(config).then(function(result){
            			toastr.success("添加成功");
            			 setTimeout(function () {
		                     $window.location.reload();
		                 }, 1000)
            		},function(result){
            			 toastr.error(AppUtil.errorMsg(result), "添加失败");
            		});
            	}else if(scope.configViewType == TABLE_VIEW_OPER_TYPE.CREATEGLOBAL){
            		var config = {'applicationMetaId':scope.applicationMetaId,'createTime':0,'key':scope.configkey,'modifyTime':0,'value':scope.configValue};
            		GlobalConfigService.create_globalconfig(config).then(function(result){
            			toastr.success("添加成功");
            			setTimeout(function () {
		                     $window.location.reload();
		                 }, 1000)
            		},function(result){
            			 toastr.error(AppUtil.errorMsg(result), "添加失败");
            		});
            	}else if(scope.configViewType == TABLE_VIEW_OPER_TYPE.UPDATEPRIVATE){
            		var config = {'applicationId':scope.config.applicationId,'createTime':0,'id':scope.config.id,'key':scope.configskey,'modifyTime':0,'value':scope.configsValue};
            		PrivateConfigService.update_privateconfig(config).then(function(result){
            			toastr.success("修改成功");
            			 setTimeout(function () {
		                     $window.location.reload();
		                 }, 1000)
            		},function(result){
            			 toastr.error(AppUtil.errorMsg(result), "修改失败");
            		});
            	}else if(scope.configViewType == TABLE_VIEW_OPER_TYPE.UPDATENODE){
            		var config = {'applicationMetaId':scope.applicationMetaId,'createTime':0,'id':scope.config.id,'key':scope.configskey,'modifyTime':0,'nodeId':scope.nodeId,'value':scope.configsValue};
            		NodeConfigService.update_nodeconfig(config).then(function(result){
            			toastr.success("修改成功");
            			 setTimeout(function () {
		                     $window.location.reload();
		                 }, 1000)
            		},function(result){
            			 toastr.error(AppUtil.errorMsg(result), "修改失败");
            		});
            	}else if(scope.configViewType == TABLE_VIEW_OPER_TYPE.UPDATEGLOBAL){
            		var applicationMetaid = scope.applicationMetaId;
            		var config = {'applicationMetaId':applicationMetaid,'createTime':0,'id':scope.config.id,'key':scope.configskey,'modifyTime':0,'value':scope.configsValue};
            		GlobalConfigService.update_globalconfig(config).then(function(result){
            			toastr.success("修改成功");
            			 setTimeout(function () {
		                     $window.location.reload();
		                 }, 1000)
            		},function(result){
            			 toastr.error(AppUtil.errorMsg(result), "修改失败");
            		});
            	}
            }
            
           
           
        }
    }
}



