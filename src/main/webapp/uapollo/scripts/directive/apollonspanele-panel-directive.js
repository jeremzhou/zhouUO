directive_module.directive('apollonspanele', directive);

function directive($window, toastr, AppUtil, ApplicationMetaService, NodeConfigService, ApplicationService, PrivateConfigService){
	return {
		restrict: 'E',
		templateUrl: '/uapollo/views/component/namespace-panel.html',
		replace: true,
		link: function (scope) {
			scope.goToServerList = goToServerList;
			scope.goToConfig = goToConfig ;
			
			function goToServerList(nodeId){
				NodeConfigService.findNodeConfigByApplicationMetaIdAndNodeId(applicationMetaid, nodeId)
				 .then(function(result){
					 if(!result){
						 scope.hasModifyPermission = false;
						 return;
					 }
					 result.forEach(function(nodeconfig){
						 scope.nodeConfigs.push(nodeconfig);
						 scope.hasModifyPermission = true;
						 console.log(nodeconfig);
					 });
				 });
			}
			
			function goToConfig(serverId){
		    	ApplicationService.findApplicationByApplicationMetaIdServerId(applicationMetaid, serverId)
		    					  .then(function(result){
		    						 if(!result){
		    							 return;
		    						 }
		    						 PrivateConfigService.find_privateconfig_by_applicationId(result.id)
		    						 					 .then(function(result){
		    						 						 if(!result){
		    						 							 return;
		    						 						 }
		    						 						 result.forEach(function(privateconfig){
		    						 							 scope.privateConfigs.push(privateconfig);
		    						 						 });
		    						 					 });
		    					  });
			}
			
		}
	}
}