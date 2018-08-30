(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('PrivateConfigHistoryDetailController', PrivateConfigHistoryDetailController);

    PrivateConfigHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PrivateConfigHistory'];

    function PrivateConfigHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, PrivateConfigHistory) {
        var vm = this;

        vm.privateConfigHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uapolloApp:privateConfigHistoryUpdate', function(event, result) {
            vm.privateConfigHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
