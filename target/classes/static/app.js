(function() {
'use strict';

    angular
        .module('app', [])
        .controller('MainController', MainController);

    MainController.$inject = ["$scope", "$http"];
    function MainController($scope, $http) {
        $scope.ingredients = [];
        $scope.recommendIngredients = [];
        $scope.recommendRecipes = [
            {name:"雜菜薯仔瘦肉湯",
            ingredients:["粟米","青紅蘿蔔","番茄","馬鈴薯","椰菜","瘦肉","薑片"],
            link:"https://icook.tw/recipes/124595"
            }
        ];
        $scope.isSearch=true;
        $scope.isResult=false;
        $scope.showRecipe=false;

        $scope.add = function() {
            var inputIngredient = $scope.ingredient;
            if(!inputIngredient) return;
            console.log($scope.ingredients.indexOf(inputIngredient));
            if($scope.ingredients.indexOf(inputIngredient) >= 0) return;
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

             $http({
                 method:"POST",
                 "url":'http://localhost:8080/recommend/ingredient',
                 "data":{"ingredients": $scope.ingredients},
                 "headers": {
                     "Content-Type": "application/json",
                     "Accept": "application/json"
                 }
             }).
             success(function(data) {
                 $scope.recommendIngredients = data;
             });

            $scope.isSearch = false;
            $scope.isResult = true;
            $scope.showRecipe=false;
        }

        $scope.recommend = function() {
            console.log("recommend");

             $http({
                 method:"POST",
                 "url":'http://localhost:8080/recommend/ingredient',
                 "data":{"ingredients": $scope.ingredients},
                 "headers": {
                     "Content-Type": "application/json",
                     "Accept": "application/json"
                 }
             }).
             success(function(data) {
                 $scope.recommendIngredients = data;
             });

            // $scope.isSearch = false;
            // $scope.isResult = true;
            $scope.showRecipe = true;
        }

        $scope.showSearch = function() {
            console.log("show search");
            $scope.isSearch = true;
            $scope.isResult = false;
        }

        $scope.showResult = function() {
            console.log("show result");
            $scope.isSearch = false;
            $scope.isResult = true;
        }

        $scope.resetSearch = function() {
            console.log("reset search");
            $scope.ingredients = [];
            $scope.showRecipe=false;
        }

        $scope.resetResult = function() {
            console.log("reset result");
            $scope.recommendIngredients = [];
            $scope.showRecipe=false;
        }
    }

    // ResultController.$inject = ["$scope"];
    // function ResultController($scope) {
    //     $scope.queryResults = [
    //         {name:"宮保雞丁", 
    //         ingredient:["雞肉", "辣椒"]},
    //         {name:"宮保雞丁", 
    //         ingredient:["雞肉", "辣椒"]},
    //         {name:"宮保雞丁", 
    //         ingredient:["雞肉", "辣椒"]}
    //     ];
    // }
})();
