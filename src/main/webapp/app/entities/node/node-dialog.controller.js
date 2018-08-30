(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('NodeDialogController', NodeDialogController);

    NodeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Node', 'Project'];

    function NodeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Node, Project) {
        var vm = this;

        vm.node = entity;
        vm.clear = clear;
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
            if (vm.node.id !== null) {
                Node.update(vm.node, onSaveSuccess, onSaveError);
            } else {
                Node.save(vm.node, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:nodeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
