(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ClientHeartbeatInfoDeleteController',ClientHeartbeatInfoDeleteController);

    ClientHeartbeatInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'ClientHeartbeatInfo'];

    function ClientHeartbeatInfoDeleteController($uibModalInstance, entity, ClientHeartbeatInfo) {
        var vm = this;

        vm.clientHeartbeatInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ClientHeartbeatInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
