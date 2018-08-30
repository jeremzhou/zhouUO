(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationConfigHistoryController', ApplicationConfigHistoryController);

    ApplicationConfigHistoryController.$inject = ['DataUtils', 'ApplicationConfigHistory'];

    function ApplicationConfigHistoryController(DataUtils, ApplicationConfigHistory) {

        var vm = this;

        vm.applicationConfigHistories = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            ApplicationConfigHistory.query(function(result) {
                vm.applicationConfigHistories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
