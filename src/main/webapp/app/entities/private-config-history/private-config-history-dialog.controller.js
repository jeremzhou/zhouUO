(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('PrivateConfigHistoryDialogController', PrivateConfigHistoryDialogController);

    PrivateConfigHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PrivateConfigHistory'];

    function PrivateConfigHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PrivateConfigHistory) {
        var vm = this;

        vm.privateConfigHistory = entity;
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
            if (vm.privateConfigHistory.id !== null) {
                PrivateConfigHistory.update(vm.privateConfigHistory, onSaveSuccess, onSaveError);
            } else {
                PrivateConfigHistory.save(vm.privateConfigHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:privateConfigHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
