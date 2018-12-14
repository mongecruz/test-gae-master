'use strict';

angular.module('libro')
    .controller('DetailCtrl', function ($scope, libro, $routeParams) {
    	$scope.cargaDetalle = function() {
            libro.detail($routeParams.id, function (detalleLibro) {
//            	alert("detalleLibro.titulo: " + detalleLibro.titulo);
                $scope.detail = detalleLibro.data;
            });
        }

        $scope.cargaDetalle();
    });
