'use strict';

angular.module('comparativestockApp').controller('MainController', function($scope, Principal, $http) {
    
    var textColor = "#87FF95";
    var backgroundColor = "#28262C";
    var legendTextColor = "#B5A6D3";
    var legendHoverColor = "#ffffff";
    var legendHiddenColor = "#1D2F43";

    $scope.checkboxModel = {};
    $scope.chartConfig = {};
    $scope.loaded = false;
    $scope.yearsback = 1;
    $scope.showSearchList = false;

    var textStyle = {
        color : textColor
    };
    var axisLabelStyle = {
        style : textStyle
    };

    Principal.identity().then(function(account) {
        $scope.account = account;
        $scope.isAuthenticated = Principal.isAuthenticated;
    });

    $scope.updateSearch = function() {
        $http({
            method : 'GET',
            url : '/api/getticker/' + $scope.stockSearch
        }).then(function(result) {
        	$scope.showSearchList = true;
            $scope.searchResult = result.data;
        });
    };

    $scope.check = function() {
        console.log($scope.checkboxModel);
    };
    $scope.submit = function() {
        var options = {
            method : 'POST',
            url : '/api/submitstocks?yearsback=' + $scope.yearsback,
            headers : {
                "Content-Type" : "application/json"
            },
            data : $scope.checkboxModel
        };
        $http(options).then(function(result) {
        	$scope.showSearchList = false;
            var series = [];

            var d = new Date();

            $scope.chartConfig = {
                options : {
                    chart : {
                        type : 'spline',
                        backgroundColor : backgroundColor,
                        color : textColor
                    },
                    tooltip : {
                        style : {
                            padding : 10,
                            fontWeight : 'bold'
                        },
                        pointFormat : 'Stock Price of <span style="color: {series.color}">{series.name}</span>: {point.y:f}',
                        xDateFormat : '%Y-%m-%d',
                    },
                    legend : {
                        itemStyle : {
                            color : legendTextColor
                        },
                        itemHoverStyle : {
                            color : legendHoverColor
                        },
                        itemHiddenStyle : {
                            color : legendHiddenColor
                        }
                    }
                },
                series : result.data,
                title : {
                    text : "Stock Prices (USD)",
                    style : textStyle
                },
                xAxis : {
                    type : 'datetime',
                    title : {
                        text : 'Date (Day-Month-Year)',
                        style : textStyle
                    },
                    labels : axisLabelStyle,
                    dateTimeLabelFormats: { // don't display the dummy year
                        month: '%e. %b %Y',
                        year: '%b'
                    },
                },
                yAxis : {
                    title : {
                        text : 'Stock Prices (USD)',
                        style : textStyle
                    },
                    min : 0,
                    labels : axisLabelStyle
                },
                loading : false
            };
            $scope.loaded = true;
        });
    };
});
