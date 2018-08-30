(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationDetailController', ApplicationDetailController);

    ApplicationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Application', 'ApplicationMeta', 'Server'];

    function ApplicationDetailController($scope, $rootScope, $stateParams, previousState, entity, Application, ApplicationMeta, Server) {
        var vm = this;

        vm.application = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uapolloApp:applicationUpdate', function(event, result) {
            vm.application = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
