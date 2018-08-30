(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('server', {
            parent: 'entity',
            url: '/server',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.server.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/server/servers.html',
                    controller: 'ServerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('server');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('server-detail', {
            parent: 'server',
            url: '/server/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.server.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/server/server-detail.html',
                    controller: 'ServerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('server');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Server', function($stateParams, Server) {
                    return Server.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'server',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('server-detail.edit', {
            parent: 'server-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/server/server-dialog.html',
                    controller: 'ServerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Server', function(Server) {
                            return Server.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('server.new', {
            parent: 'server',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/server/server-dialog.html',
                    controller: 'ServerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                ip: null,
                                createTime: null,
                                modifyTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('server', null, { reload: 'server' });
                }, function() {
                    $state.go('server');
                });
            }]
        })
        .state('server.edit', {
            parent: 'server',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/server/server-dialog.html',
                    controller: 'ServerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Server', function(Server) {
                            return Server.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('server', null, { reload: 'server' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('server.delete', {
            parent: 'server',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/server/server-delete-dialog.html',
                    controller: 'ServerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Server', function(Server) {
                            return Server.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('server', null, { reload: 'server' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
