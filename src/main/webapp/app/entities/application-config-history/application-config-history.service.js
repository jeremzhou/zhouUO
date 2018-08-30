(function() {
    'use strict';
    angular
        .module('uapolloApp')
        .factory('ApplicationConfigHistory', ApplicationConfigHistory);

    ApplicationConfigHistory.$inject = ['$resource'];

    function ApplicationConfigHistory ($resource) {
        var resourceUrl =  'api/application-config-histories/:id';

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
