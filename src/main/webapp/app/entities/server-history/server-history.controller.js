(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ServerHistoryController', ServerHistoryController);

    ServerHistoryController.$inject = ['ServerHistory'];

    function ServerHistoryController(ServerHistory) {

        var vm = this;

        vm.serverHistories = [];

        loadAll();

        function loadAll() {
            ServerHistory.query(function(result) {
                vm.serverHistories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
