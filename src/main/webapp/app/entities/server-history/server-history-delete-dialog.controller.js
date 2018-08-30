(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ServerHistoryDeleteController',ServerHistoryDeleteController);

    ServerHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'ServerHistory'];

    function ServerHistoryDeleteController($uibModalInstance, entity, ServerHistory) {
        var vm = this;

        vm.serverHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ServerHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
