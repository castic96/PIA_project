let stompClient = null;
let intervalIDOnline;
let intervalIDFriends;

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
        $("#gameLabel").html("Challenge some opponent...");
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/client/online-players', function (players) {
            showOnlinePlayers(JSON.parse(players.body));
        });
        stompClient.subscribe('/user/client/friends', function (friends) {
            showFriends(JSON.parse(friends.body));
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
        stompClient.subscribe('/user/game/win-give-up', function (message) {
            gameWinGiveUp(JSON.parse(message.body));
        });
        stompClient.subscribe('/user/game/lose-give-up', function (message) {
            gameLoseGiveUp(JSON.parse(message.body));
        });
        stompClient.subscribe('/user/game/win-disconn', function (message) {
            gameWinDisconn(JSON.parse(message.body));
        });
        stompClient.subscribe('/user/friend/ask', function (message) {
            confirmAskToFriends(JSON.parse(message.body));
        });
        stompClient.subscribe('/user/friend/accept', function (message) {
            friendAccepted(JSON.parse(message.body));
        });
        stompClient.subscribe('/user/friend/decline', function (message) {
            friendDeclined(JSON.parse(message.body));
        });
        stompClient.subscribe('/user/friend/removed', function (message) {
            friendRemoved(JSON.parse(message.body));
        });
        stompClient.subscribe('/user/client/chat', function (message) {
            getChatMessage(JSON.parse(message.body));
        });
    });

    intervalIDOnline = setInterval(onlinePlayersRequest, 1000);
    intervalIDFriends = setInterval(friendsRequest, 1000);
}

function friendsRequest() {
    if (stompClient !== null) {
        stompClient.send("/app/client/friends", {}, {});
    }
}

function onlinePlayersRequest() {
    if (stompClient !== null) {
        stompClient.send("/app/client/online-players", {}, {});
    }
}

function confirmAskToFriends(message) {
    let username = message.username;

    alertify.confirm("User " + username + " wants to add you to friend list!",
        function(){
            friendAcceptation(username,true);
            alertify.success('User ' + username + ' added to your friend list!', 3);
        },
        function(){
            friendAcceptation(username,false);
        }).set({title:"Ask to add friend"}).set({'labels': {ok:'Accept', cancel:'Decline'}}).autoCancel(10);
}

function friendAcceptation(username, accepted) {
    let value = {
        'username': username,
        'accepted': accepted
    };

    stompClient.send("/app/friend/acceptation", {}, JSON.stringify(value));
}

function gameWinDisconn(message) {
    $("#gameLabel").html("You win by default!");
    alertify.success("Opponent disconnected.", 3);

    gameEnd(message);
}

function gameWinGiveUp(message) {
    $("#gameLabel").html("You win by default!");
    alertify.success("Opponent gave up the game.", 3);

    gameEnd(message);
}

function gameLoseGiveUp(message) {
    $("#gameLabel").html("You lose by default!");

    gameEnd(message);
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

    $("#btn-play-hide").attr('disabled', false);
    $("#give-up-btn").attr('disabled', true);

    $("#opponent-label").removeClass("enabled");
    $("#opponent-label").addClass("disabled");

    $("#chat-window").attr('hidden', true);
    $("#chat").html("");

    gameState(message);
}

function gameMove(id) {
    let gameBoard = $("table#gameBoard");

    if (!gameBoard.hasClass("enabled")) {
        return;
    }

    if(!$.trim($("#" + id).html()).length) {
        let move = {'position' : id};
        stompClient.send("/app/game/move", {}, JSON.stringify(move));

        gameBoard.removeClass("enabled");
        gameBoard.addClass("disabled-in-game");
    }
}

