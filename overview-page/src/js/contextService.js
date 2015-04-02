(function () {
  'use strict';
  angular.module('caseOverview').factory('contextSrvc', ['$http', function($http) {


    function appendTransform(defaults, transform) {

      // We can't guarantee that the default transformation is an array
      defaults = angular.isArray(defaults) ? defaults : [defaults];

      // Append the new transformation to the defaults
      return defaults.concat(transform);
    }

    return {
      fetchCaseContext: function (caseId) {
        // fetch context from server
        return $http({
          url: '/bonita/API/bdm/businessDataReference?f=caseId=' + caseId + '&p=' + 0 + '&c=' + 100,
          method: 'GET',
          transformResponse: appendTransform($http.defaults.transformResponse, function (value) {
            console.log(value);
            return value;
          })
        });
      }
    }
  }]);
})();
