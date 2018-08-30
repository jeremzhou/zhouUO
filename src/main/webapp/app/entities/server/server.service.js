(function() {
    'use strict';
    angular
        .module('uapolloApp')
        .factory('Server', Server);

    Server.$inject = ['$resource'];

    function Server ($resource) {
        var resourceUrl =  'api/servers/:id';

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
