(function() {
    'use strict';
    angular
        .module('uapolloApp')
        .factory('ApplicationConfig', ApplicationConfig);

    ApplicationConfig.$inject = ['$resource'];

    function ApplicationConfig ($resource) {
        var resourceUrl =  'api/application-configs/:id';

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
