index_module.controller('IndexController', ['$scope', '$window', 'toastr', 'AppUtil', 
                                            'UserService','ProjectService','ApplicationService','ApplicationMetaService',
                                            IndexController]);

/*上面这种写法是为了防止闪屏*/
function IndexController($scope, $window, toastr, AppUtil, UserService, ProjectService, ApplicationService, ApplicationMetaService) {

    $("#app-list").removeClass("hidden");
    $scope.goToAppHomePage = goToAppHomePage;
    $scope.goToCreateAppPage = goToCreateAppPage;
    $scope.goToApplicationHomePage = goToApplicationHomePage;
    $scope.selectProjects = [];
    $scope.createdApplicationMetas = [];
    
    ProjectService.find_project().then(function (result){
    	if(!result){
			return;
		}
    	result.forEach(function (project){
    		$scope.selectProjects.push(project);
    	});
    });
    
    ApplicationMetaService.findApplicationMetas().then(function(result){
		 if(!result){
			 return;
		 }
		 result.forEach(function(applicationMeta){
			 $scope.createdApplicationMetas.push(applicationMeta);
		 });
	 });
    
    /*  根据项目查询应用    */
    function goToApplicationHomePage(projectId){
    	 $scope.createdApplicationMetas = [];
    	ApplicationMetaService.findApplicationMetaByProjectId(projectId).then(function(result){
    							  if(!result){
    					    		return;
    					    	  }
    							  result.forEach(function(applicationMeta){
    								  $scope.createdApplicationMetas.push(applicationMeta);
    							  });
    						  });
    }
    function goToCreateAppPage() {
        $window.location.href = "/uapollo/node.html";
    }

    function goToAppHomePage(applicationMetaId, projectId) {
    	 $window.location.href = "/uapollo/config.html?#/applicationMetaId=" + applicationMetaId + "&projectId=" +projectId;
    	
    }

}
