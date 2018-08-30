(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationConfigController', ApplicationConfigController);

    ApplicationConfigController.$inject = ['DataUtils', 'ApplicationConfig'];

    function ApplicationConfigController(DataUtils, ApplicationConfig) {

        var vm = this;

        vm.applicationConfigs = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            ApplicationConfig.query(function(result) {
                vm.applicationConfigs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
