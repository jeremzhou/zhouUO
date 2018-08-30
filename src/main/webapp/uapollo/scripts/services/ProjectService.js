appService.service('ProjectService', ['$resource', '$q', function($resource, $q){
	var resource =  $resource('', {}, {
        find_project: {
        	method: 'GET',
        	url: '/api/projects',
        	isArray: true
        },
        find_project_userId: {
        	method: 'GET',
        	url: '/api/v1/projects',
        	isArray: true
        },
        find_project_by_Id: {
        	method: 'GET',
        	url: '/api/projects/:id'
        },
        find_project_by_name: {
        	method: 'GET',
        	url: '/api/v1/projects/:name'
        }
    });
	return {
		find_project: function(){
			 var d = $q.defer();
			 resource.find_project({},
					 		function(result){
			 					d.resolve(result);
			 				},function(result){
			 					d.reject(result);
			 				});
			 return d.promise;
		},
	    find_project_userId: function(userId){
	    	var d = $q.defer();
	    	resource.find_project_userId({
	    						userId : userId
	    						},function(result){
	    							d.resolve(result);
	    						},function(result){
	    							d.reject(result);
	    						});
	    		return d.promise;
	    },
	    find_project_by_Id: function(id){
	    	var d =$q.defer();
	    	resource.find_project_by_Id({
	    								id : id
	    							},function(result){
	    								d.resolve(result);
	    							},function(result){
	    								d.reject(result);
	    							});
	    		return d.promise;
	    },
	    find_project_by_name: function(name){
	    	 var d = $q.defer();
	    	 resource.find_project_by_name({
	    		 				name : name
	    	 				}, function(result){
	    	 					d.resolve(result);
	    	 				}, function(result){
	    	 					d.reject(result);
	    	 				});
	    	 	return d.promise;
	    }
	}
	
}]);