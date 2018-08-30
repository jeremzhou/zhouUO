(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('node-config-history', {
            parent: 'entity',
            url: '/node-config-history',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.nodeConfigHistory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/node-config-history/node-config-histories.html',
                    controller: 'NodeConfigHistoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('nodeConfigHistory');
                    $translatePartialLoader.addPart('operation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('node-config-history-detail', {
            parent: 'node-config-history',
            url: '/node-config-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.nodeConfigHistory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/node-config-history/node-config-history-detail.html',
                    controller: 'NodeConfigHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('nodeConfigHistory');
                    $translatePartialLoader.addPart('operation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'NodeConfigHistory', function($stateParams, NodeConfigHistory) {
                    return NodeConfigHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'node-config-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('node-config-history-detail.edit', {
            parent: 'node-config-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/node-config-history/node-config-history-dialog.html',
                    controller: 'NodeConfigHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NodeConfigHistory', function(NodeConfigHistory) {
                            return NodeConfigHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('node-config-history.new', {
            parent: 'node-config-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/node-config-history/node-config-history-dialog.html',
                    controller: 'NodeConfigHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                applicationMetaId: null,
                                nodeId: null,
                                nodeConfigId: null,
                                key: null,
                                value: null,
                                operation: null,
                                createTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('node-config-history', null, { reload: 'node-config-history' });
                }, function() {
                    $state.go('node-config-history');
                });
            }]
        })
        .state('node-config-history.edit', {
            parent: 'node-config-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/node-config-history/node-config-history-dialog.html',
                    controller: 'NodeConfigHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NodeConfigHistory', function(NodeConfigHistory) {
                            return NodeConfigHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('node-config-history', null, { reload: 'node-config-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('node-config-history.delete', {
            parent: 'node-config-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/node-config-history/node-config-history-delete-dialog.html',
                    controller: 'NodeConfigHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NodeConfigHistory', function(NodeConfigHistory) {
                            return NodeConfigHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('node-config-history', null, { reload: 'node-config-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
