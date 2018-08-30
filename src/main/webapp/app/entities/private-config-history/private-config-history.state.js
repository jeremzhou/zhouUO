(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('private-config-history', {
            parent: 'entity',
            url: '/private-config-history',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.privateConfigHistory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/private-config-history/private-config-histories.html',
                    controller: 'PrivateConfigHistoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('privateConfigHistory');
                    $translatePartialLoader.addPart('operation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('private-config-history-detail', {
            parent: 'private-config-history',
            url: '/private-config-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.privateConfigHistory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/private-config-history/private-config-history-detail.html',
                    controller: 'PrivateConfigHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('privateConfigHistory');
                    $translatePartialLoader.addPart('operation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PrivateConfigHistory', function($stateParams, PrivateConfigHistory) {
                    return PrivateConfigHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'private-config-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('private-config-history-detail.edit', {
            parent: 'private-config-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/private-config-history/private-config-history-dialog.html',
                    controller: 'PrivateConfigHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PrivateConfigHistory', function(PrivateConfigHistory) {
                            return PrivateConfigHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('private-config-history.new', {
            parent: 'private-config-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/private-config-history/private-config-history-dialog.html',
                    controller: 'PrivateConfigHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                applicationId: null,
                                privateConfigId: null,
                                key: null,
                                value: null,
                                operation: null,
                                createTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('private-config-history', null, { reload: 'private-config-history' });
                }, function() {
                    $state.go('private-config-history');
                });
            }]
        })
        .state('private-config-history.edit', {
            parent: 'private-config-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/private-config-history/private-config-history-dialog.html',
                    controller: 'PrivateConfigHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PrivateConfigHistory', function(PrivateConfigHistory) {
                            return PrivateConfigHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('private-config-history', null, { reload: 'private-config-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('private-config-history.delete', {
            parent: 'private-config-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/private-config-history/private-config-history-delete-dialog.html',
                    controller: 'PrivateConfigHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PrivateConfigHistory', function(PrivateConfigHistory) {
                            return PrivateConfigHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('private-config-history', null, { reload: 'private-config-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
