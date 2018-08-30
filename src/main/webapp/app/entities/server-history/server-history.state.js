(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('server-history', {
            parent: 'entity',
            url: '/server-history',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.serverHistory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/server-history/server-histories.html',
                    controller: 'ServerHistoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serverHistory');
                    $translatePartialLoader.addPart('operation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('server-history-detail', {
            parent: 'server-history',
            url: '/server-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.serverHistory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/server-history/server-history-detail.html',
                    controller: 'ServerHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serverHistory');
                    $translatePartialLoader.addPart('operation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ServerHistory', function($stateParams, ServerHistory) {
                    return ServerHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'server-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('server-history-detail.edit', {
            parent: 'server-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/server-history/server-history-dialog.html',
                    controller: 'ServerHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServerHistory', function(ServerHistory) {
                            return ServerHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('server-history.new', {
            parent: 'server-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/server-history/server-history-dialog.html',
                    controller: 'ServerHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nodeId: null,
                                serverId: null,
                                ip: null,
                                operation: null,
                                createTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('server-history', null, { reload: 'server-history' });
                }, function() {
                    $state.go('server-history');
                });
            }]
        })
        .state('server-history.edit', {
            parent: 'server-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/server-history/server-history-dialog.html',
                    controller: 'ServerHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServerHistory', function(ServerHistory) {
                            return ServerHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('server-history', null, { reload: 'server-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('server-history.delete', {
            parent: 'server-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/server-history/server-history-delete-dialog.html',
                    controller: 'ServerHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ServerHistory', function(ServerHistory) {
                            return ServerHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('server-history', null, { reload: 'server-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
