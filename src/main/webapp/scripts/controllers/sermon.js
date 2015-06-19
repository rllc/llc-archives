'use strict';

/**
 * @ngdoc function
 * @name llcArchivesApp.controller:SermoncontrollerCtrl
 * @description
 * # SermoncontrollerCtrl
 * Controller of the llcArchivesApp
 */
angular.module('llcArchivesApp')
    .controller('SermonCtrl', function ($scope, $http, SpringDataRestAdapter) {
        var httpPromise = $http.get('api/sermons').success(function (response) {
            $scope.response = angular.toJson(response, true);
        });

        SpringDataRestAdapter.process(httpPromise).then(function (processedResponse) {
            $scope.processedResponse = angular.toJson(processedResponse, true);
            $scope.availableResources = angular.toJson(processedResponse._resources(), true);
        });
    });
