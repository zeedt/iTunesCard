/**
 * Created by longbridge on 11/21/17.
 */
function triggerSubmission() {
    $("#uploadForm").click();
}
function uploadform(form) {
    console.log(JSON.stringify(form));
    var formData = new FormData(form);
    $.ajax({
        type: "POST",
        url:"/user/uploadCard",
        data : formData,
        async:false,
        processData : false,
        contentType : false,
        success : function (result) {
            console.log("Result "+JSON.stringify(result));
            console.log("Message is "+result.message);
            if (result.message=="Registration successfull"){
                document.getElementById("signupform").reset();
            }
            $("#message").html(result.message);
        },
        error : function (error) {
            console.log("Error");
        }
    })
}