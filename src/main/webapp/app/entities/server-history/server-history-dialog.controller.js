(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ServerHistoryDialogController', ServerHistoryDialogController);

    ServerHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ServerHistory'];

    function ServerHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ServerHistory) {
        var vm = this;

        vm.serverHistory = entity;
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
            if (vm.serverHistory.id !== null) {
                ServerHistory.update(vm.serverHistory, onSaveSuccess, onSaveError);
            } else {
                ServerHistory.save(vm.serverHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:serverHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
