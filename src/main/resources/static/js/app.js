var app = angular.module('myApp', []);

app.controller('MainCtrl', ['authService','$scope','$http',
        function(authService, $scope, $http) {

            $scope.token = null;
            $scope.createUserError = null;
            $scope.loginError = null;

            // permission variables
            $scope.hasRoleUser  = false;
            $scope.hasRoleAdmin = false;

            // view variables
            $scope.threadView = false;
            $scope.postView = false;

            // data variables
            $scope.currentThread = null;

            function clearForms() {
                $scope.login.username = '';
                $scope.login.password = '';
                $scope.newUser.username = '';
                $scope.newUser.password = '';
                $scope.newUser.email = '';
            }

            $scope.createNewUser = function () {
                $scope.createUserError = null;
                $scope.loginError = null;

                var user = {
                    name:       $scope.newUser.username,
                    email:      $scope.newUser.email,
                    password:   $scope.newUser.password
                };

                $http.post('user/create', user)
                    .then(function (response) {
                        console.log(response.data);
                        $scope.token = response.data.token;
                        $http.defaults.headers.common.Authorization = 'Bearer ' + $scope.token;

                        $scope.checkRoles();
                        $scope.getAllThreads();
                        $scope.threadView = true;
                        $scope.userName = $scope.newUser.username;
                    }, function (error) {
                        $scope.createUserError = error;
                        $scope.userName = '';
                    });

                clearForms();
            };

            // login function, called
            $scope.login = function() {
                $scope.loginError = null;
                $scope.createUserError = null;

                authService.login($scope.login.username, $scope.login.password)
                .then(function(token) {  // put auth token in header
                    $scope.token = token;
                    $http.defaults.headers.common.Authorization = 'Bearer ' + token;

                    // check roles of the logged in user
                    $scope.checkRoles();

                    $scope.getCategories();

                    // load the threads and enable the thread view
                    $scope.getAllThreads();
                    $scope.threadView = true;
                    $scope.userName = $scope.login.username;
                },
                // handle possible login error
                function(error) {
                    $scope.loginError = error;
                    $scope.userName = '';
                });
            };

            $scope.viewThreads = function () {
                $scope.getAllThreads();
                $scope.postView = false;
                $scope.threadView = true;
            };

            $scope.viewTrendingThreads = function() {
                $scope.getTrendingThreads();
                $scope.postView = false;
                $scope.threadView = true;
            };

            //The computation of finding the most trending threads
            // is done in the backend, could be done here most probably, matter of design choice
            $scope.getTrendingThreads = function(){
                $http.get('thread/trending')
                .then(function(response) {
                    $scope.threads = response.data;
                }, function (error){
                    $scope.error = error;
                });
            };

            // get all forum threads
            $scope.getAllThreads = function() {
                $http.get('thread/all')
                .then(function(response) {
                    $scope.threads = response.data;
                }, function (error) {
                    $scope.error = error;
                });
            };

            // add a new thread
            $scope.addThread = function() {
                var newThread = {
                    title: $scope.newThread.title,
                    description: $scope.newThread.description,
                    categoryId: $scope.newThread.category.split('.')[0]
                };

                $http.post('thread/add', newThread)
                    .then(function(response) {
                        console.log(response.data);
                        $scope.getAllThreads();
                    }, function(error) {
                        console.log(error);
                    });

                $scope.newThread = null;
            };

            // delete thread
            $scope.deleteThread = function(thread) {
                $http.post('thread/' + thread.id + '/delete')
                    .then(function(response) {
                        console.log("thread deleted");
                        $scope.getAllThreads();
                    }, function(error) {
                        console.log(error);
                    });
            };

            // select a thread and change to post view
            $scope.viewPosts = function(thread) {
                $scope.currentThread = thread;
                $scope.getThreadPosts(thread);
                $scope.threadView = false;
                $scope.postView = true;
            };

            // add a post to the current thread
            $scope.addPost = function() {

                var threadId = $scope.currentThread.id;
                var newPost = {
                    threadId: threadId,
                    content: $scope.post.content
                };

                $http.post('/thread/' + threadId + '/addPost', newPost)
                .then(function(response) {
                    $scope.getThreadPosts($scope.currentThread);
                    console.log($scope.posts);

                }, function(error) {
                    $scope.error = error;
                });

                $scope.post.content = '';
            };

            $scope.deletePost = function(post) {
                $http.post('post/' + post.id + '/delete')
                    .then(function(response) {
                        console.log("post deleted");
                        $scope.getThreadPosts($scope.currentThread);
                    }, function(error) {
                        console.log(error.message);
                    });
            };

            // get all posts of a thread
            $scope.getThreadPosts = function(thread) {
                $http.get('thread/' + thread.id + '/posts')
                .then(function(response) {
                    $scope.posts = response.data;
                }, function(error) {
                    $scope.error = error;
                });
            };

            // check roles of the logged in user
            $scope.checkRoles = function() {
                authService.getRoles().then(function(roles) {
                    for (i = 0; i < roles.length; i++) {
                        if (roles[i] == "user") {
                            $scope.hasRoleUser = true;
                        }
                        if (roles[i] == "admin") {
                            $scope.hasRoleAdmin = true;
                        }
                    }
                    console.log("admin: " + $scope.hasRoleAdmin);
                    console.log("user: " + $scope.hasRoleUser);
                });
            };

            $scope.getCategories = function() {
                $http.get('categories/all')
                    .then(function(response) {
                        $scope.categories = response.data;
                        console.log($scope.categories);
                    }, function(error) {
                        $scope.error = error;
                    });
            };

            $scope.getThreadsInCategory = function(category) {
                $http.get('categories/' + category.id + '/threads')
                    .then(function(response) {
                        $scope.threads = response.data;
                    }, function (error) {
                        $scope.error = error;
                    });

                $scope.postView = false;
                $scope.threadView = true;
            };

            // logout function
            $scope.logout = function() {
                // reset all local variables
                $scope.login.userName = '';
                $scope.login.password = '';
                $scope.token = null;
                $scope.hasRoleAdmin = false;
                $scope.hasRoleUser  = false;
                $scope.threadView = false;
                $scope.postView = false;
                // remove authorization header for future requests
                $http.defaults.headers.common.Authorization = '';
            };

            // check if logged in
            $scope.loggedIn = function() {
                return $scope.token !== null;
            }
}]);


// authentication service
app.service('authService', function($http) {
    return {
        login : function(username, password) {
            return $http.post('/user/login', {
                name: username,
                password: password
            }).then(function(response) {
                return response.data.token;
            });
        },

        getRoles : function() {
            return $http.get('/user/role/')
                .then(function(response) {
                    return response.data;
            });
        }
    };
});