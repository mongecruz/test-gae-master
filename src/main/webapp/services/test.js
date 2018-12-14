'use strict';

angular.module('libro')
    .service('libro', function ($http) {
        return {
            list: function (success) {
                return $http.get("/rest/libro").then(success);
            },
            listFilterService: function (filtro, success) {
                return $http.get("/rest/libro/busquedaLibros/" + filtro).then(success);
            },
            guardarLibro: function (libro, success) {
                return $http.post("/rest/libro", libro).then(success);
            },
            eliminarLibro: function (id, success) {
                return $http.delete("/rest/libro/" + id).then(success);
            },
            detail: function (id, success) {
                return $http.get("/rest/libro/" + id).then(success);
            }
        };
    });
