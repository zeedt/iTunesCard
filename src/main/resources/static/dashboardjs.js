/**
 * Created by longbridge on 11/23/17.
 */
function viewCard(image,cvv,cvv2) {
    $('#myInput').html("<img src='"+image+"' class='col-lg-10' /><input type='hidden' id='cvv' value='"+cvv+"' /><input type='hidden' id='cvv2' value='"+cvv2+"' />")
    $("#viewCardModal").modal('show');
}
function viewCardDecReason(image,comment) {
    $('#decResCardImg').html("<img src='"+image+"' class='col-lg-10' /><p></p>Decline Reason: "+comment);
    $("#viewDecResCardModal").modal('show');
}
function previousDashPage(){
    var pPage = $("#lastpicked").html();
    window.location.href = "/dashboard?page="+(Number(pPage)-1);
}
function nextDashPage() {
    var pPage = $("#lastpicked").html();
    window.location.href = "/dashboard?page="+(Number(pPage)+1);
}
function previousDecPage(){
    var pPage = $("#lastpicked").html();
    window.location.href = "/declined?page="+(Number(pPage)-1);
}
function nextDecPage() {
    var pPage = $("#lastpicked").html();
    window.location.href = "/declined?page="+(Number(pPage)+1);
}
function previousVerPage(){
    var pPage = $("#lastpicked").html();
    window.location.href = "/verified?page="+(Number(pPage)-1);
}
function nextVerPage() {
    var pPage = $("#lastpicked").html();
    window.location.href = "/verified?page="+(Number(pPage)+1);
}
function followCard(id,reason){
    $("#followDeclinereas").html(reason);
    $.ajax({
        type:"POST",
        url:"/fetchNotificationPage",
        data : JSON.stringify({cardId:id}),
        contentType : "application/json; charset=utf-8",
        async: false,
        success : function(result){
            $("#followDeclinebody").html(result);
            $("#followDeclineCardModal").modal('show');
        },
        error:function (err) {
        }
    })
}

function postMessage() {
    $("#sendMessageId").click();
}
function sendMessage(form) {
    var formData = new FormData(form);
    var cardId = $("#cardDet").html();
    var message = $("#usermsg").val();
    if(message!="" && message!=undefined) {
        $.ajax({
            type: "POST",
            url: "/user/postMessage",
            data: JSON.stringify({message: message, cardId: cardId}),
            async: false,
            contentType: "application/json; charset=utf-8",
            success: function (result) {
                dispatchMessage(message, cardId);
                $("#usermsg").val("");
            },
            error: function (error) {
            }
        })
    }
}