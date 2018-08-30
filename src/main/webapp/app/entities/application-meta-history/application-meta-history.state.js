(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('application-meta-history', {
            parent: 'entity',
            url: '/application-meta-history',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.applicationMetaHistory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application-meta-history/application-meta-histories.html',
                    controller: 'ApplicationMetaHistoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('applicationMetaHistory');
                    $translatePartialLoader.addPart('operation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('application-meta-history-detail', {
            parent: 'application-meta-history',
            url: '/application-meta-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.applicationMetaHistory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application-meta-history/application-meta-history-detail.html',
                    controller: 'ApplicationMetaHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('applicationMetaHistory');
                    $translatePartialLoader.addPart('operation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ApplicationMetaHistory', function($stateParams, ApplicationMetaHistory) {
                    return ApplicationMetaHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'application-meta-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('application-meta-history-detail.edit', {
            parent: 'application-meta-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-meta-history/application-meta-history-dialog.html',
                    controller: 'ApplicationMetaHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApplicationMetaHistory', function(ApplicationMetaHistory) {
                            return ApplicationMetaHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application-meta-history.new', {
            parent: 'application-meta-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-meta-history/application-meta-history-dialog.html',
                    controller: 'ApplicationMetaHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                projectId: null,
                                applicationMetaId: null,
                                name: null,
                                configFile: null,
                                configContent: null,
                                operation: null,
                                createTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('application-meta-history', null, { reload: 'application-meta-history' });
                }, function() {
                    $state.go('application-meta-history');
                });
            }]
        })
        .state('application-meta-history.edit', {
            parent: 'application-meta-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-meta-history/application-meta-history-dialog.html',
                    controller: 'ApplicationMetaHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApplicationMetaHistory', function(ApplicationMetaHistory) {
                            return ApplicationMetaHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application-meta-history', null, { reload: 'application-meta-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application-meta-history.delete', {
            parent: 'application-meta-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-meta-history/application-meta-history-delete-dialog.html',
                    controller: 'ApplicationMetaHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ApplicationMetaHistory', function(ApplicationMetaHistory) {
                            return ApplicationMetaHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application-meta-history', null, { reload: 'application-meta-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
