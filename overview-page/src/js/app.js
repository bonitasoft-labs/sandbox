(function () {
  'use strict';
  angular.module('devMode', []); // This module is overloaded at dev time to contribute development facilities.
  var app = angular.module('caseOverview', [
    'devMode',
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
