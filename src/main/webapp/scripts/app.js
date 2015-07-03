'use strict';

/**
 * @ngdoc overview
 * @name llcArchivesApp
 * @description
 * # llcArchivesApp
 *
 * Main module of the application.
 */
angular
    .module('llcArchivesApp', [
        'ngAnimate',
        'ngResource',
        'ngRoute',
        'ngSanitize',
        'ngTouch',
        'spring-data-rest'
    ])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl'
            })
            .when('/congregations/:name', {
                templateUrl: 'views/sermon.html',
                controller: 'SermonCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    });
