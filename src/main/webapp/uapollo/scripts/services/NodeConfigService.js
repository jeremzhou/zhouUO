appService.service('NodeConfigService', ['$resource', '$q', function ($resource, $q){
	var nodeconfig_resource = $resource('', {}, {
        find_nodeconfig: {
            method: 'GET',
            url: '/api/node-configs',
            isArray: true
        },
        create_nodeconfig: {
        	method: 'POST',
        	url: '/api/node-configs'
        },
        update_nodeconfig: {
        	method: 'PUT',
        	url: '/api/node-configs'
        },
        delete_nodeconfig: {
        	method: 'DELETE',
        	url: '/api/node-configs/:id'
        },
        find_nodeconfig_id: {
        	method: 'GET',
        	url: '/api/node-configs/:id'
        },
        findNodeConfigByApplicationMetaIdAndNodeId: {
        	method: 'GET',
        	url: '/api/v1/application-metas/:applicationMetaId/nodes/:nodeId/node-configs',
        	isArray: true
        },
        releaseNodeConfigByApplicationMetaIdAndNodeId: {
        	method: 'POST',
        	url: '/api/v1/application-metas/:applicationMetaId/nodes/:nodeId/node-configs/release',
        	isArray: true
        	
        }
    });
    return {
    	find_nodeconfig: function () {
            var d = $q.defer();
            nodeconfig_resource.find_nodeconfig({}, function (result) {
                d.resolve(result);
            }, function (result) {
                 d.reject(result);
            });
               return d.promise;
        },
        
        create_nodeconfig: function(nodeConfigDTO){
        	var d = $q.defer();
        	nodeconfig_resource.create_nodeconfig({}, nodeConfigDTO,
    							  function (result){
    								  d.resolve(result);
    							  },function (result){
    								  d.reject(result);
    							  });
        							return d.promise;
   		},
   		update_nodeconfig: function(nodeConfigDTO){
   			var d = $q.defer();
   			nodeconfig_resource.update_nodeconfig({}, nodeConfigDTO,
   								  function (result){
   									  d.resolve(result);
   								  },function (result){
   									  d.reject(result);
   								  });
   									return d.promise;
   		},
   		delete_nodeconfig: function(id){
   			var d = $q.defer();
   			nodeconfig_resource.delete_nodeconfig({
   									id : id		
    							 },function(result){
    								 d.resolve(result);
    							 },function(result){
    								d.reject(result); 
    							 });
   									return d.promise;
   		},
   		find_nodeconfig_id: function(id){
   			var d = $q.defer();
   			nodeconfig_resource.find_nodeconfig_id({
   								  id : id
   								},function(result){
   									d.resolve(result);
   								},function(result){
   									d.reject(result);
   								});
   			return d.promise;
   		},
   		findNodeConfigByApplicationMetaIdAndNodeId: function(applicationMetaId, nodeId){
   			var d = $q.defer();
   			nodeconfig_resource.findNodeConfigByApplicationMetaIdAndNodeId({
							   			applicationMetaId : applicationMetaId,
							   			nodeId : nodeId
   								}, function(result){
   									d.resolve(result);
   								}, function(result){
   									d.reject(result);
   								});
   			return d.promise;
   		},
   		releaseNodeConfigByApplicationMetaIdAndNodeId: function(applicationMetaId, nodeId, nodeConfigDTO){
   			var d = $q.defer();
   			nodeconfig_resource.releaseNodeConfigByApplicationMetaIdAndNodeId({
					   				applicationMetaId : applicationMetaId,
						   			nodeId : nodeId
								},nodeConfigDTO,function(result){
									d.resolve(result);
								}, function(result){
									d.reject(result);
								});
					return d.promise;
   		}
    }
}]);