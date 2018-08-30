(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ApplicationController', ApplicationController);

    ApplicationController.$inject = ['Application'];

    function ApplicationController(Application) {

        var vm = this;

        vm.applications = [];

        loadAll();

        function loadAll() {
            Application.query(function(result) {
                vm.applications = result;
                vm.searchQuery = null;
            });
        }
    }
})();
