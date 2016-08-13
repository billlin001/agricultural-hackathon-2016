(function() {
    'use strict';

    angular
        .module('app', [])
        .controller('Hello', Hello);

    Hello.$inject = ["$scope", "$http"];
    function Hello($scope, $http) {
        $http.get('http://localhost:8080/greeting').
        success(function(data) {
            $scope.greeting = data;
        });
    }

})();

