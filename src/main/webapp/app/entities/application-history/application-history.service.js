(function() {
    'use strict';
    angular
        .module('uapolloApp')
        .factory('ApplicationHistory', ApplicationHistory);

    ApplicationHistory.$inject = ['$resource'];

    function ApplicationHistory ($resource) {
        var resourceUrl =  'api/application-histories/:id';

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
