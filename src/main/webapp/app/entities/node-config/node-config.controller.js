(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('NodeConfigController', NodeConfigController);

    NodeConfigController.$inject = ['NodeConfig'];

    function NodeConfigController(NodeConfig) {

        var vm = this;

        vm.nodeConfigs = [];

        loadAll();

        function loadAll() {
            NodeConfig.query(function(result) {
                vm.nodeConfigs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
