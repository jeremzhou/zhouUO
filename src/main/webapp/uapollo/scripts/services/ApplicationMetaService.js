appService.service('ApplicationMetaService', ['$resource', '$q', function($resource, $q){
	 var resource = $resource('', {}, {
		 findApplicationMetas: {
			method: 'GET',
			url: '/api/application-metas',
			isArray: true
		 },
		 findApplicationMetaByProjectId:{
			 method: 'GET',
			 url: '/api/v1/projects/:projectId/application-metas',
			 isArray: true
		 },
		 findApplicationMetaById: {
			 method: 'GET',
			 url: '/api/application-metas/:id'
		 },
		 createApplicationMeta: {
			 method: 'POST',
			 url: '/api/application-metas'
		 },
		 updateApplicationMeta: {
			 method: 'PUT',
			 url: '/api/application-metas'
		 },
		 deleteApplicationMeta: {
			 method: 'DELETE',
			 url: '/api/application-metas/:id'
		 },
		 findApplicationMetaByProjectIdAndName: {
			 method: 'GET',
			 url: '/api/v1/application-metas'
		 }
	 });
	 return{
		 findApplicationMetas: function (){
			var d = $q.defer();
			resource.findApplicationMetas({}, function(result){
												d.resolve(result);	
										   }, function(result){
											    d.reject(result); 
										   });
					return d.promise;
		 },
		 findApplicationMetaByProjectId:function (projectId){
			 var d = $q.defer();
			 resource.findApplicationMetaByProjectId({
				 								projectId : projectId
			 									}, function(result){
			 										d.resolve(result);			
			 									}, function(result){
			 										d.reject(result);
			 									});
			 		return d.promise;
		 },
		 findApplicationMetaById: function(id){
			 var d = $q.defer();
			 resource.findApplicationMetaById({
											id : id	
										 },function(result){
											d.resolve(result);
										 },function(result){
											d.reject(result);
										 });
										 	return d.promise;
		 },
		 createApplicationMeta: function (applicationMetaDTO){
			 var d = $q.defer();
			 resource.createApplicationMeta({},applicationMetaDTO, function(result){
			 								d.resolve(result);
			 							}, function(result){
			 								d.reject(result);
			 							});
			 		return d.promise;
		 },
		 updateApplicationMeta: function (applicationMetaDTO){
			 var d = $q.defer();
			 resource.updateApplicationMeta({},applicationMetaDTO, function(result){
			 								d.resolve(result);
			 							}, function(result){
			 								d.reject(result);
			 							});
			 		return d.promise;
		 },
		 deleteApplicationMeta: function (id){
			 var d = $q.defer();
			 resource.deleteApplicationMeta({
				 							id : id	
			 							}, function(result){
			 								d.resolve(result);
			 							}, function(result){
			 								d.reject(result);
			 							});
			 		return d.promise;
		 },
		 findApplicationMetaByProjectIdAndName: function (projectId , name){
			 var d = $q.defer();
			 resource.findApplicationMetaByProjectIdAndName({
				 						projectId : projectId,
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