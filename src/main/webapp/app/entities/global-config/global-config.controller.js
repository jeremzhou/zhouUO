(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('GlobalConfigController', GlobalConfigController);

    GlobalConfigController.$inject = ['GlobalConfig'];

    function GlobalConfigController(GlobalConfig) {

        var vm = this;

        vm.globalConfigs = [];

        loadAll();

        function loadAll() {
            GlobalConfig.query(function(result) {
                vm.globalConfigs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
