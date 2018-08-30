(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationMetaHistoryDetailController', ApplicationMetaHistoryDetailController);

    ApplicationMetaHistoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ApplicationMetaHistory'];

    function ApplicationMetaHistoryDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ApplicationMetaHistory) {
        var vm = this;

        vm.applicationMetaHistory = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('uapolloApp:applicationMetaHistoryUpdate', function(event, result) {
            vm.applicationMetaHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
