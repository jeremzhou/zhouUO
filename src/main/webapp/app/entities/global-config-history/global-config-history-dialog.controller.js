(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('GlobalConfigHistoryDialogController', GlobalConfigHistoryDialogController);

    GlobalConfigHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GlobalConfigHistory'];

    function GlobalConfigHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GlobalConfigHistory) {
        var vm = this;

        vm.globalConfigHistory = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.globalConfigHistory.id !== null) {
                GlobalConfigHistory.update(vm.globalConfigHistory, onSaveSuccess, onSaveError);
            } else {
                GlobalConfigHistory.save(vm.globalConfigHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:globalConfigHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
