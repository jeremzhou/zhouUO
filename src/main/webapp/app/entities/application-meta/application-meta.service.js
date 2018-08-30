(function() {
    'use strict';
    angular
        .module('uapolloApp')
        .factory('ApplicationMeta', ApplicationMeta);

    ApplicationMeta.$inject = ['$resource'];

    function ApplicationMeta ($resource) {
        var resourceUrl =  'api/application-metas/:id';

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
