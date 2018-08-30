(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ClientHeartbeatInfoController', ClientHeartbeatInfoController);

    ClientHeartbeatInfoController.$inject = ['ClientHeartbeatInfo'];

    function ClientHeartbeatInfoController(ClientHeartbeatInfo) {

        var vm = this;

        vm.clientHeartbeatInfos = [];

        loadAll();

        function loadAll() {
            ClientHeartbeatInfo.query(function(result) {
                vm.clientHeartbeatInfos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
