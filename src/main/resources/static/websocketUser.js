/**
 * Created by longbridge on 12/17/17.
 */
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
    var cardId = $("#cardDet").html();
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
            setConnected(true);
            stompClient.subscribe('/topic/zeed', function (greeting) {
                showGreeting(JSON.parse(greeting.body).message,JSON.parse(greeting.body).cardId,JSON.parse(greeting.body).userRole);
            });
        },
        function(){
            getCardMessages(cardId,"");
            webconnect();
        }
    );
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
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

function getCardMessages(id,reason){
    $.ajax({
        type:"POST",
        url:"/fetchNotificationPage",
        data : JSON.stringify({cardId:id}),
        contentType : "application/json; charset=utf-8",
        async: false,
        success : function(result){
            $("#followDeclinebody").html(result);
        },
        error:function (err) {
        }
    })
}