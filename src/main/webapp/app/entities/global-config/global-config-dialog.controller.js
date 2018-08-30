(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('GlobalConfigDialogController', GlobalConfigDialogController);

    GlobalConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GlobalConfig', 'ApplicationMeta'];

    function GlobalConfigDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GlobalConfig, ApplicationMeta) {
        var vm = this;

        vm.globalConfig = entity;
        vm.clear = clear;
        vm.save = save;
        vm.applicationmetas = ApplicationMeta.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.globalConfig.id !== null) {
                GlobalConfig.update(vm.globalConfig, onSaveSuccess, onSaveError);
            } else {
                GlobalConfig.save(vm.globalConfig, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:globalConfigUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
