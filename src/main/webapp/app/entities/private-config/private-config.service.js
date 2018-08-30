(function() {
    'use strict';
    angular
        .module('uapolloApp')
        .factory('PrivateConfig', PrivateConfig);

    PrivateConfig.$inject = ['$resource'];

    function PrivateConfig ($resource) {
        var resourceUrl =  'api/private-configs/:id';

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
