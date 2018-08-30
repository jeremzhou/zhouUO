(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationMetaHistoryController', ApplicationMetaHistoryController);

    ApplicationMetaHistoryController.$inject = ['DataUtils', 'ApplicationMetaHistory'];

    function ApplicationMetaHistoryController(DataUtils, ApplicationMetaHistory) {

        var vm = this;

        vm.applicationMetaHistories = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            ApplicationMetaHistory.query(function(result) {
                vm.applicationMetaHistories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
