(function () {
  'use strict';
  var app = angular.module('caseOverview', [
    'ui.bootstrap',
    'ngResource',
    'org.bonita.common.resources',
    'angular-timeline'
    ]);

  app.controller('MainCtrl', ['$scope','$window', 'archivedTaskAPI', '$location', function ($scope, $window, archivedTaskAPI, $location) {
    var listDoneTasks = function(){
      archivedTaskAPI.search({
        p:0,
        c:50,
        d:['executedBy'],
        //f:['caseId='+$location.path().split('/')[5]],
        f:['caseId='+$location.search().id],
        o:['reached_state_date DESC']
      }).$promise.then(function mapArchivedTasks(data){
        $scope.doneTasks = data;
      });
    };
    listDoneTasks();
  }])
  .config(['$locationProvider',function($locationProvider) {
    $locationProvider.html5Mode({
      enabled: true,
      requireBase: false
    });
  }]);


})();
