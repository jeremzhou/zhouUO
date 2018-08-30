(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('NodeController', NodeController);

    NodeController.$inject = ['Node'];

    function NodeController(Node) {

        var vm = this;

        vm.nodes = [];

        loadAll();

        function loadAll() {
            Node.query(function(result) {
                vm.nodes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
