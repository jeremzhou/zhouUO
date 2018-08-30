'use strict';

describe('Controller Tests', function() {

    describe('GlobalConfig Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockGlobalConfig, MockApplicationMeta;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockGlobalConfig = jasmine.createSpy('MockGlobalConfig');
            MockApplicationMeta = jasmine.createSpy('MockApplicationMeta');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'GlobalConfig': MockGlobalConfig,
                'ApplicationMeta': MockApplicationMeta
            };
            createController = function() {
                $injector.get('$controller')("GlobalConfigDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'uapolloApp:globalConfigUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
