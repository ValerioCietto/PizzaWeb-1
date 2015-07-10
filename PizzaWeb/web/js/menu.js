/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function Menu() {
}


Menu.sendCatalogoRequest = function () {
    $.ajax({
        type: "POST",
        url: "/PizzaWeb/Servlet",
        data: {"view": "catalogo"},
        dataType: "html",
        success: function (risposta) {
            $("#body_article").html(risposta);
            $("#page").html("catalogo");
            $("#message").load("view/notify.jsp");

        },
        error: function (jqXHR, textStatus, errorThrown) {
            location.reload();
        }
    });

};

Menu.sendPrenotazioniRequest = function () {
    $.ajax({
        type: "POST",
        url: "/PizzaWeb/Servlet",
        data: {"view": "prenotazioni"},
        dataType: "html",
        success: function (risposta) {
            $("#body_article").html(risposta);
            $("#page").html("prenotazioni");
            $("#message").load("view/notify.jsp");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            location.reload();
        }
    });
};

Menu.sendViewUtentiRequest = function () {
    $.ajax({
        type: "POST",
        url: "/PizzaWeb/Servlet",
        data: {"view": "utenti"},
        dataType: "html",
        success: function (risposta) {
            $("#body_article").html(risposta);
            $("#page").html("utenti");
            $("#message").load("view/notify.jsp");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            location.reload();
        }
    });
};


Menu.addEmpatize = function (event, elem) {
    event.stopPropagation();
    $(elem).addClass("empatize_button");
};

Menu.removeEmpatize = function (event, elem) {
    event.stopPropagation();
    $(elem).removeClass("empatize_button");
};