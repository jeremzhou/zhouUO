appService.service('PrivateConfigService', ['$resource', '$q', function ($resource, $q){
	var privateconfig_resource = $resource('', {}, {
        find_privateconfig: {
            method: 'GET',
            url: '/api/private-configs',
            isArray: true
        },
        create_privateconfig: {
        	method: 'POST',
        	url: '/api/private-configs'
        },
        update_privateconfig: {
        	method: 'PUT',
        	url: '/api/private-configs'
        },
        delete_privateconfig: {
        	method: 'DELETE',
        	url: '/api/private-configs/:id'
        },
        find_privateconfig_by_id: {
        	method: 'GET',
        	url: '/api/private-configs/:id'
        },
        find_privateconfig_by_applicationId: {
        	method: 'GET',
        	url: '/api/v1/applications/:applicationId/private-configs',
        	isArray: true
        }
    });
    return {
    	find_privateconfig: function () {
            var d = $q.defer();
            privateconfig_resource.find_privateconfig({}, function (result) {
                d.resolve(result);
            }, function (result) {
                 d.reject(result);
            });
               return d.promise;
        },
        
        create_privateconfig: function(privateConfigDTO){
        	var d = $q.defer();
        	privateconfig_resource.create_privateconfig({},privateConfigDTO,
    							  function (result){
    								  d.resolve(result);
    							  },function (result){
    								  d.reject(result);
    							  });
        							return d.promise;
   		},
   		update_privateconfig: function(privateConfigDTO){
   			var d = $q.defer();
   			privateconfig_resource.update_privateconfig({},privateConfigDTO,
   								  function (result){
   									  d.resolve(result);
   								  },function (result){
   									  d.reject(result);
   								  });
   					return d.promise;
   		},
   		delete_privateconfig: function(id){
   			var d = $q.defer();
   			privateconfig_resource.delete_privateconfig({
   									id : id		
    							 },function(result){
    								 d.resolve(result);
    							 },function(result){
    								d.reject(result); 
    							 });
   			    return d.promise;
   		},
   		find_privateconfig_by_id: function(id){
   			var d = $q.defer();
   			privateconfig_resource.find_privateconfig_by_id({
   									id : id	
   								},function(result){
   									d.resolve(result);
   								},function(result){
   									d.reject(result);
   								});
   				return d.promise;
   		},
   		find_privateconfig_by_applicationId: function(applicationId){
   			var d = $q.defer();
   			privateconfig_resource.find_privateconfig_by_applicationId({
   								  applicationId : applicationId
   								}, function(result){
   									d.resolve(result);
   								}, function(result){
   									d.reject(result);
   								});
   				return d.promise;
   		}
   		
    }
}]);