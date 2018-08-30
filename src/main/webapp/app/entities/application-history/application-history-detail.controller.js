(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationHistoryDetailController', ApplicationHistoryDetailController);

    ApplicationHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ApplicationHistory'];

    function ApplicationHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, ApplicationHistory) {
        var vm = this;

        vm.applicationHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uapolloApp:applicationHistoryUpdate', function(event, result) {
            vm.applicationHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
