 'use strict';

angular.module('comparativestockApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-comparativestockApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-comparativestockApp-params')});
                }
                return response;
            },
        };
    });