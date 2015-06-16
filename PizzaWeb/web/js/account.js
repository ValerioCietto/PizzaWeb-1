/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function Account(){}

Account.checkLogin = function() {
  var form = $('form[name="login_form"]');
  var user = $('form[name="login_form"] :input[name="username"]');
  var pass = $('form[name="login_form"] :input[name="password"]');
  
  if(user == "" ||  !Re.checkUsername(user.val())) {
    alert("Inserire un Username Valido");
    user.focus();
    return false;
  }
  
  if(pass == "" ||  !Re.checkPassword(pass.val())) {
    alert("Inserire una Password Valida");
    pass.focus();
    return false;
  }

  form.submit();
};

Account.checkJoin = function() {
  
};