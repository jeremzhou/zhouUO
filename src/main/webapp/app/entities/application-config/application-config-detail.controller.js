(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationConfigDetailController', ApplicationConfigDetailController);

    ApplicationConfigDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ApplicationConfig', 'Application'];

    function ApplicationConfigDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ApplicationConfig, Application) {
        var vm = this;

        vm.applicationConfig = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('uapolloApp:applicationConfigUpdate', function(event, result) {
            vm.applicationConfig = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