function gameStateTurn(message, myTurn) {
    let gameBoard = $("table#gameBoard");

    if (gameBoard.hasClass("disabled")) {
        gameBoard.removeClass("disabled");
    }

    if (myTurn) {
        $("#gameLabel").html("It is your turn!");
        gameBoard.removeClass("disabled-in-game");
        gameBoard.addClass("enabled");
    }
    else {
        $("#gameLabel").html("Waiting for opponent...");

        if (gameBoard.hasClass("enabled")) {
            gameBoard.removeClass("enabled");
        }

        if (!gameBoard.hasClass("disabled-in-game")) {
            gameBoard.addClass("disabled-in-game");
        }

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

function friendRemoved(message) {
    alertify.warning('User ' + message.username + ' removed you from friend list!', 3);
}


function friendAccepted(message) {
    $("#btn-add-friend-hide").attr('disabled', false);

    alertify.success('User ' + message.username + ' accepted you as friend!', 3);

}

function friendDeclined(message) {
    $("#btn-add-friend-hide").attr('disabled', false);

    alertify.warning('User ' + message.username + ' declined friend invitation.', 3);
}

function gameAccepted(message) {
    let opponentLabel = $("#opponent-label");

    $("#gameBoard").attr('hidden', false);
    $("#give-up-btn").attr('disabled', false);
    $("#chat-window").attr('hidden', false);

    opponentLabel.removeClass("disabled");
    opponentLabel.addClass("enabled");
    opponentLabel.html("Opponent: " + message.username);

    alertify.success('User ' + message.username + ' accepted game invitation.', 3);
    alertify.success('New game started!', 3);

}

function gameDeclined(message) {
    $("#btn-play-hide").attr('disabled', false);
    alertify.warning('User ' + message.username + ' declined game invitation.', 3);
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
            let opponentLabel = $("#opponent-label");
            $("#gameBoard").attr('hidden', false);
            $("#btn-play-hide").attr('disabled', true);
            $("#chat-window").attr('hidden', false);
            gameAcceptation(username,true);
            createGame(username);
            $("#give-up-btn").attr('disabled', false);

            opponentLabel.removeClass("disabled");
            opponentLabel.addClass("enabled");
            opponentLabel.html("Opponent: " + username);

            alertify.success('New game started!', 3);
        },
        function(){
            gameAcceptation(username,false);
        }).set({title:"New game initiated"}).set({'labels': {ok:'Accept', cancel:'Decline'}}).autoCancel(10);
}

function createGame(username) {
    let value = {'username': username};

    stompClient.send("/app/game/create", {}, JSON.stringify(value));
}

function disconnectVerify() {
    if ($("#btn-play-hide").prop('disabled') === true) {
        alertify.confirm("Do you really want to disconnect and leave the game?",
            function(){
                disconnect();
            },
            function(){}).set({title:"Disconnect"}).set({'labels': {ok:'Yes', cancel:'No'}}).autoCancel(10);
    }
    else {
        disconnect();
    }
}

function disconnect() {
    let gameBoard = $("#gameBoard");

    $("#gameLabel").html("Connect to game...");

    if (gameBoard.hasClass("enabled")) {
        gameBoard.removeClass("enabled");
    }

    if (gameBoard.hasClass("disabled-in-game")) {
        gameBoard.removeClass("disabled-in-game");
    }

    if (!gameBoard.hasClass("disabled")) {
        gameBoard.addClass("disabled");
    }

    $("#btn-play-hide").attr('disabled', false);
    $("#give-up-btn").attr('disabled', true);
    $("#chat-window").attr('hidden', true);
    $("#chat").html("");

    $("#opponent-label").removeClass("enabled");
    $("#opponent-label").addClass("disabled");

    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    clearInterval(intervalIDOnline);
    clearInterval(intervalIDFriends);
    console.log("Disconnected");
    alertify.success('Successfully disconnected.', 2);
    $("#onlineTable").html("");
    $("#friendsTable").html("");
}

function sendMessage() {
    let message = $("#message");

    if (message.val() === "") {
        return;
    }

    let value = {
        'message': message.val(),
        'user' : null
    };

    stompClient.send("/app/client/chat", {}, JSON.stringify(value));

    getChatMessage({'message' : message.val(), 'user' : 'You'});

    message.val("");
}

