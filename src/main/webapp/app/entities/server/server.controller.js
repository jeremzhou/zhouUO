(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ServerController', ServerController);

    ServerController.$inject = ['Server'];

    function ServerController(Server) {

        var vm = this;

        vm.servers = [];

        loadAll();

        function loadAll() {
            Server.query(function(result) {
                vm.servers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
