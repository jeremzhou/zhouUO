(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('PrivateConfigDeleteController',PrivateConfigDeleteController);

    PrivateConfigDeleteController.$inject = ['$uibModalInstance', 'entity', 'PrivateConfig'];

    function PrivateConfigDeleteController($uibModalInstance, entity, PrivateConfig) {
        var vm = this;

        vm.privateConfig = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PrivateConfig.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
