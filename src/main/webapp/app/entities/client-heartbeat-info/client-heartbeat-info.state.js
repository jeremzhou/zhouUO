(function() {
    'use strict';

    angular
        .module('uapolloApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('client-heartbeat-info', {
            parent: 'entity',
            url: '/client-heartbeat-info',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.clientHeartbeatInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-heartbeat-info/client-heartbeat-infos.html',
                    controller: 'ClientHeartbeatInfoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clientHeartbeatInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('client-heartbeat-info-detail', {
            parent: 'client-heartbeat-info',
            url: '/client-heartbeat-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'uapolloApp.clientHeartbeatInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-heartbeat-info/client-heartbeat-info-detail.html',
                    controller: 'ClientHeartbeatInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clientHeartbeatInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ClientHeartbeatInfo', function($stateParams, ClientHeartbeatInfo) {
                    return ClientHeartbeatInfo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'client-heartbeat-info',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('client-heartbeat-info-detail.edit', {
            parent: 'client-heartbeat-info-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-heartbeat-info/client-heartbeat-info-dialog.html',
                    controller: 'ClientHeartbeatInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClientHeartbeatInfo', function(ClientHeartbeatInfo) {
                            return ClientHeartbeatInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client-heartbeat-info.new', {
            parent: 'client-heartbeat-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-heartbeat-info/client-heartbeat-info-dialog.html',
                    controller: 'ClientHeartbeatInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                ip: null,
                                applicationMetaName: null,
                                updateTime: null,
                                updateVersion: null,
                                heartbeatTime: null,
                                heartbeatVersion: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('client-heartbeat-info', null, { reload: 'client-heartbeat-info' });
                }, function() {
                    $state.go('client-heartbeat-info');
                });
            }]
        })
        .state('client-heartbeat-info.edit', {
            parent: 'client-heartbeat-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-heartbeat-info/client-heartbeat-info-dialog.html',
                    controller: 'ClientHeartbeatInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClientHeartbeatInfo', function(ClientHeartbeatInfo) {
                            return ClientHeartbeatInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-heartbeat-info', null, { reload: 'client-heartbeat-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client-heartbeat-info.delete', {
            parent: 'client-heartbeat-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-heartbeat-info/client-heartbeat-info-delete-dialog.html',
                    controller: 'ClientHeartbeatInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClientHeartbeatInfo', function(ClientHeartbeatInfo) {
                            return ClientHeartbeatInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-heartbeat-info', null, { reload: 'client-heartbeat-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
