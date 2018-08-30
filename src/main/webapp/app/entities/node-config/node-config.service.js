(function() {
    'use strict';
    angular
        .module('uapolloApp')
        .factory('NodeConfig', NodeConfig);

    NodeConfig.$inject = ['$resource'];

    function NodeConfig ($resource) {
        var resourceUrl =  'api/node-configs/:id';

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
