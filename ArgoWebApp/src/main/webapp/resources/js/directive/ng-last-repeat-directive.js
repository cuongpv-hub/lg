/**
 * Created by phatpq on 7/21/2015.
 */
(function () {
    'use strict';
    var module = angular.module('tiny.directive.repeat', []);

    module.directive('ngLastRepeat', function ($timeout) {
        return {
            restrict: 'A',
            link: function (scope, element, attr) {
                if (scope.$last === true) {
                    $timeout(function () {
                    	scope.$emit('ngLastRepeat' + (attr.ngLastRepeat ? '.' + attr.ngLastRepeat : ''));
                    });
                }
            }
        }
    });
})();