(function() {
    'use strict';
    angular
        .module('uapolloApp')
        .factory('ServerHistory', ServerHistory);

    ServerHistory.$inject = ['$resource'];

    function ServerHistory ($resource) {
        var resourceUrl =  'api/server-histories/:id';

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
