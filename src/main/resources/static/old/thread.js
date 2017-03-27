var app_thread = angular.module('app_thread', ["ngRoute"]);

var threadId = getURLParameter('id');

//taken from stackoverflow here: http://stackoverflow.com/questions/11582512/how-to-get-url-parameters-with-javascript
//used to get the threadid
function getURLParameter(name) {
  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [null, ''])[1].replace(/\+/g, '%20')) || null;
}

app_thread.controller('ctrl1thread', function($scope, $http){
	var getThreadPosts = 'posts/threadid/' + threadId;
	$http.get(getThreadPosts).then(function (response) {
		$scope.posts = response.data;
		//SORT posts BY LATEST POST/ timestamp / id +  add username somehow to connect username with text. now only user id
	});
});

//get the thread object for the thread with id in the url. write the threadname to h3 in thread.html
app_thread.controller('ctrlThreadName',  function($scope, $http){
	var threadVar = 'threads/id/' + threadId;
	$http.get(threadVar).then(function (response) {
		$scope.thread = response.data;
	});

});
