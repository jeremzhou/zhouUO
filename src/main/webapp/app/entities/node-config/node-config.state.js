(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('node-config', {
            parent: 'entity',
            url: '/node-config',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.nodeConfig.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/node-config/node-configs.html',
                    controller: 'NodeConfigController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('nodeConfig');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('node-config-detail', {
            parent: 'node-config',
            url: '/node-config/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.nodeConfig.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/node-config/node-config-detail.html',
                    controller: 'NodeConfigDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('nodeConfig');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'NodeConfig', function($stateParams, NodeConfig) {
                    return NodeConfig.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'node-config',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('node-config-detail.edit', {
            parent: 'node-config-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/node-config/node-config-dialog.html',
                    controller: 'NodeConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NodeConfig', function(NodeConfig) {
                            return NodeConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('node-config.new', {
            parent: 'node-config',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/node-config/node-config-dialog.html',
                    controller: 'NodeConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                key: null,
                                value: null,
                                createTime: null,
                                modifyTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('node-config', null, { reload: 'node-config' });
                }, function() {
                    $state.go('node-config');
                });
            }]
        })
        .state('node-config.edit', {
            parent: 'node-config',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/node-config/node-config-dialog.html',
                    controller: 'NodeConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NodeConfig', function(NodeConfig) {
                            return NodeConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('node-config', null, { reload: 'node-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('node-config.delete', {
            parent: 'node-config',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/node-config/node-config-delete-dialog.html',
                    controller: 'NodeConfigDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NodeConfig', function(NodeConfig) {
                            return NodeConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('node-config', null, { reload: 'node-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
