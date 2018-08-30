(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .controller('ProjectHistoryController', ProjectHistoryController);

    ProjectHistoryController.$inject = ['ProjectHistory'];

    function ProjectHistoryController(ProjectHistory) {

        var vm = this;

        vm.projectHistories = [];

        loadAll();

        function loadAll() {
            ProjectHistory.query(function(result) {
                vm.projectHistories = result;
                vm.searchQuery = null;
            });
        }
    }
})();
