(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ProjectDetailController', ProjectDetailController);

    ProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Project'];

    function ProjectDetailController($scope, $rootScope, $stateParams, previousState, entity, Project) {
        var vm = this;

        vm.project = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uapolloApp:projectUpdate', function(event, result) {
            vm.project = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
