'use strict';

/**
 * @ngdoc function
 * @name llcArchivesApp.controller:SermoncontrollerCtrl
 * @description
 * # SermoncontrollerCtrl
 * Controller of the llcArchivesApp
 */
angular.module('llcArchivesApp')
    .controller('SermonCtrl', function ($scope, $http, $routeParams, SpringDataRestAdapter) {
        $scope.name = $routeParams.name;
        SpringDataRestAdapter.process(
            $http.get('api/congregations/search/findByName?name=' + $scope.name)
        ).then(function (processedResponse) {
                SpringDataRestAdapter.process(
                    $http.get(processedResponse._embeddedItems[0]._links.sermons.href)
                ).then(function (sermonResponse) {
                        $scope.sermons = sermonResponse._embeddedItems;
                    });
            });
    });
