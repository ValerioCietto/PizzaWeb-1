function Prenotazione() {
}

Prenotazione.addPrenotazione = function () {
    var list = [];
    var data = $("input[name='data']");
    if (!Re.checkData(data.val())) {
        alert("orario prenotazione non accettabile");
        data.focus();
        return false;
    }

    $(".pren_pizza").each(function (index) {
        pizza = $(this).find("select").val();
        qt = $(this).find("input[name='quantita']").val();
        if (qt > 0)
            list.push(new PrenElem(pizza, qt));
    });

    $.ajax({
        url: "/PizzaWeb/Servlet",
        type: "POST",
        data: {"action": "addPrenotazione", "lista": JSON.stringify(list), "data": data.val()},
        dataType: "html",
        success: function (risposta) {
            Menu.sendPrenotazioniRequest();
        }
    });
};


Prenotazione.addPizza = function () {
    var div = $(".slot_add_pizza");
    div.load("view/catalogo/prenotapizza.jsp");
    div.removeClass("slot_add_pizza");
    $(".pren_pizza_list").append("<div class='slot_add_pizza'></div>");
};


function PrenElem(pizza, quantita) {
    this.pizza = pizza;
    this.quantita = quantita;
}



Prenotazione.modPrenotazioneUser = function (button) {
    var form = $(button.parentNode);
    var parent = form.parent();
    var quantita = form.find("input[name='quantita']");
    var data = form.find("input[name='data']");
    var utente = parent.find("input[name='nome_utente']");
    var pizza = parent.find("input[name='pizza']");
    var ut = "";
    var pz = "";

    if (!Re.checkNumber(quantita.val()))
    {
        alert("deve essere un numero");
        quantita.focus();
        return false;
    }

    if (data.val() != "" && !Re.checkData(data.val()))
    {
        alert("data prenotazione non valida");
        data.focus();
        return false;
    }

    if (utente.length > 0 &&  utente.val() != ""  && !Re.checkUsername(utente.val())) {
        alert("user non accettabile");
        utente.focus();
        return false;

    }
    else
        ut = utente.val();
    
    if (pizza.length > 0 &&  pizza.val() != ""  && !Re.checkText(pizza.val())) {
        alert("nome pizza non accettabile");
        utente.focus();
        return false;

    }
    else
        pz = pizza.val();
      
    $.ajax({
        url: "/PizzaWeb/Servlet",
        type: "POST",
        data: {"ajaxAction": "modPrenotazione", "id": form.find("input[name='id']").val(), "nome_utente": ut, "pizza" : pz , "quantita": quantita.val(), "data": data.val()},
        dataType: "html",
        success: function (risposta) {
            if (risposta != "") parent.html(risposta);
            $("#message").load("view/notify.jsp");
        }

    });
return true;
};


Prenotazione.modStatoPrenotazione = function (button) {
  var parent = $(button.parentNode.parentNode);
  var sel = parent.find("select[name='stato']");
  var opt ;
  if(sel.length > 0)
    opt = sel.val();
  else
    opt = "Consegnato";
      $.ajax({
        url: "/PizzaWeb/Servlet",
        type: "POST",
        data: {"ajaxAction": "modStatoPrenotazione", "id": parent.find("input[name='id']").val() , "stato" : opt },
        dataType: "html",
        success: function (risposta) {
            if (risposta != "") parent.html(risposta);
            $("#message").load("view/notify.jsp");
        }

    });
  
};

Prenotazione.remPrenotazione = function (button) {
  var parent = $(button.parentNode.parentNode);
  var opt ;
  
      $.ajax({
        url: "/PizzaWeb/Servlet",
        type: "POST",
        data: {"ajaxAction": "remPrenotazione", "id": parent.find("input[name='id']").val() },
        dataType: "html",
        success: function (risposta) {
            if (risposta == "") parent.html(risposta);
            $("#message").load("view/notify.jsp");
        }

    });
  
};