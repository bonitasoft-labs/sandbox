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
    'xeditable',
    'case.overview'
    ]);

  app.controller('MainCtrl', ['$scope', function ($scope) {
    
  }])
  .filter('dateInMillis', function() {
    return function(dateString) {
      return Date.parse(dateString);
    };
  })
  .config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    $stateProvider.state('processMoreDetails', {
      url: '/admin/case/overview/:caseId',
      templateUrl: 'templates/caseOverview.html',
      controller: 'CaseOverviewCtrl',
      controllerAs : 'caseCtrl',
      resolve: { 
        processId: ['$stateParams', 'manageTopUrl',
          function($stateParams, manageTopUrl){
            manageTopUrl.addOrReplaceParam('_processId', $stateParams.processId || '');
            return $stateParams.processId+'';
          }
        ]
      }
      
    });
  }
  ]);


})();
