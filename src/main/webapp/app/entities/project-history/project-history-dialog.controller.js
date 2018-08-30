(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ProjectHistoryDialogController', ProjectHistoryDialogController);

    ProjectHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProjectHistory'];

    function ProjectHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProjectHistory) {
        var vm = this;

        vm.projectHistory = entity;
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
            if (vm.projectHistory.id !== null) {
                ProjectHistory.update(vm.projectHistory, onSaveSuccess, onSaveError);
            } else {
                ProjectHistory.save(vm.projectHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:projectHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
