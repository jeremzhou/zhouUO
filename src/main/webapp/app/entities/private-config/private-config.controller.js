(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('PrivateConfigController', PrivateConfigController);

    PrivateConfigController.$inject = ['PrivateConfig'];

    function PrivateConfigController(PrivateConfig) {

        var vm = this;

        vm.privateConfigs = [];

        loadAll();

        function loadAll() {
            PrivateConfig.query(function(result) {
                vm.privateConfigs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
