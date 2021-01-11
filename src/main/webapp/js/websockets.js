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
        alertify.success('Successfully connected.', 2);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/client/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/client/online-players', function (players) {
            showOnlinePlayers(JSON.parse(players.body));
        });
        stompClient.subscribe('/user/invite', function (message) {
            confirmInvitation(JSON.parse(message.body));
        });
    });
}

function confirmInvitation(message) {
    alertify.confirm("User " + message.username + " wants to play with you!",
        function(){
            alertify.success('Accept');
        },
        function(){
            alertify.error('Decline');
        }).set({title:"New game initiated"}).set({'labels': {ok:'Accept', cancel:'Decline'}});
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
    alertify.success('Successfully disconnected.', 2);
    $("#onlineTable").html("");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function inviteToGame(username) {
    let value = {'username': username};
    stompClient.send("/app/invite", {}, JSON.stringify(value));
}

function showOnlinePlayers(message) {
    let onlineTable = $("#onlineTable");
    onlineTable.html("<tr><th class=\"status-tab\"></th><th class=\"user-tab\">User</th></tr>");
    for (let i = 0; i < message.length; i++) {
        if (message[i].username.localeCompare($("#loggedUser").html()) === 0) continue;
        onlineTable.append(
            "<tr><td class='status-tab'><span class=\"indicator " + (message[i].inGame === true ? 'in-game' : 'online') + " \"/></td>" +
            "<td class='user-tab'>" + message[i].username + "</td>" +
            "<td class='button-play-tab'><button class='btn btn-primary btn-play' onclick=\"inviteToGame('" + message[i].username + "')\">Play</button></td>" +
            "</tr>");
    }
}

$(function () {
    $(".ws-form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() {
        connect($(this).attr("data-csrf"));
    });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});
