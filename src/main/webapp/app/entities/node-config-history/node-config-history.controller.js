(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('NodeConfigHistoryController', NodeConfigHistoryController);

    NodeConfigHistoryController.$inject = ['NodeConfigHistory'];

    function NodeConfigHistoryController(NodeConfigHistory) {

        var vm = this;

        vm.nodeConfigHistories = [];

        loadAll();

        function loadAll() {
            NodeConfigHistory.query(function(result) {
                vm.nodeConfigHistories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
