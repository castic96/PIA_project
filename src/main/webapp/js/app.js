var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect(csrf) {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({ "X-CSRF-TOKEN": csrf }, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

// $(function () {
//     $("form").on('submit', function (e) {
//         e.preventDefault();
//     });
//     $( "#connect" ).click(function() { connect($(this).attr("data-csrf")); });
//     $( "#disconnect" ).click(function() { disconnect(); });
//     $( "#send" ).click(function() { sendName(); });
// });


function openUserEvent(e) {
    if (e.status == 'success') {
        $('#userDetailModal').modal({'show': true});
    }
}

function closeUserEvent(e) {
    if (e.status == 'success') {
        $('#userDetailModal').modal('hide');
    }
}

