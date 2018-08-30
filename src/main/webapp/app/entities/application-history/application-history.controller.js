(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationHistoryController', ApplicationHistoryController);

    ApplicationHistoryController.$inject = ['ApplicationHistory'];

    function ApplicationHistoryController(ApplicationHistory) {

        var vm = this;

        vm.applicationHistories = [];

        loadAll();

        function loadAll() {
            ApplicationHistory.query(function(result) {
                vm.applicationHistories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
