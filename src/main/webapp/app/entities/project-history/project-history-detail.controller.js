(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ProjectHistoryDetailController', ProjectHistoryDetailController);

    ProjectHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProjectHistory'];

    function ProjectHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, ProjectHistory) {
        var vm = this;

        vm.projectHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uapolloApp:projectHistoryUpdate', function(event, result) {
            vm.projectHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
