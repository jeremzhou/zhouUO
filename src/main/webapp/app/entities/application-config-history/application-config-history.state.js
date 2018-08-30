(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('application-config-history', {
            parent: 'entity',
            url: '/application-config-history',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.applicationConfigHistory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application-config-history/application-config-histories.html',
                    controller: 'ApplicationConfigHistoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('applicationConfigHistory');
                    $translatePartialLoader.addPart('operation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('application-config-history-detail', {
            parent: 'application-config-history',
            url: '/application-config-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.applicationConfigHistory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application-config-history/application-config-history-detail.html',
                    controller: 'ApplicationConfigHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('applicationConfigHistory');
                    $translatePartialLoader.addPart('operation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ApplicationConfigHistory', function($stateParams, ApplicationConfigHistory) {
                    return ApplicationConfigHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'application-config-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('application-config-history-detail.edit', {
            parent: 'application-config-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-config-history/application-config-history-dialog.html',
                    controller: 'ApplicationConfigHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApplicationConfigHistory', function(ApplicationConfigHistory) {
                            return ApplicationConfigHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application-config-history.new', {
            parent: 'application-config-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-config-history/application-config-history-dialog.html',
                    controller: 'ApplicationConfigHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                applicationId: null,
                                applicationConfigid: null,
                                configContent: null,
                                operation: null,
                                createTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('application-config-history', null, { reload: 'application-config-history' });
                }, function() {
                    $state.go('application-config-history');
                });
            }]
        })
        .state('application-config-history.edit', {
            parent: 'application-config-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-config-history/application-config-history-dialog.html',
                    controller: 'ApplicationConfigHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApplicationConfigHistory', function(ApplicationConfigHistory) {
                            return ApplicationConfigHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application-config-history', null, { reload: 'application-config-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application-config-history.delete', {
            parent: 'application-config-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-config-history/application-config-history-delete-dialog.html',
                    controller: 'ApplicationConfigHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ApplicationConfigHistory', function(ApplicationConfigHistory) {
                            return ApplicationConfigHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application-config-history', null, { reload: 'application-config-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
