'use strict';

describe('Controller Tests', function() {

    describe('NodeConfig Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockNodeConfig, MockApplicationMeta, MockNode;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockNodeConfig = jasmine.createSpy('MockNodeConfig');
            MockApplicationMeta = jasmine.createSpy('MockApplicationMeta');
            MockNode = jasmine.createSpy('MockNode');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'NodeConfig': MockNodeConfig,
                'ApplicationMeta': MockApplicationMeta,
                'Node': MockNode
            };
            createController = function() {
                $injector.get('$controller')("NodeConfigDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'uapolloApp:nodeConfigUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
