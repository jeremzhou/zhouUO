(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('NodeHistoryDeleteController',NodeHistoryDeleteController);

    NodeHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'NodeHistory'];

    function NodeHistoryDeleteController($uibModalInstance, entity, NodeHistory) {
        var vm = this;

        vm.nodeHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            NodeHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
