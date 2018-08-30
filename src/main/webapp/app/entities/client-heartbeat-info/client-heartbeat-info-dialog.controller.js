(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ClientHeartbeatInfoDialogController', ClientHeartbeatInfoDialogController);

    ClientHeartbeatInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ClientHeartbeatInfo'];

    function ClientHeartbeatInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ClientHeartbeatInfo) {
        var vm = this;

        vm.clientHeartbeatInfo = entity;
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
            if (vm.clientHeartbeatInfo.id !== null) {
                ClientHeartbeatInfo.update(vm.clientHeartbeatInfo, onSaveSuccess, onSaveError);
            } else {
                ClientHeartbeatInfo.save(vm.clientHeartbeatInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:clientHeartbeatInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
