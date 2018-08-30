(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ClientHeartbeatInfoDetailController', ClientHeartbeatInfoDetailController);

    ClientHeartbeatInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ClientHeartbeatInfo'];

    function ClientHeartbeatInfoDetailController($scope, $rootScope, $stateParams, previousState, entity, ClientHeartbeatInfo) {
        var vm = this;

        vm.clientHeartbeatInfo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uapolloApp:clientHeartbeatInfoUpdate', function(event, result) {
            vm.clientHeartbeatInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
