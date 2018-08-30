(function() {
    'use strict';
    angular
        .module('uapolloApp')
        .factory('ClientHeartbeatInfo', ClientHeartbeatInfo);

    ClientHeartbeatInfo.$inject = ['$resource'];

    function ClientHeartbeatInfo ($resource) {
        var resourceUrl =  'api/client-heartbeat-infos/:id';

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
