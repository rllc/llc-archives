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
        $scope.predicate = 'date';
        $scope.reverse = true;

        $scope.name = $routeParams.name;
        SpringDataRestAdapter.process(
            $http.get('api/congregations/search/findByName?name=' + $scope.name)
        ).then(function (processedResponse) {
                $scope.fullName = processedResponse._embeddedItems[0].fullName;
                SpringDataRestAdapter.process(
                    $http.get(processedResponse._embeddedItems[0]._links.sermons.href)
                ).then(function (sermonResponse) {
                        $scope.sermons = sermonResponse._embeddedItems;
                    });
            });


        $scope.orderByDate = function (item) {
            if (item.date) {
                return Date.parse(item.date);
            }
            return '';
        };

        $scope.sort = function (predicate) {
            if ($scope.predicate === predicate) {
                $scope.reverse = !$scope.reverse;
            }
            $scope.predicate = predicate;
        }

    });
