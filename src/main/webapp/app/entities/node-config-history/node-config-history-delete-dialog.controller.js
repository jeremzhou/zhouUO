(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('NodeConfigHistoryDeleteController',NodeConfigHistoryDeleteController);

    NodeConfigHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'NodeConfigHistory'];

    function NodeConfigHistoryDeleteController($uibModalInstance, entity, NodeConfigHistory) {
        var vm = this;

        vm.nodeConfigHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            NodeConfigHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
