(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('NodeHistoryDialogController', NodeHistoryDialogController);

    NodeHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NodeHistory'];

    function NodeHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NodeHistory) {
        var vm = this;

        vm.nodeHistory = entity;
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
            if (vm.nodeHistory.id !== null) {
                NodeHistory.update(vm.nodeHistory, onSaveSuccess, onSaveError);
            } else {
                NodeHistory.save(vm.nodeHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:nodeHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
