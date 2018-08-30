(function() {
    'use strict';
    angular
        .module('uapolloApp')
        .factory('PrivateConfigHistory', PrivateConfigHistory);

    PrivateConfigHistory.$inject = ['$resource'];

    function PrivateConfigHistory ($resource) {
        var resourceUrl =  'api/private-config-histories/:id';

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
