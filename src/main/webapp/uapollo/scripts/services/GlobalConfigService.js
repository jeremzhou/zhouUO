appService.service('GlobalConfigService', ['$resource', '$q', function ($resource, $q){
	var globalconfig_resource = $resource('', {}, {
		find_globalconfig: {
            method: 'GET',
            url: '/api/global-configs',
            isArray: true
        },
        create_globalconfig: {
        	method: 'POST',
        	url: '/api/global-configs'
        },
        update_globalconfig: {
        	method: 'PUT',
        	url: '/api/global-configs'
        },
        delete_globalconfig: {
        	method: 'DELETE',
        	url: '/api/global-configs/:id'
        },
        find_globalconfig_id:{
        	method: 'GET',
        	url: '/api/global-configs/:id'
        },
        find_globalconfig_applicationMetaId: {
        	method: 'GET',
        	url: '/api/v1/application-metas/:applicationMetaId/global-configs',
        	isArray: true
        },
        release_globalconfig: {
        	method: 'POST',
        	url: '/api/v1/application-metas/:applicationMetaId/global-configs/release',
        	isArray: true
        }
    });
    return {
    	find_globalconfig: function () {
            var d = $q.defer();
            globalconfig_resource.find_globalconfig({}, function (result) {
                d.resolve(result);
            }, function (result) {
                 d.reject(result);
            });
               return d.promise;
        },
        
        create_globalconfig: function(globalConfigDTO){
        	var d = $q.defer();
        	globalconfig_resource.create_globalconfig({},globalConfigDTO,
    							  function (result){
    								  d.resolve(result);
    							  },function (result){
    								  d.reject(result);
    							  });
        							return d.promise;
   		},
   		update_globalconfig: function(globalConfigDTO){
   			var d = $q.defer();
   			globalconfig_resource.update_globalconfig({},globalConfigDTO,
   								  function (result){
   									  d.resolve(result);
   								  },function (result){
   									  d.reject(result);
   								  });
   									return d.promise;
   		},
   		delete_globalconfig: function(id){
   			var d = $q.defer();
   			globalconfig_resource.delete_globalconfig({
   									id: id		
    							 },function(result){
    								 d.resolve(result);
    							 },function(result){
    								d.reject(result); 
    							 });
   									return d.promise;
   		},
   		find_globalconfig_id: function(id){
   			var d = $q.defer();
   			globalconfig_resource.find_globalconfig_id({
					id: id		
			 },function(result){
				 d.resolve(result);
			 },function(result){
				d.reject(result); 
			 });
				return d.promise;
   		},
   		find_globalconfig_applicationMetaId: function(applicationMetaId){
	   			var d = $q.defer();
	   			globalconfig_resource.find_globalconfig_applicationMetaId({
	   				applicationMetaId : applicationMetaId
	   			}, function(result){
	   				d.resolve(result);
	   			}, function(result){
	   				d.reject(result);
	   			});
	   			return d.promise;
   		},
   		release_globalconfig: function(applicationMetaId, globalConfigDTO){
   			var d = $q.defer();
   			globalconfig_resource.release_globalconfig({
	   				applicationMetaId : applicationMetaId
	   			}, globalConfigDTO ,function(result){
	   				d.resolve(result);
	   			}, function(result){
	   				d.reject(result);
	   			});
	   			return d.promise;
   		}
    }
}]);