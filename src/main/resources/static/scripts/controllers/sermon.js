'use strict';

/**
 * @ngdoc function
 * @name llcArchivesApp.controller:SermoncontrollerCtrl
 * @description
 * # SermoncontrollerCtrl
 * Controller of the llcArchivesApp
 */
angular.module('llcArchivesApp')
    .controller('SermonCtrl', function ($scope, $http, $window, $location, $routeParams, SpringDataRestAdapter) {
        $scope.size = 10;
        $scope.sizes = [5, 10, 25, 50];
        $scope.currentPage = 0;
        $scope.predicate = 'date';
        $scope.reverse = true;
        $scope.name = $routeParams.name;
        $scope.fullName = '';
        $scope.query = '';


        // load congregations
        var httpPromise = $http.get('api/congregations?sort=name').success(function (response) {
            $scope.response = angular.toJson(response, true);
        });

        SpringDataRestAdapter.process(httpPromise).then(function (processedResponse) {
            $scope.congregations = processedResponse.content;
            angular.forEach($scope.congregations, function (congregation) {
                if (congregation.name === $routeParams.name) {
                    $scope.fullName = congregation.fullName;
                }
            });
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

        $scope.track = function (event) {
            $window.ga('send', {
                hitType: 'pageview',
                page: '/' + event.target.hash
            });
        };

        $scope.listen = function (event) {
            $window.ga('send', 'event', {
                eventCategory: 'Outbound Link',
                eventAction: 'click',
                eventLabel: event.target.href,
                transport: 'beacon'
            });
            window.setTimeout(function () {
                $window.location.href = event.target.href;
            }, 200);
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
                    $scope.sermons = sermonResponse.content;
                    $scope.pageData = sermonResponse.page;
                });
        };

        $scope.refreshSermons();

    })
    .filter('titlecase', function () {
        return function (input) {
            var smallWords = /^(a|an|and|as|at|but|by|en|for|if|in|nor|of|on|or|per|the|to|vs?\.?|via)$/i;

            input = input.toLowerCase();
            return input.replace(/[A-Za-z0-9\u00C0-\u00FF]+[^\s-]*/g, function (match, index, title) {
                if (index > 0 && index + match.length !== title.length &&
                    match.search(smallWords) > -1 && title.charAt(index - 2) !== ":" &&
                    (title.charAt(index + match.length) !== '-' || title.charAt(index - 1) === '-') &&
                    title.charAt(index - 1).search(/[^\s-]/) < 0) {
                    return match.toLowerCase();
                }

                if (match.substr(1).search(/[A-Z]|\../) > -1) {
                    return match;
                }

                return match.charAt(0).toUpperCase() + match.substr(1);
            });
        }
    });
