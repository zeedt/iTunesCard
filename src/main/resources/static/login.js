/**
 * Created by longbridge on 11/13/17.
 */
function  submit(){
    $("#validateUserId").click();
}
function  adminsubmit(){
    $("#validateAdminUserId").click();
}

function validateUser(form){
    var valid = false;
    if(form.username.value.length>0 && form.password.value.length>0){
        valid=true;
    }
    if(valid) {
        $.ajax({
            type: "POST",
            url: "/user/validateUser",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({username: form.username.value, password: form.password.value}),
            success: function (result) {
                if (result.message == "User found") {
                    redirectToDashBoard();
                } else {
                    $("#message").html(result.message);
                }
            },
            error: function (error) {
            }
        })
    }
}
function validateAdminUser(form){
    var valid = false;
    if(form.username.value.length>0 && form.password.value.length>0){
        valid=true;
    }
    if(valid){
        $.ajax({
            type: "POST",
            url: "/user/validateAdminUser",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({username: form.username.value, password: form.password.value}),
            success: function (result) {
                if (result.message == "User found") {
                    redirectToAdminDashBoard();
                } else {
                    $("#message").html(result.message);
                }
            },
            error: function (error) {
            }
        })
    }
}
function  signup(){
    $("#signUpUserId").click();
}
function  adminSignup(){
    $("#signUpAdminUserId").click();
}

function signUpUser(form){
    var valid = true;
    if(form.username.value.length<3){
        valid=false;$("#userM").html("Username cannot be less than 4 characters");
    }else{
        $("#userM").html("");
    }
    if(form.password.value.length<8){
        valid=false;$("#passM").html("Username cannot be less than 8 characters");
    }else{
        $("#passM").html("");
    }
    if(form.bank.value.length<1){
        valid=false;$("#bankM").html("You must select a bank");
    }else{
        $("#bankM").html("");
    }
    if(form.gender.value.length<1){
        valid=false;$("#genM").html("You must select a gender");
    }else{
        $("#genM").html("");
    }
    if(form.firstname.value.length<4){
        valid=false;$("#firstM").html("First name cannot be less than 4 characters");
    }else{
        $("#firstM").html("");
    }
    if(form.lastname.value.length<4){
        valid=false;$("#lastM").html("Last name cannot be less than 4 characters");
    }else{
        $("#lastM").html("");
    }
    if(form.accountNumber.value.length!=10){
        valid=false;$("#accountM").html("Invalid account number");
    }else{
        $("#accountM").html("");
    }
    if(form.email.value.length<10 || form.email.value.indexOf("@")<0){
        valid=false;$("#emailM").html("Invalid email address");
    }else{
        $("#emailM").html("");
    }
    if(valid){
        $.ajax({
            type: "POST",
            url: "/user/registerUser",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({
                username: form.username.value,
                password: form.password.value,
                bank: form.bank.value,
                gender: form.gender.value,
                firstName: form.firstname.value,
                lastName: form.lastname.value,
                accountNumber: form.accountNumber.value,
                email: form.email.value
            }),
            async: false,
            success: function (result) {
                if (result.message == "Registration successfull") {
                    document.getElementById("signupform").reset();
                }
                $("#message").html(result.message);
            },
            error: function (error) {
            }
        })
    }
}
function signAdminUpUser(form){
    $.ajax({
        type: "POST",
        url:"/user/registerAdminUser",
        contentType: "application/json; charset=utf-8",
        data : JSON.stringify({username:form.username.value,password:form.password.value,gender:form.gender.value,
            firstName:form.firstname.value,lastName:form.lastname.value}),
        async:false,
        success : function (result) {
            if (result.message=="Registration successfull"){
                document.getElementById("signupform").reset();
            }
            $("#message").html(result.message);
        },
        error : function (error) {
        }
    })
}

function redirectToDashBoard(){
    window.location.href = "/dashboard";
}
function redirectToAdminDashBoard(){
    window.location.href = "/admindashboard?page=0";
}




