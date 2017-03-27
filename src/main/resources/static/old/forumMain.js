/*
The "app_forumMain" parameter refers to an HTML element in which the application will run.
Now you can add controllers, directives, filters, and more, to your AngularJS application.
*/

var app_forumMain = angular.module('app_forumMain', ["ngRoute"]); // ngrouting for switching html page views. Not used atm

//used for testing when http rest api not available
//var threads = [{"threadname": "thread 1" , "id": 1, "user" : "johan"} , {"threadname": "thread 2" , "id": 2, "user" : "eddan"} ];
//var posts = [{"id": 1, "user" : "johan", "text" : "á post by user johan, this is the text element"}];

/* Used for testing when rest api not available
app_forumMain.controller('ctrl1forumMain', function($scope){
		
		$scope.threads= threads;

		//SORT threads BY LATEST POST/ LATEST CREATED.

		$scope.enterThread = function(threadObj){
			 window.location.href = "thread.html?id=" + threadObj.id; //passing the id of the clicked thread as url parameter

		}
});
*/

 //OBS: använd detta istället för samma funktion ovan
//Use for later when using REST
app_forumMain.controller('ctrl1forumMain', function($scope, $http){
	$http.get('threads/all').then(function (response) {
		$scope.threads = response.data;
		//SORT threads BY LATEST POST/ LATEST CREATED.
	});

	$scope.enterThread = function(threadObj){
			 window.location.href = "thread.html?id=" + threadObj.id; //passing the id of the clicked thread as url parameter
	}
});



/*
Used for changing views. Can be used instead of window.location.href  as above in app_forumMain.controller
app_forumMain .config(function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "thread.html"
    })
});
*/
