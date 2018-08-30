(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('private-config', {
            parent: 'entity',
            url: '/private-config',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.privateConfig.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/private-config/private-configs.html',
                    controller: 'PrivateConfigController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('privateConfig');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('private-config-detail', {
            parent: 'private-config',
            url: '/private-config/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.privateConfig.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/private-config/private-config-detail.html',
                    controller: 'PrivateConfigDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('privateConfig');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PrivateConfig', function($stateParams, PrivateConfig) {
                    return PrivateConfig.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'private-config',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('private-config-detail.edit', {
            parent: 'private-config-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/private-config/private-config-dialog.html',
                    controller: 'PrivateConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PrivateConfig', function(PrivateConfig) {
                            return PrivateConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('private-config.new', {
            parent: 'private-config',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/private-config/private-config-dialog.html',
                    controller: 'PrivateConfigDialogController',
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
                    $state.go('private-config', null, { reload: 'private-config' });
                }, function() {
                    $state.go('private-config');
                });
            }]
        })
        .state('private-config.edit', {
            parent: 'private-config',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/private-config/private-config-dialog.html',
                    controller: 'PrivateConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PrivateConfig', function(PrivateConfig) {
                            return PrivateConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('private-config', null, { reload: 'private-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('private-config.delete', {
            parent: 'private-config',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/private-config/private-config-delete-dialog.html',
                    controller: 'PrivateConfigDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PrivateConfig', function(PrivateConfig) {
                            return PrivateConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('private-config', null, { reload: 'private-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
