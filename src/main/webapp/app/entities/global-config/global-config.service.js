(function() {
    'use strict';
    angular
        .module('uapolloApp')
        .factory('GlobalConfig', GlobalConfig);

    GlobalConfig.$inject = ['$resource'];

    function GlobalConfig ($resource) {
        var resourceUrl =  'api/global-configs/:id';

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
