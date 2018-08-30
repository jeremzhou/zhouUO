(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('PrivateConfigHistoryController', PrivateConfigHistoryController);

    PrivateConfigHistoryController.$inject = ['PrivateConfigHistory'];

    function PrivateConfigHistoryController(PrivateConfigHistory) {

        var vm = this;

        vm.privateConfigHistories = [];

        loadAll();

        function loadAll() {
            PrivateConfigHistory.query(function(result) {
                vm.privateConfigHistories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
