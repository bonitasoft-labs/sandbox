(function () {
  'use strict';
  var app = angular.module('caseOverview', [
    'ui.bootstrap',
    'ui.router',
    'angular-growl',
    'ngResource',
    'org.bonita.common.resources',
    'org.bonitasoft.bonitable',
    'org.bonitasoft.services.topurl',
    'gettext',
    'angular-timeline'
    ]);

  app.controller('MainCtrl', ['$scope','$window', 'archivedTaskAPI', '$location', function ($scope, $window, archivedTaskAPI, $location) {
    var listDoneTasks = function(){
      archivedTaskAPI.search({
        p:0,
        c:50,
        d:['executedBy'],
        f:['caseId='+$location.path().split('/')[2]]
      }).$promise.then(function mapArchivedTasks(data){
        $scope.doneTasks = data;
      });
    };
    listDoneTasks();
  }])
  .filter('dateInMillis', function() {
    return function(dateString) {
      return Date.parse(dateString);
    };
  })
  /*.config(['$locationProvider',function($locationProvider) {
    $locationProvider.html5Mode({
      enabled: true,
      requireBase: false
    });
  }]);*/
;


})();
