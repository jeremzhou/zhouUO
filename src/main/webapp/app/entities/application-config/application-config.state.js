(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('application-config', {
            parent: 'entity',
            url: '/application-config',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.applicationConfig.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application-config/application-configs.html',
                    controller: 'ApplicationConfigController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('applicationConfig');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('application-config-detail', {
            parent: 'application-config',
            url: '/application-config/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.applicationConfig.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application-config/application-config-detail.html',
                    controller: 'ApplicationConfigDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('applicationConfig');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ApplicationConfig', function($stateParams, ApplicationConfig) {
                    return ApplicationConfig.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'application-config',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('application-config-detail.edit', {
            parent: 'application-config-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-config/application-config-dialog.html',
                    controller: 'ApplicationConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApplicationConfig', function(ApplicationConfig) {
                            return ApplicationConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application-config.new', {
            parent: 'application-config',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-config/application-config-dialog.html',
                    controller: 'ApplicationConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                configContent: null,
                                createTime: null,
                                modifyTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('application-config', null, { reload: 'application-config' });
                }, function() {
                    $state.go('application-config');
                });
            }]
        })
        .state('application-config.edit', {
            parent: 'application-config',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-config/application-config-dialog.html',
                    controller: 'ApplicationConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApplicationConfig', function(ApplicationConfig) {
                            return ApplicationConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application-config', null, { reload: 'application-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application-config.delete', {
            parent: 'application-config',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-config/application-config-delete-dialog.html',
                    controller: 'ApplicationConfigDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ApplicationConfig', function(ApplicationConfig) {
                            return ApplicationConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application-config', null, { reload: 'application-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
