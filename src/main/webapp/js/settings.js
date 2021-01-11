// Prepare HTTP headers for REST calls (CSRF protection).
function getHeaders() {
	var headers = Array();
	headers[_csrfHeaderName] = _csrfToken;
	return headers;
}

// Updates the view after a successful REST call.
function ajaxSuccess(data) {
	$('#changePersonalDetails\\:firstName').val(data.firstName);
	$('#changePersonalDetails\\:lastName').val(data.lastName);
	alertify.success('User successfully updated.', 2);
}

// Calls the set REST call - used as an event method for the SET button.
function saveUserInfo(e) {

	if (e) {
		e.preventDefault();
	}

	let headers = getHeaders();

	let values = {
		'firstName': $('#changePersonalDetails\\:firstName').val(),
		'lastName': $('#changePersonalDetails\\:lastName').val()
	};

	let data = JSON.stringify(values);

	let settings = {
		'headers': headers,
		'method': 'put',
		'success': ajaxSuccess,
		'contentType': 'application/json',
		'dataType': 'json',
		'data': data
	};

	$.ajax('/api/user/save', settings);
}
