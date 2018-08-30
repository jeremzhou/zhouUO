(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('GlobalConfigHistoryDetailController', GlobalConfigHistoryDetailController);

    GlobalConfigHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GlobalConfigHistory'];

    function GlobalConfigHistoryDetailController($scope, $rootScope, $stateParams, previousState, entity, GlobalConfigHistory) {
        var vm = this;

        vm.globalConfigHistory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uapolloApp:globalConfigHistoryUpdate', function(event, result) {
            vm.globalConfigHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
