appService.service('ApplicationService', ['$resource', '$q', function ($resource, $q){
	var resource = $resource('', {}, {
		find_application: {
			method: 'GET',
			url: '/api/applications',
			isArray: true
		},
		find_allapplication: {
			method: 'GET',
			url: '/api/v1/infomation/applications',
			isArray: true
		},
		create_application: {
			method: 'POST',
			url: '/api/applications'
		},
		update_application: {
			method: 'PUT',
			url: '/api/applications'
		},
		delete_application: {
			method: 'DELETE',
			url: '/api/applications/:id'
		},
		find_application_by_Id: {
			method: 'GET',
			url:'/api/applications/:id'
		},
		find_application_by_projectId: {
			method: 'GET',
			url: '/api/v1/projects/:projectId/applications',
			isArray: true
		},
		findApplicationByApplicationMetaIdServerId: {
			method: 'GET',
			url: '/api/v1/application-metas/:applicationMetaId/servers/:serverId/applications'
		},
		release_application_by_Id: {
			method: 'POST',
			url: '/api/v1/applications/:id/release'
		}
		
	});
	return {
		find_application: function(){
			var d = $q.defer();
			resource.find_application({}, function (result) {
                d.resolve(result); 
           }, function (result) {
               d.reject(result);
           	});
			 return d.promise;
		},
		find_allApplication: function(){
			var d = $q.defer();
			resource.find_allapplication({}, function (result) {
                d.resolve(result); 
           }, function (result) {
               d.reject(result);
           	});
			 return d.promise;
		},
		create_application: function(applicationDTO){
			var d = $q.defer();
			resource.create_application({},applicationDTO,
											function(result){
												d.resolve(result);
											}, function (result){
												d.reject(result);
											});
											return  d.promise;
		},
		update_application: function(applicationDTO){
			var d = $q.defer();
			resource.update_application({},applicationDTO,
									function(result){
										d.resolve(result);
									},function(result){
										d.reject(result);
									});
							return d.promise;
		},
		
		delete_application: function(id){
			var d = $q.defer();
			resource.delete_application({
										id : id
									},function(result){
										d.resolve(result);
									}, function(result){
										d.reject(result);
									});
									return d.promise;
		},
		find_application_by_Id: function(id){
			var d = $q.defer();
			resource.find_application_by_Id({
											id : id
										}, function(result){
											d.resolve(result);
										}, function(result){
											d.reject(result);
										});
								return d.promise;
		},
		find_application_by_projectId: function(projectId){
			var d = $q.defer();
			resource.find_application_by_projectId({
											projectId:projectId
										}, function(result){
											d.resolve(result);
										}, function(result){
											d.reject(result);
										});
										return d.promise;
		},
		findApplicationByApplicationMetaIdServerId: function(applicationMetaId, serverId){
			var d = $q.defer();
			resource.findApplicationByApplicationMetaIdServerId({
									          applicationMetaId : applicationMetaId,
									          serverId : serverId
										}, function(result){
											d.resolve(result);
										}, function(result){
											d.reject(result);
										});
									return	d.promise;
		},
		release_application_by_Id: function(id){
			var d = $q.defer();
			resource.release_application_by_Id({
											id : id
										}, function(result){
											d.resolve(result);
										}, function(result){
											d.reject(result);
										});
									return	d.promise;
		}
	}
}]);