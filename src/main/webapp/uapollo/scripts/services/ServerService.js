appService.service('ServerService', ['$resource', '$q', function ($resource, $q){
	var server_resource = $resource('', {}, {
        find_server: {
            method: 'GET',
            url: '/api/servers',
            isArray: true
        },
        find_allserver: {
        	method: 'GET',
        	url : '/api/v1/infomation/servers',
        	isArray: true
        },
        create_server: {
        	method: 'POST',
        	url: '/api/servers'
        },
        update_server: {
        	method: 'PUT',
        	url: '/api/servers' 
        },
        delete_server: {
        	method: 'DELETE',
        	url: '/api/servers/:id'
        },
        find_server_by_id: {
        	method: 'GET',
        	url: '/api/servers/:id'
        },
        find_server_projectId: {
        	method: 'GET',
        	url: '/api/v1/projects/:projectId/servers',
        	isArray: true
        },
        findServerByApplicationMetaIdAndNodeId: {
        	method: 'GET',
        	url: '/api/v1/application-metas/:applicationMetaId/nodes/:nodeId/servers',
        	isArray: true
        },
        findServerByNodeIdAndIp: {
        	method: 'GET',
        	url: '/api/v1/servers'
        },
    });
    return {
    	find_server: function () {
            var d = $q.defer();
            server_resource.find_server({}, function (result) {
                d.resolve(result);
            }, function (result) {
                 d.reject(result);
            });
               return d.promise;
        },
        find_allserver: function(){
        	 var d = $q.defer();
             server_resource.find_allserver({}, function (result) {
                 d.resolve(result);
             }, function (result) {
                  d.reject(result);
             });
                return d.promise;
        },
        create_server: function(serverDTO){
        	var d = $q.defer();
        	server_resource.create_server({
    							  },serverDTO,
    							  function (result){
    								  d.resolve(result);
    							  },function (result){
    								  d.reject(result);
    							  });
        							return d.promise;
   		},
   		update_server: function(serverDTO){
   			var d = $q.defer();
   			server_resource.update_server({},serverDTO,
   										function (result){
   											d.resolve(result);
   										}, function (result){
   											d.reject(result);
   										});
   			return d.promise;
   		},
   		delete_server: function(id){
   			var d = $q.defer();
   			server_resource.delete_server({
   									id: id			
    							 },function(result){
    								 d.resolve(result);
    							 },function(result){
    								d.reject(result); 
    							 });
   				return d.promise;
   		},
   		find_server_by_id: function(id){
   			var d = $q.defer();
   			server_resource.find_server_by_id({
   									id : id
   								},function(result){
   									d.resolve(result);
   								},function(result){
   									d.reject(result);
   								});
   			return d.promise;
   		},
   		find_server_by_project: function(projectId){
   			var d = $q.defer();
   			server_resource.find_server_projectId({
   									projectId : projectId
   								},function(result){
   									d.resolve(result);
   								},function(result){
   									d.reject(result);
   								});
   			return d.promise;
   		}
   		,
   		findServerByApplicationMetaIdAndNodeId: function(applicationMetaId, nodeId){
   			var d= $q.defer();
   			server_resource.findServerByApplicationMetaIdAndNodeId({
   									applicationMetaId: applicationMetaId,
   									nodeId: nodeId
   								}, function(result){
   									d.resolve(result);
   								}, function(result){
   									d.reject(result);
   								});
   			return d.promise;
   		},
   		findServerByNodeIdAndIp: function(nodeId, ip){
   			var d = $q.defer();
   			server_resource.findServerByNodeIdAndIp({
   										nodeId : nodeId,
   										ip : ip
   									}, function(result){
   										d.resolve(result);
   									}, function(result){
   										d.reject(result);
   									});
   			return d.promise;
   		}
    }
}]);