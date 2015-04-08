(function () {
  'use strict';
  var app = angular.module('caseOverview', [
    'ui.bootstrap',
    'ngResource',
    'org.bonita.common.resources',
    'angular-timeline'
    ]);

  app.controller('MainCtrl', ['$scope','$window', 'archivedTaskAPI', '$location', 'contextSrvc', 'dataSrvc', function ($scope, $window, archivedTaskAPI, $location, contextSrvc, dataSrvc) {
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


    var fetchContext = function(){
      contextSrvc.fetchCaseContext($location.search().id).then(function(result){
        var contextData;
        for (contextData in result.data) {
          fetchValue(result.data[contextData]);
        }
      });
    };

    $scope.businessData = {};
    var fetchValue = function(valueToFetch){
      // Implement fetching of data based on 2 strategies to illustrate the 2 possible capabilities. Using the link is
      // the most generic approach and should be preferred in most of the cases.
      // Using the type and value are most likely to be used to call a custom query on the API.
      if(angular.isObject(valueToFetch) && !angular.isArray(valueToFetch.value)){
        fetchDataFromTypeAndStorageId(valueToFetch);
      } else if(angular.isObject(valueToFetch) && angular.isArray(valueToFetch.value)) {
        fetchDataFromLink(valueToFetch);
      }
    };

    var fetchDataFromTypeAndStorageId = function(valueToFetch) {
      dataSrvc.getData(valueToFetch.type, valueToFetch.value).then(function(result){
        if(!angular.isDefined($scope.businessData[valueToFetch.type])) {
          $scope.businessData[valueToFetch.type] = [];
        }
        $scope.businessData[valueToFetch.type].push(result.data);
      });
    };

    var fetchDataFromLink = function(valueToFetch) {
      // Follow link to fetch multiple values
      dataSrvc.queryData(valueToFetch.link).then(function(result){
        if(!angular.isDefined($scope.businessData[valueToFetch.type])) {
          $scope.businessData[valueToFetch.type] = [];
        }
        $scope.businessData[valueToFetch.type] = $scope.businessData[valueToFetch.type].concat(result.data);
      });
    };

    listDoneTasks();
    fetchContext();
  }])
  .config(['$locationProvider',function($locationProvider) {
    $locationProvider.html5Mode({
      enabled: true,
      requireBase: false
    });
  }]);


})();
