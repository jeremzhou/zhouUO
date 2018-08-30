(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('NodeConfigHistoryDialogController', NodeConfigHistoryDialogController);

    NodeConfigHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NodeConfigHistory'];

    function NodeConfigHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NodeConfigHistory) {
        var vm = this;

        vm.nodeConfigHistory = entity;
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
            if (vm.nodeConfigHistory.id !== null) {
                NodeConfigHistory.update(vm.nodeConfigHistory, onSaveSuccess, onSaveError);
            } else {
                NodeConfigHistory.save(vm.nodeConfigHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:nodeConfigHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
