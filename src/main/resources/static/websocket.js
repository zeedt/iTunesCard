/**
 * Created by longbridge on 12/11/17.
 */
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

function webconnect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/zeed', function (greeting) {
            showGreeting(JSON.parse(greeting.body).message,JSON.parse(greeting.body).cardId,JSON.parse(greeting.body).userRole);
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
function dispatchMessage(message,cardId){
    if($("#cardDet").html()==cardId){
        stompClient.send("/app/sendMessage", {}, JSON.stringify({'message': message,cardId:cardId,userRole:$("#urole").html()}));
    }
}

function showGreeting(message,cardId,role) {
    if($("#cardDet").html()==cardId){
        if(role==$("#urole").html()){
            $("#chatbox").append("<div class='col-md-12 text-right sender' style='float: right'>" + message + "</div>");
        }else{
            $("#chatbox").append("<div class='col-md-12 row receiverr'>" + message + "</div>");
        }
    }
    var objDiv = $("#chatbox");
    var objM = objDiv.get(0).scrollHeight;
    objDiv.animate({scrollTop:objM});
}

$(function () {
    $( "#connect" ).click(function() { webconnect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
});

