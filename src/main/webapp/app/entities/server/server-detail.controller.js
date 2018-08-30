(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ServerDetailController', ServerDetailController);

    ServerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Server', 'Node'];

    function ServerDetailController($scope, $rootScope, $stateParams, previousState, entity, Server, Node) {
        var vm = this;

        vm.server = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uapolloApp:serverUpdate', function(event, result) {
            vm.server = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
