(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationConfigHistoryDetailController', ApplicationConfigHistoryDetailController);

    ApplicationConfigHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ApplicationConfigHistory'];

    function ApplicationConfigHistoryDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ApplicationConfigHistory) {
        var vm = this;

        vm.applicationConfigHistory = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('uapolloApp:applicationConfigHistoryUpdate', function(event, result) {
            vm.applicationConfigHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
