'use strict';

angular.module('comparativestockApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


