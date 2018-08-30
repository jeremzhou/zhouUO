(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('NodeDetailController', NodeDetailController);

    NodeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Node', 'Project'];

    function NodeDetailController($scope, $rootScope, $stateParams, previousState, entity, Node, Project) {
        var vm = this;

        vm.node = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uapolloApp:nodeUpdate', function(event, result) {
            vm.node = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
