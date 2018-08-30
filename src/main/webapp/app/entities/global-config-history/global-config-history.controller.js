(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('GlobalConfigHistoryController', GlobalConfigHistoryController);

    GlobalConfigHistoryController.$inject = ['GlobalConfigHistory'];

    function GlobalConfigHistoryController(GlobalConfigHistory) {

        var vm = this;

        vm.globalConfigHistories = [];

        loadAll();

        function loadAll() {
            GlobalConfigHistory.query(function(result) {
                vm.globalConfigHistories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
