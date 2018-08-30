(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('GlobalConfigDeleteController',GlobalConfigDeleteController);

    GlobalConfigDeleteController.$inject = ['$uibModalInstance', 'entity', 'GlobalConfig'];

    function GlobalConfigDeleteController($uibModalInstance, entity, GlobalConfig) {
        var vm = this;

        vm.globalConfig = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GlobalConfig.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
