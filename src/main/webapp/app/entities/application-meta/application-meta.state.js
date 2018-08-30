(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('application-meta', {
            parent: 'entity',
            url: '/application-meta',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.applicationMeta.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application-meta/application-metas.html',
                    controller: 'ApplicationMetaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('applicationMeta');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('application-meta-detail', {
            parent: 'application-meta',
            url: '/application-meta/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.applicationMeta.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application-meta/application-meta-detail.html',
                    controller: 'ApplicationMetaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('applicationMeta');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ApplicationMeta', function($stateParams, ApplicationMeta) {
                    return ApplicationMeta.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'application-meta',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('application-meta-detail.edit', {
            parent: 'application-meta-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-meta/application-meta-dialog.html',
                    controller: 'ApplicationMetaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApplicationMeta', function(ApplicationMeta) {
                            return ApplicationMeta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application-meta.new', {
            parent: 'application-meta',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-meta/application-meta-dialog.html',
                    controller: 'ApplicationMetaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                configFile: null,
                                configContent: null,
                                createTime: null,
                                modifyTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('application-meta', null, { reload: 'application-meta' });
                }, function() {
                    $state.go('application-meta');
                });
            }]
        })
        .state('application-meta.edit', {
            parent: 'application-meta',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-meta/application-meta-dialog.html',
                    controller: 'ApplicationMetaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApplicationMeta', function(ApplicationMeta) {
                            return ApplicationMeta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application-meta', null, { reload: 'application-meta' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application-meta.delete', {
            parent: 'application-meta',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-meta/application-meta-delete-dialog.html',
                    controller: 'ApplicationMetaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ApplicationMeta', function(ApplicationMeta) {
                            return ApplicationMeta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application-meta', null, { reload: 'application-meta' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
