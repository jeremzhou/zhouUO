(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationMetaController', ApplicationMetaController);

    ApplicationMetaController.$inject = ['DataUtils', 'ApplicationMeta'];

    function ApplicationMetaController(DataUtils, ApplicationMeta) {

        var vm = this;

        vm.applicationMetas = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            ApplicationMeta.query(function(result) {
                vm.applicationMetas = result;
                vm.searchQuery = null;
            });
        }
    }
})();
