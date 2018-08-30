(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('PrivateConfigDetailController', PrivateConfigDetailController);

    PrivateConfigDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PrivateConfig', 'Application'];

    function PrivateConfigDetailController($scope, $rootScope, $stateParams, previousState, entity, PrivateConfig, Application) {
        var vm = this;

        vm.privateConfig = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uapolloApp:privateConfigUpdate', function(event, result) {
            vm.privateConfig = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
