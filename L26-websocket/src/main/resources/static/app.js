let stompClient = null;

const setConnected = (connected) => {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#chatLine").show();
    } else {
        $("#chatLine").hide();
    }
    $("#message").html("");
}

const connect = () => {
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    stompClient.connect({}, (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/response.' + $("#roomId").val(), (message) => showMessage(JSON.parse(message.body).messageStr));
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

const sendMsg = () => stompClient.send("/app/message." + $("#roomId").val(), {}, JSON.stringify({'messageStr': $("#message").val()}))

const showMessage = (message) => $("#chatLine").append("<tr><td>" + message + "</td></tr>")

$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    $("#connect").click(connect);
    $("#disconnect").click(disconnect);
    $("#send").click(sendMsg);
});
