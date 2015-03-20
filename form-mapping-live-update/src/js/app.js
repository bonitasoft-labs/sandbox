(function () {
  'use strict';
  var app = angular.module('formMappingList', [
    'org.bonita.common.resources',
    'ui.bootstrap', 
    'ui.router',
    'angular-growl',
    'ngResource',
    'org.bonitasoft.bonitable',
    'xeditable'
  ]);

  app.controller('FormMappingListCtrl', ['$scope', '$q', 'formMappingAPI', 'processAPI','customPageAPI', 'growl', function ($scope, $q, formMappingAPI, processAPI, customPageAPI, growl) {
    $scope.selectedProcess = undefined;
    $scope.taskMappingResults = undefined;
    $scope.startMappingResult= undefined;
    $scope.overviewMappingResult = undefined;
    var searchTaskFormMapping = function(){
      searchFormMapping('TASK');
    };
    var searchStartFormMapping = function(){
      searchFormMapping('PROCESS_START');
    }; 
    var searchOverviewFormMapping = function(){
      searchFormMapping('PROCESS_OVERVIEW');
    };

    var searchFormMapping = function(type){
      if($scope.selectedProcess){
        formMappingAPI.search({
          p: 0,
          f: ['processDefinitionId='+$scope.selectedProcess.id, 'type='+type ],
          c: 1000
        }).$promise.then(function mapMapping(mapping) {
          switch(type){
            case 'TASK': $scope.taskMappingResults = mapping.data; break;
            case 'PROCESS_START':  $scope.startMappingResult = mapping.data; break;
            case 'PROCESS_OVERVIEW':  $scope.overviewMappingResult = mapping.data; break;
          }
        });
      }
    };

    var searchCustomPage = function(){
        customPageAPI.search({
          p: 0,
          c: 1000, 
          o: 'displayName ASC'
        }).$promise.then(function mapMapping(customPages) {
          var result = [];
          var customPagesData = customPages.data;
          for(var i = 0; i<customPagesData.length; i++){
            result.push(customPagesData[i].urlToken);
          }
          $scope.customPagesResult = result;
        });
    };
    searchCustomPage();


    var searchProcess = function(){
      processAPI.search({
        p: 0,
        c: 1000,
        o: 'displayName ASC'
      }).$promise.then(function mapMapping(processes) {
        $scope.processesResults = processes.data;
      });
    }; 

    searchProcess();

    $scope.$watch('selectedProcess',function(){
        searchTaskFormMapping();
        searchStartFormMapping();
        searchOverviewFormMapping();
      });

    $scope.updateMapping = function (formMapping) {

      //build the payload
      var formMappingPayload ={
        form: formMapping.form,
        target: formMapping.target
      };

      formMappingAPI.update({
        id: formMapping.id
      }, formMappingPayload).$promise.then(function recallSearchFormMappingAndConfirm(){
        switch(formMapping.type){
          case 'TASK': searchTaskFormMapping(); break;
          case 'PROCESS_START': searchStartFormMapping(); break;
          case 'PROCESS_OVERVIEW': searchOverviewFormMapping(); break;
        }
        growl.success('Form mapping updated', { ttl: 3000, disableCountDown: true, disableIcons: true });
      }
      );
    };

    $scope.statuses = [
          {value: 'INTERNAL', text: 'INTERNAL'},
          {value: 'LEGACY', text: 'LEGACY'},
          {value: 'URL', text: 'URL'}
    ];

  }])

.config(['$stateProvider',  function($stateProvider) {
  $stateProvider.state('bonita.processformMapping', {
    url: '/admin/process/form/mapping',
    templateUrl: 'features/admin/process/form-mapping/process-form-mapping-list.html',
    controller: 'FormMappingListCtrl',
    controllerAs : 'processCtrl'
  });
}
])
.config(['growlProvider',function (growlProvider) {
  growlProvider.globalPosition('top-center');
}])
.run(['editableOptions', function(editableOptions) {
    editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
  }]);
})();