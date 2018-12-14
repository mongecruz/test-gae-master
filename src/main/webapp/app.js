'use strict';
angular
    .module('libro', ['ngRoute'])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                //templateUrl: 'views/list.html',
            	templateUrl: 'views/listadoLibros.html',
                controller: 'ListCtrl',
                controllerAs: 'list'
            })
            .when('/:id', {
                templateUrl: 'views/detalleLibro.html',
                controller: 'DetailCtrl',
                controllerAs: 'detail'
            })
            .otherwise({
                redirectTo: '/'
            });
    });
