(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('NodeConfigDialogController', NodeConfigDialogController);

    NodeConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NodeConfig', 'ApplicationMeta', 'Node'];

    function NodeConfigDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NodeConfig, ApplicationMeta, Node) {
        var vm = this;

        vm.nodeConfig = entity;
        vm.clear = clear;
        vm.save = save;
        vm.applicationmetas = ApplicationMeta.query();
        vm.nodes = Node.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.nodeConfig.id !== null) {
                NodeConfig.update(vm.nodeConfig, onSaveSuccess, onSaveError);
            } else {
                NodeConfig.save(vm.nodeConfig, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:nodeConfigUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
