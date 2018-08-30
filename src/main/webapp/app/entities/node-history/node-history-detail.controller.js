(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('NodeHistoryDetailController', NodeHistoryDetailController);

    NodeHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'NodeHistory'];

    function NodeHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, NodeHistory) {
        var vm = this;

        vm.nodeHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uapolloApp:nodeHistoryUpdate', function(event, result) {
            vm.nodeHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
