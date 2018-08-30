(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ServerDeleteController',ServerDeleteController);

    ServerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Server'];

    function ServerDeleteController($uibModalInstance, entity, Server) {
        var vm = this;

        vm.server = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Server.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
