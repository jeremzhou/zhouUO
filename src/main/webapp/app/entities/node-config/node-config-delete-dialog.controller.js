(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('NodeConfigDeleteController',NodeConfigDeleteController);

    NodeConfigDeleteController.$inject = ['$uibModalInstance', 'entity', 'NodeConfig'];

    function NodeConfigDeleteController($uibModalInstance, entity, NodeConfig) {
        var vm = this;

        vm.nodeConfig = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            NodeConfig.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
