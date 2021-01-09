// Prepare HTTP headers for REST calls (CSRF protection).
function getHeaders() {
	var headers = Array();
	headers[_csrfHeaderName] = _csrfToken;
	return headers;
}

// Updates the view after a successful REST call.
function ajaxSuccess(data) {
	console.log("test2");
	$('#changePersonalDetails\\:firstName').val(data);
}

// Calls the set REST call - used as an event method for the SET button.
function saveUserInfo(e) {
	console.log("test");
	if (e) {
		e.preventDefault();
	}
	var headers = getHeaders();
	var value = $('#changePersonalDetails\\:firstName').val();
	var settings = {
		'headers': headers,
		'method': 'put',
		'success': ajaxSuccess,
		'contentType': 'application/json',
		'dataType': 'json',
		'data': value
	};
	$.ajax('/api/user/save', settings);
}
