(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationMetaHistoryDialogController', ApplicationMetaHistoryDialogController);

    ApplicationMetaHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'ApplicationMetaHistory'];

    function ApplicationMetaHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, ApplicationMetaHistory) {
        var vm = this;

        vm.applicationMetaHistory = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.applicationMetaHistory.id !== null) {
                ApplicationMetaHistory.update(vm.applicationMetaHistory, onSaveSuccess, onSaveError);
            } else {
                ApplicationMetaHistory.save(vm.applicationMetaHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:applicationMetaHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
