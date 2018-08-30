(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('global-config', {
            parent: 'entity',
            url: '/global-config',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.globalConfig.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/global-config/global-configs.html',
                    controller: 'GlobalConfigController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('globalConfig');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('global-config-detail', {
            parent: 'global-config',
            url: '/global-config/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.globalConfig.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/global-config/global-config-detail.html',
                    controller: 'GlobalConfigDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('globalConfig');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'GlobalConfig', function($stateParams, GlobalConfig) {
                    return GlobalConfig.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'global-config',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('global-config-detail.edit', {
            parent: 'global-config-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/global-config/global-config-dialog.html',
                    controller: 'GlobalConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GlobalConfig', function(GlobalConfig) {
                            return GlobalConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('global-config.new', {
            parent: 'global-config',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/global-config/global-config-dialog.html',
                    controller: 'GlobalConfigDialogController',
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
                    $state.go('global-config', null, { reload: 'global-config' });
                }, function() {
                    $state.go('global-config');
                });
            }]
        })
        .state('global-config.edit', {
            parent: 'global-config',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/global-config/global-config-dialog.html',
                    controller: 'GlobalConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GlobalConfig', function(GlobalConfig) {
                            return GlobalConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('global-config', null, { reload: 'global-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('global-config.delete', {
            parent: 'global-config',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/global-config/global-config-delete-dialog.html',
                    controller: 'GlobalConfigDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GlobalConfig', function(GlobalConfig) {
                            return GlobalConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('global-config', null, { reload: 'global-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
