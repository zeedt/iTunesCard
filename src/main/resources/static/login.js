/**
 * Created by longbridge on 11/13/17.
 */
function  submit(){
    $("#validateUserId").click();
}

function validateUser(form){
    $.ajax({
        type: "POST",
        url:"/user/validateUser",
        contentType: "application/json; charset=utf-8",
        data : JSON.stringify({username:form.username.value,password:form.password.value}),
        success : function (result) {
            console.log("Result "+JSON.stringify(result));
            if(result.message=="User found"){
                redirectToDashBoard();
            }else{
                $("#message").html(result.message);
            }
        },
        error : function (error) {
            console.log("Error");
        }
    })
}
function  signup(){
    $("#signUpUserId").click();
}

function signUpUser(form){
    $.ajax({
        type: "POST",
        url:"/user/registerUser",
        contentType: "application/json; charset=utf-8",
        data : JSON.stringify({username:form.username.value,password:form.password.value,bank:bank,
            firstName:form.firstname.value,lastName:form.lastname.value,acctountNumber:form.accountnumber.value}),
        async:false,
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

function redirectToDashBoard(){
    window.location.href = "/dashboard";
}