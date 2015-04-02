'use strict';

describe('Case Overview test', function () {

  var $httpBackend, contextSrvc;

  beforeEach(module('caseOverview'));

  beforeEach(function () {

    inject(function ($injector) {
      // Set up the mock http service responses
      $httpBackend = $injector.get('$httpBackend');
      contextSrvc = $injector.get('contextSrvc');
    });
  });

  afterEach(function () {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

  it('should declare a fetch function', function () {

    expect(contextSrvc.fetchCaseContext).toBeTruthy();
  });

  it('should return a context with 3 values', function () {

    var context = {key1: 'val1'};
    var response;

    $httpBackend.expect('GET', '/bonita/API/bdm/businessDataReference?f=caseId=2&p=0&c=100')
      .respond(context);
    contextSrvc.fetchCaseContext(2).then(function (fetchedData) {
      response = fetchedData.data;
    });
    $httpBackend.flush();
    expect(response).toEqual(context);
  });

});
