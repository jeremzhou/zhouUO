(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ServerDialogController', ServerDialogController);

    ServerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Server', 'Node'];

    function ServerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Server, Node) {
        var vm = this;

        vm.server = entity;
        vm.clear = clear;
        vm.save = save;
        vm.nodes = Node.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.server.id !== null) {
                Server.update(vm.server, onSaveSuccess, onSaveError);
            } else {
                Server.save(vm.server, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('uapolloApp:serverUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
