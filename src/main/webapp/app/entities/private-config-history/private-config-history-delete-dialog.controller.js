(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('PrivateConfigHistoryDeleteController',PrivateConfigHistoryDeleteController);

    PrivateConfigHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'PrivateConfigHistory'];

    function PrivateConfigHistoryDeleteController($uibModalInstance, entity, PrivateConfigHistory) {
        var vm = this;

        vm.privateConfigHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PrivateConfigHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
