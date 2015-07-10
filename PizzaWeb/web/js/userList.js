function UserList() {
}

UserList.modUtente = function (button) {
    var parent = $(button.parentNode.parentNode);
    var name = parent.find("input[name='name']");
    var password = parent.find("input[name='password']");
    var rule = parent.find("select[name='permission']");
    if (name.val() !== "" && !Re.checkUsername(name.val())) {
        alert("nome non valido");
        name.focus();
        return false;
    }
    else
        name = name.val();
    if (password.val() != "" && !Re.checkPassword(password.val())) {
        alert("password non valida");
        password.focus();
        return false;
    }
    else
        password = password.val();
    rule = rule.val();
    $.ajax({
        url: "/PizzaWeb/Servlet",
        type: "POST",
        data: {"ajaxAction": "modUtente", "id": parent.find("input[name='id']").val(), "name": name, "password": password, "rule": rule},
        dataType: "html",
        success: function (risposta) {
            if (risposta != "")
                parent.html(risposta);
            $("#message").load("view/notify.jsp");
        }
    });
    return false; // non deve fare il submit
};

UserList.remUtente = function (button) {
    var parent = $(button.parentNode.parentNode);
    $.ajax({
        url: "/PizzaWeb/Servlet",
        type: "POST",
        data: {"ajaxAction": "remUtente", "id": parent.find("input[name='id']").val()},
        dataType: "html",
        success: function (risposta) {
            if (risposta == "")
                parent.html(risposta);
            $("#message").load("view/notify.jsp");
        }

    });
};