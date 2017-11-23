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
        cache : false,
        success : function (result) {
            if (result.status=="success"){
                document.getElementById("uploadFormId").reset();
                $("#uploadSMessage").html(result.message);
            }else{
                $("#uploadEMessage").html(result.message);
            }
        },
        error : function (error) {
            console.log("Error");
        }
    })
}