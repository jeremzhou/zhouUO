/*appService.service('IndexService', ['$resource', '$q', function('$resource', '$q'){
	var resource = $resource("", {}, {
		load_project: {
			method: 'GET',
			url: '/api/v1/projects',
			isArray: true
		},
		load_application:{
			method: 'GET',
			url: '/api/applications',
			isArray: true
		},
		load_application_by_project:{
			method: 'GET',
			url: '/api/v1/projects/{projectId}/applications'
		}
	});
	
	return{
		load_project: function (userId){
			var d = $q.defer();
            index_resource.load_project({
            							userId: userId	
            							}, function (result) {
                d.resolve(result);
            }, function (result) {
                 d.reject(result);
            });
               return d.promise;
		},
		load_application: function(){
			var d = $q.defer();
            index_resource.load_project({}, function (result) {
                d.resolve(result);
            }, function (result) {
                 d.reject(result);
            });
               return d.promise;
		},
		
		load_application_by_project: function(projectId){
			var d = $q.defer();
            index_resource.load_project({
            								projectId: projectId
            							}, function (result) {
                d.resolve(result);
            }, function (result) {
                 d.reject(result);
            });
               return d.promise;
		}
	}
}]);
*/