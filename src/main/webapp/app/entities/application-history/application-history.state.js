(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('application-history', {
            parent: 'entity',
            url: '/application-history',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.applicationHistory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application-history/application-histories.html',
                    controller: 'ApplicationHistoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('applicationHistory');
                    $translatePartialLoader.addPart('operation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('application-history-detail', {
            parent: 'application-history',
            url: '/application-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.applicationHistory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application-history/application-history-detail.html',
                    controller: 'ApplicationHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('applicationHistory');
                    $translatePartialLoader.addPart('operation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ApplicationHistory', function($stateParams, ApplicationHistory) {
                    return ApplicationHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'application-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('application-history-detail.edit', {
            parent: 'application-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-history/application-history-dialog.html',
                    controller: 'ApplicationHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApplicationHistory', function(ApplicationHistory) {
                            return ApplicationHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application-history.new', {
            parent: 'application-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-history/application-history-dialog.html',
                    controller: 'ApplicationHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                applicationMetaId: null,
                                serverId: null,
                                applicationId: null,
                                operation: null,
                                createTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('application-history', null, { reload: 'application-history' });
                }, function() {
                    $state.go('application-history');
                });
            }]
        })
        .state('application-history.edit', {
            parent: 'application-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-history/application-history-dialog.html',
                    controller: 'ApplicationHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApplicationHistory', function(ApplicationHistory) {
                            return ApplicationHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application-history', null, { reload: 'application-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application-history.delete', {
            parent: 'application-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-history/application-history-delete-dialog.html',
                    controller: 'ApplicationHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ApplicationHistory', function(ApplicationHistory) {
                            return ApplicationHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application-history', null, { reload: 'application-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
