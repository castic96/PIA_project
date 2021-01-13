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
        stompClient.subscribe('/user/game/invite', function (message) {
            confirmInvitation(JSON.parse(message.body));
        });
        stompClient.subscribe('/user/game/accept', function (message) {
            gameAccepted(JSON.parse(message.body));
        });
        stompClient.subscribe('/user/game/decline', function (message) {
            gameDeclined(JSON.parse(message.body))
        });
        stompClient.subscribe('/user/game/state', function (message) {
            gameState(JSON.parse(message.body));
        });
        stompClient.subscribe('/user/game/state/my-turn', function (message) {
            gameStateTurn(JSON.parse(message.body), true);
        });
        stompClient.subscribe('/user/game/state/opp-turn', function (message) {
            gameStateTurn(JSON.parse(message.body), false);
        });
        stompClient.subscribe('/user/game/win', function (message) {
            gameWin(JSON.parse(message.body));
        });
        stompClient.subscribe('/user/game/lose', function (message) {
            gameLose(JSON.parse(message.body));
        });
    });
}

function gameWin(message) {
    $("#gameLabel").html("You win!");

    gameEnd(message);
}

function gameLose(message) {
    $("#gameLabel").html("You lose!");

    gameEnd(message);
}

function gameEnd(message) {
    let gameBoard = $("table#gameBoard");
    gameBoard.removeClass("disabled-in-game");
    gameBoard.removeClass("enabled");
    gameBoard.addClass("disabled");

    gameState(message);
}

function gameMove(id) {
    if (!$("table#gameBoard").hasClass("enabled")) {
        return;
    }

    if(!$.trim($("#" + id).html()).length) {
        let move = {'position' : id};
        stompClient.send("/app/game/move", {}, JSON.stringify(move));
        //TODO: pak disablovat herní plohchu
    }
}

function gameStateTurn(message, myTurn) {
    let gameBoard = $("table#gameBoard");
    gameBoard.removeClass("disabled");

    if (myTurn) {
        $("#gameLabel").html("It is your turn!");
        gameBoard.removeClass("disabled-in-game");
        gameBoard.addClass("enabled");
    }
    else {
        $("#gameLabel").html("Waiting for opponent...");
        gameBoard.removeClass("enabled");
        gameBoard.addClass("disabled-in-game");
    }

    gameState(message);
}
function gameState(message) {
    let board = message.gameBoard;

    for (let i = 0; i < board.length; i++) {
        for (let j = 0; j < board[i].length; j++) {
            let char = '';

            if (board[i][j] === 1) {
                char = 'O';
            }

            if (board[i][j] === 2) {
                char = 'X';
            }

            $("#" + i + "-" +  j).html(char);
        }
    }

}

function gameAccepted(message) {
    $("#gameBoard").attr('hidden', false);
    alertify.success('User ' + message.username + ' accepted game invitation.', 3);
    alertify.success('New game started!', 3);

}

function gameDeclined(message) {
    $("#btn-play-hide").attr('disabled', false);
    $(".btn-play").attr('disabled', false);
    alertify.warning('User ' + message.username + ' declined game invitation.', 3);
    //TODO: nejaky dalsi veci
}

function gameAcceptation(username, accepted) {
    let value = {
        'username': username,
        'accepted': accepted
    };

    stompClient.send("/app/game/acceptation", {}, JSON.stringify(value));
}

function confirmInvitation(message) {
    let username = message.username;

    alertify.confirm("User " + username + " wants to play with you!",
        function(){
            $("#gameBoard").attr('hidden', false);
            gameAcceptation(username,true);
            createGame(username);
            alertify.success('New game started!', 3);
        },
        function(){
            gameAcceptation(username,false);
            alertify.error('Decline');
        }).set({title:"New game initiated"}).set({'labels': {ok:'Accept', cancel:'Decline'}}).autoCancel(10);
}

function createGame(username) {
    let value = {'username': username};

    stompClient.send("/app/game/create", {}, JSON.stringify(value));
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
    $("#btn-play-hide").attr('disabled', true);
    $(".btn-play").attr('disabled', true);

    let value = {'username': username};
    stompClient.send("/app/game/invite", {}, JSON.stringify(value));
}

function showOnlinePlayers(message) {
    let onlineTable = $("#onlineTable");
    onlineTable.html("<tr><th class=\"status-tab\"></th><th class=\"user-tab\">User</th></tr>");
    for (let i = 0; i < message.length; i++) {
        if (message[i].username.localeCompare($("#loggedUser").html()) === 0) continue;
        onlineTable.append(
            "<tr><td class='status-tab'><span class=\"indicator " + (message[i].inGame === true ? 'in-game' : 'online') + " \"/></td>" +
            "<td class='user-tab'>" + message[i].username + "</td>" +
            "<td class='button-play-tab'><button " + ($("#btn-play-hide").prop('disabled') === true ? 'disabled' : 'enabled') + " class='btn btn-primary btn-play' onclick=\"inviteToGame('" + message[i].username + "')\">Play</button></td>" +
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
