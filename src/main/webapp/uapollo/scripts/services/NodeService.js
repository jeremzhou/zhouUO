appService.service('NodeService', ['$resource', '$q', function ($resource, $q){
	var resource = $resource('', {}, {
        findNode: {
            method: 'GET',
            url: '/api/nodes',
            isArray: true
        },
        findAllNode: {
        	method: 'GET',
        	url: '/api/v1/infomation/nodes',
        	isArray: true
        },
        createNode: {
        	method: 'POST',
        	url: '/api/nodes'
        },
        updateNode: {
        	method: 'PUT',
        	url: '/api/nodes'
        },
        deleteNode: {
        	method: 'DELETE',
        	url: '/api/nodes/:id'
        },
        findNodeById: {
        	method: 'GET',
        	url: '/api/nodes/:id'
        }
        ,
        findNodeByProjectId: {
        	method: 'GET',
        	url: '/api/v1/projects/:projectId/nodes',
        	isArray: true
        },
        findNodesByApplicationMetaId: {
        	method: 'GET',
        	url: '/api/v1/application-metas/:applicationMetaId/nodes',
        	isArray: true
        },
        findNodeByProjectIdAndName: {
        	method: 'GET',
        	url: '/api/v1/nodes'
        }
    });
    return {
    	findNode: function () {
            var d = $q.defer();
            resource.findNode({}, function (result) {
			                d.resolve(result);
			            }, function (result) {
			                 d.reject(result);
			            });
			               return d.promise;
        },
        findAllNode: function() {
        	var d = $q.defer();
        	resource.findAllNode({}, function (result) {
                d.resolve(result);
            }, function (result) {
                 d.reject(result);
            });
        	return d.promise;
        },
        createNode: function(nodeDTO){
        	var d = $q.defer();
        	resource.createNode({}, nodeDTO,
    							  function (result){
    								  d.resolve(result);
    							  },function (result){
    								  d.reject(result);
    							  });
        				return d.promise;
   		},
   		updateNode: function(nodeDTO){
   			var d = $q.defer();
   			resource.updateNode({}, nodeDTO,
   								  function (result){
   									  d.resolve(result);
   								  },function (result){
   									  d.reject(result);
   								  });
   						return d.promise;
   		},
   		deleteNode: function(id){
   			var d = $q.defer();
   			resource.deleteNode({
   									id : id	
    							 },function(result){
    								 d.resolve(result);
    							 },function(result){
    								d.reject(result); 
    							 });
   					return d.promise;
   		},
   		findNodeById: function(id){
   			var d = $q.defer();
   			resource.findNodeById({
   									id : id
   								},function (result){
   									d.resolve(result);
   								}, function(result){
   									d.reject(result);
   								});
   			   return d.promise;
   		},
   		
   		findNodeByProjectId: function(projectId){
   			var d = $q.defer();
   			resource.findNodeByProjectId({
   											projectId: projectId	
   										},function (result){
   											d.resolve(result);
   										},function(result){
   											d.reject(result);
   										});
   			  	return d.promise;
   		},
   		
   		findNodesByApplicationMetaId: function(applicationMetaId){
   			var d = $q.defer();
   			resource.findNodeByApplicationMetaId({
   												applicationMetaId : applicationMetaId
   										}, function (result){
   											d.resolve(result);
   										}, function (result){
   											d.reject(result);
   										});
   				return d.promise; 
   		},
   		
   		findNodeByProjectIdAndName: function(projectId, name){
   			var d = $q.defer();
   			resource.findNodeByProjectIdAndName({
   												projectId : projectId,
   												name : name
   											},function (result){
   												d.resolve(result);
   											}, function (result){
   												d.reject(result);
   											});
   				return d.promise;
   		}
    }
}]);