(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationConfigDialogController', ApplicationConfigDialogController);

    ApplicationConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'ApplicationConfig', 'Application'];

    function ApplicationConfigDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, ApplicationConfig, Application) {
        var vm = this;

        vm.applicationConfig = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.applications = Application.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.applicationConfig.id !== null) {
                ApplicationConfig.update(vm.applicationConfig, onSaveSuccess, onSaveError);
            } else {
                ApplicationConfig.save(vm.applicationConfig, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:applicationConfigUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