function getChatMessage(message) {
    let chatSelector = $("#chat");
    let chat = chatSelector.html();

    chatSelector.html("<tr>" +
        "<td>" + message.user + ": " + message.message + "</td>" +
        "</tr>");

    chatSelector.append(chat);
}

function inviteToGame(username) {
    $("#btn-play-hide").attr('disabled', true);
    $(".btn-play").attr('disabled', true);

    let value = {'username': username};
    stompClient.send("/app/game/invite", {}, JSON.stringify(value));
}

function showOnlinePlayers(message) {
    let onlineTable = $("#onlineTable");

    onlineTable.html("");

    for (let i = 0; i < message.length; i++) {
        if (message[i].username.localeCompare($("#loggedUser").html()) === 0) continue;

        let inGame = message[i].inGame;
        let inFriendList = message[i].inFriendList;

        onlineTable.append(
            "<tr><td class='status-tab'><span title=" + (inGame === true ? ('Playing') : ('Online')) + " class=\"indicator " + (inGame === true ? 'in-game' : 'online') + " \"/></td>" +
            "<td class='user-tab'>" + message[i].username + "</td>" +
            "<td class='button-play-tab'><button " + ($("#btn-play-hide").prop('disabled') === true ? 'disabled' : (inGame === true ? 'disabled' : 'enabled')) + " class='btn btn-primary btn-play' onclick=\"inviteToGame('" + message[i].username + "')\">Play</button></td>" +
            "<td class='button-add-friend-tab'><button " + ($("#btn-add-friend-hide").prop('disabled') === true ? 'disabled' : (inFriendList === true ? 'disabled' : 'enabled')) + " class='btn btn-primary btn-add-friend' onclick=\"addFriend('" + message[i].username + "')\">Add friend</button></td>" +
            "</tr>");
    }
}

function showFriends(message) {
    let friendsTable = $("#friendsTable");

    friendsTable.html("");

    for (let i = 0; i < message.length; i++) {

        let username = message[i].username;
        let status = message[i].status;

        friendsTable.append(
            "<tr>" +
            "<td class='status-tab'><span title=" + (status === 0 ? ('Offline') : (status === 1 ? ('Online') : ('Playing'))) + " class=\"indicator " + (status === 0 ? 'offline' : (status === 1 ? 'online' : 'in-game')) + " \"/></td>" +
            "<td class='user-tab'>" + username + "</td>" +
            "<td class='button-play-tab'><button " + ($("#btn-play-hide").prop('disabled') === true ? 'disabled' : (status === 1 ? 'enabled' : 'disabled')) + " class='btn btn-primary btn-play' onclick=\"inviteToGame('" + message[i].username + "')\">Play</button></td>" +
            "<td class='button-remove-friend-tab'><button class='btn btn-primary btn-remove-friend' onclick=\"removeFriend('" + message[i].username + "')\">Remove</button></td>" +
            "</tr>");
    }
}

function removeFriend(username) {
    $(".btn-remove-friend").attr('disabled', true);

    let value = {'username': username};
    stompClient.send("/app/friend/remove", {}, JSON.stringify(value));

    alertify.success("User " + username + " has been removed from friend list.", 3);
}

function addFriend(username) {
    $("#btn-add-friend-hide").attr('disabled', true);
    $(".btn-add-friend").attr('disabled', true);

    let value = {'username': username};
    stompClient.send("/app/friend/add", {}, JSON.stringify(value));
}

function giveUp() {
    alertify.confirm("Do you really want to give up the game?",
        function(){
            stompClient.send("/app/game/give-up", {}, {});
        },
        function(){}).set({title:"Surrender"}).set({'labels': {ok:'Yes', cancel:'No'}}).autoCancel(10);
}

$(function () {
    window.onbeforeunload = function() {
        if ($("#btn-play-hide").prop('disabled') === true) {
            return "Leaving this page will reset the wizard";
        }
    };

    $(".ws-form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() {
        connect($(this).attr("data-csrf"));
    });
    $( "#disconnect" ).click(function() { disconnectVerify(); });
});

