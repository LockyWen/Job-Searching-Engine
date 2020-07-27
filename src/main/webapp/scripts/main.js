(function(){
	var user_id = "1111";
	var user_fullname = "Locky";
	var lng = -73.75;
	var lat = 43.40;
	
	function init(){
		
		validateSession();
	}
	
	function validateSession(){
		onSessionInvalid();
	}
	
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
	


	

	
	
	init();
})()