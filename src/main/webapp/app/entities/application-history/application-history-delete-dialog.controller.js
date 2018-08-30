(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationHistoryDeleteController',ApplicationHistoryDeleteController);

    ApplicationHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'ApplicationHistory'];

    function ApplicationHistoryDeleteController($uibModalInstance, entity, ApplicationHistory) {
        var vm = this;

        vm.applicationHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ApplicationHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
