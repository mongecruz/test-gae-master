'use strict';

angular.module('libro')
    .controller('ListCtrl', function ($scope, libro) {

        $scope.load = function() {
            libro.list(function (list) {
                $scope.list = list.data;
            });
        }
        
        
        $scope.listFilter = function() {
            libro.listFilterService($scope.textoBuscar, function (list) {
                $scope.list = list.data;
            });
        }


        $scope.guardarLibro = function() {
            libro.guardarLibro($scope.form, function() {
                $scope.load();
            });
        }
        
        $scope.eliminarLibro = function(l) {
            libro.eliminarLibro(l.id, function() {
                $scope.load();
            });
        }
        
        
//        $scope.buscarLibro = function(textoBusqueda)
//        {
//        	alert("texto a buscar: " + textoBusqueda);
//        }
        

        $scope.form = {};

        $scope.load();
    });
