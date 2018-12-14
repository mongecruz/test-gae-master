'use strict';

angular.module('libro')
    .service('libro', function ($http) {
        return {
            detail: function (id, success) {
            	alert("detalle libro 2");
            	
                return $http.get("/rest/libro" + id).then(success);
            }
           
        };
    });
