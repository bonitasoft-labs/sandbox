(function () {
  'use strict';
  var app = angular.module('case.overview', [
    'ui.bootstrap',
    'ui.router',
    'angular-growl',
    'ngResource',
    'org.bonita.common.resources',
    'org.bonitasoft.bonitable',
    'org.bonitasoft.services.topurl',
    'gettext',
    'xeditable',
    'isteven-multi-select'
    ]);

  app
  .controller('CaseOverviewCtrl', ['$scope', '$stateParams', 'processAPI','processSupervisorAPI','actorAPI', 'processParameterAPI', 'processConnectorAPI','caseAPI', 'categoryAPI', 'userAPI', 'groupAPI', 'roleAPI', 'actorMemberAPI', 'customPageAPI', 'formMappingAPI', function ($scope, $stateParams, processAPI, processSupervisorAPI, actorAPI, processParameterAPI, processConnectorAPI, caseAPI, categoryAPI, userAPI, groupAPI, roleAPI, actorMemberAPI, customPageAPI, formMappingAPI ) {
    

  }])
.config(['growlProvider',function (growlProvider) {
  growlProvider.globalPosition('top-center');
}])
.run(['editableOptions', function(editableOptions) {
    editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
  }]);
})();
