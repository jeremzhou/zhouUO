directive_module.directive('showtextmodal', showTextModalDirective);

function showTextModalDirective() {
    return {
        restrict: 'E',
        templateUrl: '/uapollo/views/component/show-text-modal.html',
        transclude: true,
        replace: true,
        scope: {
            text: '='
        },
        link: function (scope) {
            scope.$watch('text', init);

            function init() {
                scope.jsonObject = undefined;
                if (isJsonText(scope.text)) {
                    scope.jsonObject = JSON.parse(scope.text);
                }
            }

            function isJsonText(text) {
                try {
                    JSON.parse(text);
                } catch (e) {
                    return false;
                }
                return true;
            }
        }
    }
}


