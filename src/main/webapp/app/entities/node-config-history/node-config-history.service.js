(function() {
    'use strict';
    angular
        .module('uapolloApp')
        .factory('NodeConfigHistory', NodeConfigHistory);

    NodeConfigHistory.$inject = ['$resource'];

    function NodeConfigHistory ($resource) {
        var resourceUrl =  'api/node-config-histories/:id';

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
