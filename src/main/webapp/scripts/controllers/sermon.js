'use strict';

/**
 * @ngdoc function
 * @name llcArchivesApp.controller:SermoncontrollerCtrl
 * @description
 * # SermoncontrollerCtrl
 * Controller of the llcArchivesApp
 */
angular.module('llcArchivesApp')
    .controller('SermonCtrl', function ($scope, $http, $location, $routeParams, SpringDataRestAdapter) {
        $scope.size = 10;
        $scope.sizes = [5, 10, 25, 50];
        $scope.currentPage = 0;
        $scope.predicate = 'date';
        $scope.reverse = true;
        $scope.name = $routeParams.name;
        $scope.query = '';


        // load congregations
        var httpPromise = $http.get('api/congregations?sort=name').success(function (response) {
            $scope.response = angular.toJson(response, true);
        });

        SpringDataRestAdapter.process(httpPromise).then(function (processedResponse) {
            $scope.congregations = processedResponse._embeddedItems;
        });

        // utility functions
        $scope.orderByDate = function (item) {
            if (item.date) {
                return Date.parse(item.date);
            }
            return '';
        };

        $scope.isActive = function (path) {
            return (path === $location.path());
        };

        $scope.sort = function (predicate) {
            if ($scope.predicate === predicate) {
                $scope.reverse = !$scope.reverse;
            }
            $scope.predicate = predicate;
            $scope.refreshSermons();
        };

        $scope.page = function (direction) {
            if (direction === 'next') {
                if ($scope.pageData && $scope.currentPage < $scope.pageData.totalPages - 1) {
                    $scope.currentPage++;
                }
            }
            else if (direction === 'prev') {
                if ($scope.currentPage > 0) {
                    $scope.currentPage--;
                }
            }
            $scope.refreshSermons();
        };

        $scope.resetPage = function () {
            $scope.currentPage = 0;
            $scope.refreshSermons();
        };


        $scope.refreshSermons = function () {

            function buildQuery() {
                var query = '';

                var sortOrder = $scope.reverse ? 'desc' : 'asc';
                var sortQuery = $scope.predicate + ',' + sortOrder;
                if ($scope.name) {
                    query = 'api/sermons/search/findAnyFieldMatchingQueryByCongregation?name=' + $scope.name + '&sort=' + sortQuery;
                }
                else {
                    query = 'api/sermons/search/findAnyFieldMatchingQuery?sort=' + sortQuery;
                }
                query += '&page=' + $scope.currentPage;
                query += '&size=' + $scope.size;
                query += '&query=' + $scope.query;

                return query;
            }

            SpringDataRestAdapter.process(
                $http.get(buildQuery())
            ).then(function (sermonResponse) {
                    $scope.sermons = sermonResponse._embeddedItems;
                    $scope.pageData = sermonResponse.page;
                });
        };

        $scope.refreshSermons();

    });
