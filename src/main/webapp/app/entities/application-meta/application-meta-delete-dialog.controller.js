(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationMetaDeleteController',ApplicationMetaDeleteController);

    ApplicationMetaDeleteController.$inject = ['$uibModalInstance', 'entity', 'ApplicationMeta'];

    function ApplicationMetaDeleteController($uibModalInstance, entity, ApplicationMeta) {
        var vm = this;

        vm.applicationMeta = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ApplicationMeta.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
