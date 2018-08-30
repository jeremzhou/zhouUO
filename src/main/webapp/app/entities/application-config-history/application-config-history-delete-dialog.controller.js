(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationConfigHistoryDeleteController',ApplicationConfigHistoryDeleteController);

    ApplicationConfigHistoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'ApplicationConfigHistory'];

    function ApplicationConfigHistoryDeleteController($uibModalInstance, entity, ApplicationConfigHistory) {
        var vm = this;

        vm.applicationConfigHistory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ApplicationConfigHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
