(function() {
    'use strict';
    angular
        .module('uapolloApp')
        .factory('ProjectHistory', ProjectHistory);

    ProjectHistory.$inject = ['$resource'];

    function ProjectHistory ($resource) {
        var resourceUrl =  'api/project-histories/:id';

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
