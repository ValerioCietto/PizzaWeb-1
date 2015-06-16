document.addEventListener("DOMContentLoaded", function () {
  
  var checkForm = function (e) {
    if (this.username.value == "") {
      alert("Error: Username cannot be blank!");
      this.username.focus();
      e.preventDefault();
      return;
    }
    re = /^\w+$/;
    if (!re.test(this.username.value)) {
      alert("Error: Username must contain only letters, numbers and underscores!");
      this.username.focus();
      e.preventDefault();
      return;
    }
    if (this.pwd1.value != "" && this.pwd1.value == this.pwd2.value) {
      if (!checkPassword(this.pwd1.value)) {
        alert("The password you have entered is not valid!");
        this.pwd1.focus();
        e.preventDefault();
        return;
      }
    } else {
      alert("Error: Please check that you've entered and confirmed your password!");
      this.pwd1.focus();
      e.preventDefault();
      return;
    }
    if (Model.checkLogin(this.username.value) == false) {
      alert("Username already in use;");
      this.username.focus();
      e.preventDefault();
      return;
    }
    alert("Username and password are both VALID!");
  };
  var myForm = document.getElementById("myForm");
  myForm.addEventListener("submit", checkForm, true);

  var supports_input_validity = function () {
    var i = document.createElement("input");
    return "setCustomValidity" in i;
  }
  if (supports_input_validity()) {
    var usernameInput = document.getElementById("field_username");
    var usernameRule = "Username must not be blank and contain only letters, numbers and understores.";
    usernameInput.setCustomValidity(usernameRule);

    var pwd1Input = document.getElementById("field_pwd1");
    var pwd1Rule = "Password must contain at least 6 characters, including UPPER/lowercase and numbers.";
    pwd1Input.setCustomValidity(pwd1Rule);

    var pwd2Input = document.getElementById("field_pwd2");
    var pwd2Rule = "Please enter the same Password as above.";

    usernameInput.addEventListener("change", function () {
      usernameInput.setCustomValidity(this.validity.patternMismatch ? usernameRule : "");
    }, false);

    pwd1Input.addEventListener("change", function () {
      this.setCustomValidity(this.validity.patternMismatch ? pwd1Rule : "");
      if (this.checkValidity()) {
        pwd2Input.pattern = this.value;
        pwd2Input.setCustomValidity(pwd2Rule);
      } else {
        pwd2Input.pattern = this.pattern;
        pwd2Input.setCustomValidity("");
      }
    }, false);

    pwd2Input.addEventListener("change", function () {
      this.setCustomValidity(this.validity.patternMismatch ? pwd2Rule : "");
    }, false);

  }
}, false);
