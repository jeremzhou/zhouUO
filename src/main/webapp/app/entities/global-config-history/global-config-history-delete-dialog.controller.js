(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('GlobalConfigHistoryDeleteController',GlobalConfigHistoryDeleteController);

    GlobalConfigHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'GlobalConfigHistory'];

    function GlobalConfigHistoryDeleteController($uibModalInstance, entity, GlobalConfigHistory) {
        var vm = this;

        vm.globalConfigHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GlobalConfigHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
