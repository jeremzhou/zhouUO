(function() {
    'use strict';
    angular
        .module('uapolloApp')
        .factory('GlobalConfigHistory', GlobalConfigHistory);

    GlobalConfigHistory.$inject = ['$resource'];

    function GlobalConfigHistory ($resource) {
        var resourceUrl =  'api/global-config-histories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
