(function() {
'use strict';

    angular
        .module('app', [])
        .controller('MainController', MainController)
        .controller("ResultController", ResultController);

    MainController.$inject = ["$scope"];
    function MainController($scope) {
        $scope.ingredients = [];
        $scope.test = {aaa:"bbb"};

        $scope.add = function() {
            var inputIngredient = $scope.ingredient;
            if(!inputIngredient) return;
            console.log("add, " + inputIngredient);
            $scope.ingredients.push(inputIngredient);
            $scope.ingredient = "";
        };

        $scope.enter = function($event) {
            // console.log("enter");
            if ($event.which === 13) {
                console.log("hit enter")
                $scope.add();
            }
        }

        $scope.search = function() {
            console.log("search");
        }
    }

    ResultController.$inject = ["$scope"];
    function ResultController($scope) {
        $scope.queryResults = [
            {name:"宮保雞丁", 
            ingredient:["雞肉", "辣椒"]},
            {name:"宮保雞丁", 
            ingredient:["雞肉", "辣椒"]},
            {name:"宮保雞丁", 
            ingredient:["雞肉", "辣椒"]}
        ];
    }
})();
