app.factory('authenticationService', function($http, $rootScope,$q,socialAuthenticationService) {
	
	function authenticate(credentials) {
		var headers = credentials ? {
			authorization : "Basic " + btoa(credentials.username + ":" + credentials.password)
		} : {};
	  
		return $http.get('api/user', {headers : headers})
			.then(function(result) {
				return result.data
			}, function(e) {
				return $q.reject(e)
			});
	}			
	
	function getUser() {
		return authenticate()
	}
	
	function init() {
		getUser().then(function(data) {					
			if(data) {
				$rootScope.authenticated = true
				$rootScope.user = data
			} else {
				$rootScope.authenticated = false
				$rootScope.user = undefined
			}
		}, function(e) {
			$rootScope.authenticated = false
			$rootScope.user = undefined
		})
	}
	
	function loginCallback(data) {
		console.log(data)
		if(data) {
			$rootScope.authenticated = true
			$rootScope.user = data
			return data;
		} else {
			$rootScope.authenticated = false
			$rootScope.user = undefined
			return $q.reject({ status: 'USER_NOT_LOGGED_IN' });
		}		
	}
	
	function loginErrorCallback(err) {
		console.error('login failed', err)
		$rootScope.authenticated = false
		$rootScope.user = undefined
		return $q.reject(err);		
	}
	
	init()
	
	return {
		getUser: function() {
			return getUser()
		},
		login: function(credentials) {
			return authenticate(credentials)
			.then(loginCallback, loginErrorCallback);
		},
		loginWithTwitter: function() {
			return socialAuthenticationService
			.authenticateWithTwitter()
			.then(getUser)
			.then(loginCallback, loginErrorCallback);		
		},
		loginWithGoogle: function() {
			return socialAuthenticationService
			.authenticateWithGoogle()
			.then(getUser)
			.then(loginCallback, loginErrorCallback);		
		},
		loginWithGithub: function() {
			return socialAuthenticationService
			.authenticateWithGithub()
			.then(getUser)
			.then(loginCallback, loginErrorCallback);		
		},
		loginWithLinkedin: function() {
			return socialAuthenticationService
			.authenticateWithLinkedin()
			.then(getUser)
			.then(loginCallback, loginErrorCallback);		
		},								
		logout: function() {
			return $http.get('/api/user/logout')
			.then(function() {
				$rootScope.authenticated = false
				$rootScope.user = undefined
			});
		}
	}	
})