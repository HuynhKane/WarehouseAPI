const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8081/warehouse'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/notifications', (notification) => {
        showNotification(JSON.parse(notification.body));
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

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

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendNotification() {
    const notification = {
        title: $("#title").val(),
        description: $("#description").val(),
        type: $("#type").val()
    };
    stompClient.publish({
        destination: "/app/notify",
        body: JSON.stringify(notification)
    });
}

function showNotification(notification) {
    const content = `Title: ${notification.title}, Description: ${notification.description}, Type: ${notification.type}`;
    $("#greetings").append("<tr><td>" + content + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendNotification());
});