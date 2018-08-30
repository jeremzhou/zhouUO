(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ServerHistoryDetailController', ServerHistoryDetailController);

    ServerHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ServerHistory'];

    function ServerHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, ServerHistory) {
        var vm = this;

        vm.serverHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uapolloApp:serverHistoryUpdate', function(event, result) {
            vm.serverHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
