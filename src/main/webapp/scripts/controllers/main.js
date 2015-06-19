'use strict';

/**
 * @ngdoc function
 * @name llcArchivesApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the llcArchivesApp
 */
angular.module('llcArchivesApp')
    .controller('MainCtrl', function ($scope, $http, SpringDataRestAdapter) {
        var httpPromise = $http.get('api/congregations').success(function (response) {
            $scope.response = angular.toJson(response, true);
        });

        SpringDataRestAdapter.process(httpPromise).then(function (processedResponse) {
            $scope.congregations = processedResponse._embeddedItems;
        });
    });
