(function(){
	var user_id = "1111";
	var user_fullname = "Locky";
	var lng = -73.75;
	var lat = 43.40;
	// ========================================================
	// Function init
	// ========================================================
	function init(){
		
		// Register event listeners
		document.querySelector('#login-form-btn').addEventListener('click', onSessionInvalid);
		document.querySelector('#register-form-btn').addEventListener('click', showRegisterForm);
		document.querySelector('#register-btn').addEventListener('click', register);
		document.querySelector('#login-btn').addEventListener('click', login);
		
		validateSession();
	}
	
	// ========================================================
	// Function validateSession
	// ========================================================
	
	function validateSession(){
		onSessionInvalid();
		// The request parameters
		var url = './login';
		var req = JSON.stringify({});
		
		// Display loading message 
		showLoadingMessage("Validate session ... ");
		
		// make AJAX call
		ajax('GET', url, req,
		// session is still valid 
		function(res){
			var result = JSON.parse(res);
			
			if(result.status==='OK'){
				onSessionValid(result);
			}
		}, function(){
			console.log('login error');
		}
		);
	}
	
	// ========================================================
	// Function showLoadingMessage; onSessionValid
	// ========================================================	
	
	function showLoadingMessage(msg){
		var itemList = document.querySelector('#item-list');
		itemList.innerHTML = '<p class="notice"><i class="fa fa-spinner fa-spin"></i>' + msg + '</p>';
	}
	
	function onSessionValid(result){
		user_id = result.user_id;
		user_fullname = result.name;
		
		var loginForm = document.querySelector('#login_form');
		var registerForm = document.querySelector('#register-form');
		var itemNav = document.querySelector('#item-nav');
		var itemList = document.querySelector('#item-list');
		var avatar = document.querySelector('#avatar');
		var welcomMsg = document.querySelector('#welcome-msg');
		var logoutBtn = document.querySelector('#logout-link');
		
		welcomeMsg.innerHTML = "Welcome, " + user_fullname;
		
		showElement(itemNav);
		showElement(itemList);
		showElement(avatar);
		showElement(welcomeMsg);
		showElement(logoutBtn, 'inline-block');
		hideElement(loginForm);
		hideElement(registerForm);
		
		initGeoLocation();
	}
	
	
	// ========================================================
	// Function onSessionInvalid
	// ========================================================	
	/**
	 Initial State: Only show login form and hide other elements. 
	 */
	function onSessionInvalid(){
		var loginForm = document.querySelector('#login_form');
		var registerForm = document.querySelector('#register-form');
		var itemNav = document.querySelector('#item-nav');
		var itemList = document.querySelector('#item-list');
		var avatar = document.querySelector('#avatar');
		var welcomMsg = document.querySelector('#welcome-msg');
		var logoutBtn = document.querySelector('#logout-link');
		
		hideElement(itemNav);
		hideElement(itemList);
		hideElement(avatar);
		hideElement(logoutBtn);
		hideElement(welcomeMsg);
		hideElement(registerForm);
		
		clearLoginError();
		showElement(loginForm)
	}
	
	
	// ========================================================
	// Function hideElement; clearLoginError; showElement
	// ========================================================	
	/**
	 * Hide the element
	 */
	function hideElement(element){
		element.style.display = 'none';
	}
	
	/**
	Show that there is no login Error
	 */
	function clearLoginError(){
		document.querySelector('#login-error').innerHTML = '';
	}
	
	/**
	 If no style input, then the element will be shown as "block"
	 ELse, the element will be shown as the target style
	 */
	function showElement(element, style){
		var displayStyle = style ? style : 'block';
		element.style.display = displayStyle;
	}
	
	// ========================================================
	// Function showRegisterForm ; clearRegisterResult
	// ========================================================	
	
	/**
	Show register form and hide another element. 
	 */
	function showRegisterForm(){
		var loginForm = document.querySelector('#login_form');
		var registerForm = document.querySelector('#register-form');
		var itemNav = document.querySelector('#item-nav');
		var itemList = document.querySelector('#item-list');
		var avatar = document.querySelector('#avatar');
		var welcomMsg = document.querySelector('#welcome-msg');
		var logoutBtn = document.querySelector('#logout-link');
		
		hideElement(itemNav);
		hideElement(itemList);
		hideElement(avatar);
		hideElement(logoutBtn);
		hideElement(welcomeMsg);
		hideElement(loginForm);
		
		clearRegisterResult();
		showElement(registerForm);
	}
	
	function clearRegisterResult(){
		document.querySelector('#register-result').innerHTML = '';
	}
	
	
	//--------------------------------------
	// Function : Register
	//--------------------------------------
	
	function register(){
		var username = document.querySelector('#register-username').value;
		var password = document.querySelector('#register-password').value;
		var firstName = document.querySelector('#register-first-name').value;
		var lastName = document.querySelector('#register-last-name').value;
		
		if(username === "" || password == "" || firstName === "" || lastName === ""){
			showRegisterResult('Please fill in all fields');
			return;
		}
		
		if(username.match(/^[a-z0-9_]+$/) === null){
			showRegisterResult('invalid username');
			return;
		}
		
		
		//Encrypt the username and password
		password = md5(username + md5(password));
		
		var url = "./register";
		var req = JSON.stringify(
			{user_id : username,
			 password: password,
			 first_name : firstName,
			 last_name : lastName
			}
		);
		
		// Run ajax function 
		ajax('POST', url, req, 
		function(res){
			var result = JSON.parse(res);
			// successfully logged in
			if(result.status === 'OK'){
				showRegisterResult('Successfully registered');
			}else{
				showRegisterResult('User already existed');
			}
		},
		// Error
		function(){
			showRegisterResult('Failed to register');
		});
	}
	
	// ========================================================
	// Function ajax ; showRegisterResult ; clearRegisterResult
	// ========================================================	
	function ajax(method, url, data, succCb, errCb){
		var xhr = new XMLHttpRequest();
		xhr.open(method, url, true);
		
		xhr.onload = function(){
			if(xhr.status === 200){
				succCb(xhr.responseText);
			}else{
				errCb();
			}
		}
		
		xhr.onerror = function(){
		console.error("The request couldn't be completed");
		errCb();
		};
		
		if(data === null){
			xhr.send();
		}else{
			xhr.setRequestHeader("Content-type", "application/json;charset=utf-8");
			xhr.send(data);
		}
	}
	
	
	function showRegisterResult(registerMessage){
		document.querySelector('#register-result').innerHTML = registerMessage;
	}
	
	function clearRegisterResult(){
		document.querySelector('#register-result').innerHTML = '';
	}
	
	//======================================
	// Function : login ; showLoginError
	//======================================
	
	function login(){
		
		var username = document.querySelector('#username').value;
		var password = document.querySelector('#password').value;
		password = md5(username + md5(password));
		
		// The request parameters
		var url = './login';
		var req = JSON.stringify({
			user_id : username,
			password : password
		});
		
		ajax('POST', url, req,
		//successful callback
		function(res){
			var result = JSON.parse(res);
			// successful login 
			if (result.status === 'OK'){
				onSessionValid(result);
			}
		}, 
		//error
		function(){
			showLoginError();
		}, true);
	}
	
	function showLoginError(){
		document.querySelector('#login-error').innerHTML = 'Invalid username or password';
	}
	
	//=====================================================
	// Functions: get geolocation
	//=====================================================
	
	function initGeolocation(){
		if(navigator.geolocation){
			navigator.geolocation.getCurrentPosition(onPositionUpdated, onLoadPositionFailed, {maximumAge: 60000});
			showLoadingMessage('Retrieving your location ... ');
		}else{
			onLoadPositionFailed();
		}
	}
	
	function onPositionUpdated(position){
		lat = position.coords.latitude;
		lng = position.coords.longitude;
		loadNearbyItems();
	}
	
	function onLoadPositionFailed(){
		console.warn('navigator.geolocation is not valid');
		getLocationFromIP();
	}
	
	function getLocationFromIP(){
		//get location from http://ipinfo.io/json
		var url = 'http://ipinfo.io/json'
		var data = null;
		
		ajax('GET', url, data, function(res){
			var result = JSON.parse(res);
			if('loc' in result){
				var loc = result.loc.split(',');
				lat = loc[0];
				lng = loc[1];
			}else{
				console.warn('Getting location by IP failed.');
			}
			loadNearbyItems();
		});
	}
	
	//=======================================
	// Functions : loadNearbyItems
	//=======================================
	
	/**
	  API 1# Load the nearby items API end point: [GET]
	  /search?user_id=1111&lat=37.38&lon=-122.08
	 */
	
	function loadNearbyItems(){
		console.log('loadNearbyItems');
		activeBtn('nearby-btn');
		
		// The request parameters
		var url = './search';
		var params = 'user_id=' + user_id + '&lat=' + lat + '&lon=' + lng;
		var data = null;
		
		//display loading message
		showLoadingMessage('Loading nearby items...');
		
		// make AJAX call
		ajax('GET', url + '?' + params, data,
		//successful callback
		function(res){
			var items = JSON.parse(res);
			if(!items || items.length === 0){
				showWarningMessage('No nearby items. ');
			}else{
				listItems(items);
			}
		},
		//failed callback
		function(){
			showErrorMessage('Cannot load nearby items');
		});
	}
	
	/**
	 A helper function that makes a navigation button active 

	 @param btnId The id of the navigation btn 
	 */
	function activeBtn(btnId){
		var btns = document.querySelectorAll('.main-nav-btn');
		
		//deactivate all navigation buttons
		for(var i=0; i<btn.length; i++){
			btns[i].className = btns[i].className.replace(/\bactive\b/, '');
		}
		
		//active the one that has id = btnId
		var btn = document.querySelector('#' + btnId);
		btn.className += 'active';
	}
	
	function showLoadingMessage(msg){
		var itemList = document.querySelector('#item-list');
		itemList.innerHTML = '<p class="notice"><i class="fa fa-spinner fa-spin"></i>' + msg + '</p>';
	}
	
	function showWarningMessage(msg){
		var itemList = document.querySelector('#item-list');
		itemList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-triangle"></i>' + msg + '</p>';
	}
	
	function showLoadingMessage(msg){
		var itemList = document.querySelector('#item-list');
		itemList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-circle"></i>' + msg + '</p>';
	}
	
	
	//=======================================
	// Function : create item list
	//=======================================
	
	/**
	A helper function that creates a DOM element 
	
	@param tag the tag of the target element
	@param options the class / id of the target element
	@returns the element
	 */
	function $create(tag, options){
		var element = document.createElement(tag);
		for(var key in options){
			if(options.hasOwnProperty(key)){
				element[key] = options[key];
			}
		}
		return element;
	}
	
	/**
		List recommendation items base on the data received
		@param items An array of item JSON objects
	 */
	function listItems(items){
		var itemList = document.querySelector('#item-list');
		itemList.innerHTML = ''; // clear current results
	}
	
	
	
	init();
})()