/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function Account() {
}

Account.checkLogin = function () {
  var form = $('form[name="login_form"]');
  var user = $('form[name="login_form"] :input[name="username"]');
  var pass = $('form[name="login_form"] :input[name="password"]');

  if (user.val() == "" || !Re.checkUsername(user.val())) {
    alert("Inserire un Username Valido");
    user.focus();
    return false;
  }

  if (pass.val() == "" || !Re.checkPassword(pass.val())) {
    alert("Inserire una Password Valida. Nota: La password deve contenere 6 caratteri di cui almeno un numero, una maiuscola e una minuscola");
    pass.focus();
    return false;
  }

  form.submit();
};

Account.checkJoin = function () {
   var form = $('form[name="join_form"]');
   var user = $('form[name="join_form"] :input[name="username"]');
   var pass = $('form[name="join_form"] :input[name="password1"]');
   var cpass = $('form[name="join_form"] :input[name="password2"]');
   
   if (user.val() == "" || !Re.checkUsername(user.val())) {
    alert("Inserire un Username Valido");
    user.focus();
    return false;
  }
   
  if (pass.val() == "" || !Re.checkPassword(pass.val())) {
    alert("Inserire una Password Valida. Nota: La password deve contenere 6 caratteri di cui almeno un numero, una maiuscola e una minuscola");
    pass.focus();
    return false;
  }
  
  if(pass.val() != cpass.val()) {
    alert("Le due password devono coincidere");
    cpass.focus();
    return false;
  }
  
  form.submit();
   
   
};


Account.updateForm = function (checkView) {
  if (checkView == "Login") {
    $("#Login").show();
    $("#Join").hide();
  }
  else {
    $("#Join").show();
    $("#Login").hide();
  }
}