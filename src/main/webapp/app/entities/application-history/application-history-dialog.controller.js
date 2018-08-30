(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationHistoryDialogController', ApplicationHistoryDialogController);

    ApplicationHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ApplicationHistory'];

    function ApplicationHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ApplicationHistory) {
        var vm = this;

        vm.applicationHistory = entity;
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
            if (vm.applicationHistory.id !== null) {
                ApplicationHistory.update(vm.applicationHistory, onSaveSuccess, onSaveError);
            } else {
                ApplicationHistory.save(vm.applicationHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:applicationHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
