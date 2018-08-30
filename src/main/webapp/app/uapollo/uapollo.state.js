(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('uapollo', {
            parent: 'admin',
            url: '/',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'global.menu.admin.uapollo'
            },
            views: {
                'content@': {
                    templateUrl: 'app/uapollo/uapollo.html'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', function ($translate) {
                    return $translate.refresh();
                }]
            }
        });
    }
})();
