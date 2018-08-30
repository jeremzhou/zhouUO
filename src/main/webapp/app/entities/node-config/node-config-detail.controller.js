(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('NodeConfigDetailController', NodeConfigDetailController);

    NodeConfigDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'NodeConfig', 'ApplicationMeta', 'Node'];

    function NodeConfigDetailController($scope, $rootScope, $stateParams, previousState, entity, NodeConfig, ApplicationMeta, Node) {
        var vm = this;

        vm.nodeConfig = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uapolloApp:nodeConfigUpdate', function(event, result) {
            vm.nodeConfig = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
