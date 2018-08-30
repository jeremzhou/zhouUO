(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('global-config-history', {
            parent: 'entity',
            url: '/global-config-history',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.globalConfigHistory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/global-config-history/global-config-histories.html',
                    controller: 'GlobalConfigHistoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('globalConfigHistory');
                    $translatePartialLoader.addPart('operation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('global-config-history-detail', {
            parent: 'global-config-history',
            url: '/global-config-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.globalConfigHistory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/global-config-history/global-config-history-detail.html',
                    controller: 'GlobalConfigHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('globalConfigHistory');
                    $translatePartialLoader.addPart('operation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'GlobalConfigHistory', function($stateParams, GlobalConfigHistory) {
                    return GlobalConfigHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'global-config-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('global-config-history-detail.edit', {
            parent: 'global-config-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/global-config-history/global-config-history-dialog.html',
                    controller: 'GlobalConfigHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GlobalConfigHistory', function(GlobalConfigHistory) {
                            return GlobalConfigHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('global-config-history.new', {
            parent: 'global-config-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/global-config-history/global-config-history-dialog.html',
                    controller: 'GlobalConfigHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                applicationMetaId: null,
                                globalConfigId: null,
                                key: null,
                                value: null,
                                operation: null,
                                createTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('global-config-history', null, { reload: 'global-config-history' });
                }, function() {
                    $state.go('global-config-history');
                });
            }]
        })
        .state('global-config-history.edit', {
            parent: 'global-config-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/global-config-history/global-config-history-dialog.html',
                    controller: 'GlobalConfigHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GlobalConfigHistory', function(GlobalConfigHistory) {
                            return GlobalConfigHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('global-config-history', null, { reload: 'global-config-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('global-config-history.delete', {
            parent: 'global-config-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/global-config-history/global-config-history-delete-dialog.html',
                    controller: 'GlobalConfigHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GlobalConfigHistory', function(GlobalConfigHistory) {
                            return GlobalConfigHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('global-config-history', null, { reload: 'global-config-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
