(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('PrivateConfigDialogController', PrivateConfigDialogController);

    PrivateConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PrivateConfig', 'Application'];

    function PrivateConfigDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PrivateConfig, Application) {
        var vm = this;

        vm.privateConfig = entity;
        vm.clear = clear;
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
            if (vm.privateConfig.id !== null) {
                PrivateConfig.update(vm.privateConfig, onSaveSuccess, onSaveError);
            } else {
                PrivateConfig.save(vm.privateConfig, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:privateConfigUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
