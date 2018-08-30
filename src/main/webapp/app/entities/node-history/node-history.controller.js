(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('NodeHistoryController', NodeHistoryController);

    NodeHistoryController.$inject = ['NodeHistory'];

    function NodeHistoryController(NodeHistory) {

        var vm = this;

        vm.nodeHistories = [];

        loadAll();

        function loadAll() {
            NodeHistory.query(function(result) {
                vm.nodeHistories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
