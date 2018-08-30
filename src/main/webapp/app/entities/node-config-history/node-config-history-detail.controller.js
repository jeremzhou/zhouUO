(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('NodeConfigHistoryDetailController', NodeConfigHistoryDetailController);

    NodeConfigHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'NodeConfigHistory'];

    function NodeConfigHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, NodeConfigHistory) {
        var vm = this;

        vm.nodeConfigHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uapolloApp:nodeConfigHistoryUpdate', function(event, result) {
            vm.nodeConfigHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
