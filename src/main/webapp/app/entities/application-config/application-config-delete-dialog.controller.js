(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationConfigDeleteController',ApplicationConfigDeleteController);

    ApplicationConfigDeleteController.$inject = ['$uibModalInstance', 'entity', 'ApplicationConfig'];

    function ApplicationConfigDeleteController($uibModalInstance, entity, ApplicationConfig) {
        var vm = this;

        vm.applicationConfig = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ApplicationConfig.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
