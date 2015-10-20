'use strict';

angular.module('comparativestockApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
