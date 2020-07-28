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
	// Function showLoadingMessage
	// ========================================================	
	
	function showLoadingMessage(msg){
		var itemList = document.querySelector('#item-list');
		itemList.innerHTML = '<p class="notice"><i class="fa fa-spinner fa-spin"></i>' + msg + '</p>';
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
	

	
	
	
	init();
})()