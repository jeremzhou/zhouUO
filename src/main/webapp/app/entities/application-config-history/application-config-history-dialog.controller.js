(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationConfigHistoryDialogController', ApplicationConfigHistoryDialogController);

    ApplicationConfigHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'ApplicationConfigHistory'];

    function ApplicationConfigHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, ApplicationConfigHistory) {
        var vm = this;

        vm.applicationConfigHistory = entity;
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
            if (vm.applicationConfigHistory.id !== null) {
                ApplicationConfigHistory.update(vm.applicationConfigHistory, onSaveSuccess, onSaveError);
            } else {
                ApplicationConfigHistory.save(vm.applicationConfigHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:applicationConfigHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
