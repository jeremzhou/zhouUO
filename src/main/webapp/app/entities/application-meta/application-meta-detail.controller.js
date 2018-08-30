(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationMetaDetailController', ApplicationMetaDetailController);

    ApplicationMetaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'ApplicationMeta', 'Project'];

    function ApplicationMetaDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, ApplicationMeta, Project) {
        var vm = this;

        vm.applicationMeta = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('uapolloApp:applicationMetaUpdate', function(event, result) {
            vm.applicationMeta = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
