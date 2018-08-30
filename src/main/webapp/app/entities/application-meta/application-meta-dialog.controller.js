(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationMetaDialogController', ApplicationMetaDialogController);

    ApplicationMetaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'ApplicationMeta', 'Project'];

    function ApplicationMetaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, ApplicationMeta, Project) {
        var vm = this;

        vm.applicationMeta = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.projects = Project.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.applicationMeta.id !== null) {
                ApplicationMeta.update(vm.applicationMeta, onSaveSuccess, onSaveError);
            } else {
                ApplicationMeta.save(vm.applicationMeta, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:applicationMetaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
