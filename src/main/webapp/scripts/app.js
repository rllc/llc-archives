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
        'ngRoute',
        'spring-data-rest'
    ])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/sermon.html',
                controller: 'SermonCtrl'
            })
            .when('/congregations/:name', {
                templateUrl: 'views/sermon.html',
                controller: 'SermonCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    });
