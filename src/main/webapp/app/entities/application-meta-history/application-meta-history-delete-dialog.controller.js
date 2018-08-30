(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationMetaHistoryDeleteController',ApplicationMetaHistoryDeleteController);

    ApplicationMetaHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'ApplicationMetaHistory'];

    function ApplicationMetaHistoryDeleteController($uibModalInstance, entity, ApplicationMetaHistory) {
        var vm = this;

        vm.applicationMetaHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ApplicationMetaHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
