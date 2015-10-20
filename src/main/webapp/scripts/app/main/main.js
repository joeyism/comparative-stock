'use strict';

angular.module('comparativestockApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('home', {
                url: '/',
                data: {
                    roles: []
                },
                views: {
                    'stock': {
                        templateUrl: 'scripts/app/main/main.html',
                        controller: 'MainController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        return $translate.refresh();
                    }]
                }
            });
    });
