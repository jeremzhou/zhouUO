(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('GlobalConfigDetailController', GlobalConfigDetailController);

    GlobalConfigDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GlobalConfig', 'ApplicationMeta'];

    function GlobalConfigDetailController($scope, $rootScope, $stateParams, previousState, entity, GlobalConfig, ApplicationMeta) {
        var vm = this;

        vm.globalConfig = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('uapolloApp:globalConfigUpdate', function(event, result) {
            vm.globalConfig = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
