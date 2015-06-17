function Catalogo() {
}

Catalogo.modificaPizza = function (elem) {
  var div = elem.parentNode.parentNode.parentNode;
  var prezzo = $(div).find("input[name='prezzo']");
  var ingredienti = $(div).find("input[name='ingredienti']");
  var pizza = $(div).find("input[name='pizza']").val();
  if (!Re.checkText(ingredienti.val())) {
    alert("Ingredienti non validi");
    ingredienti.focus();
    return false;
  }
  if (!Re.checkText(pizza)) {
    alert("Nome della pizza non valido");
    return false;
  }
  if (!Re.checkDouble(prezzo.val()) || prezzo.val() < 0) {
    alert("Prezzo non valido");
    prezzo.focus();
    return false;
  }

  $.ajax({
    type: "POST",
    url: "/PizzaWeb/Servlet",
    data: {"ajaxAction": "modPizza", "pizza": pizza, "ingredienti": ingredienti.val(), "prezzo": prezzo.val()},
    dataType: "html",
    success: function (risposta) {
      if (risposta != "null") {
        $(div).find("div.element").html(risposta);
        prezzo.val("");
        ingredienti.val("");
      }
      $("#message").load("js/notify.jsp");

    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("Error");
    }
  });

};



Catalogo.aggiungiPizza = function (elem) {
  var form = $(elem.parentNode);
  var nome = form.find("input[name='nome']");
  var prezzo = form.find("input[name='prezzo']");
  var ingredienti = form.find("input[name='ingredienti']");
  if (!Re.checkText(ingredienti.val())) {
    alert("Ingredienti non validi");
    ingredienti.focus();
    return false;
  }
  if (!Re.checkText(nome.val())) {
    alert("Nome della pizza non valido");
    nome.focus();
    return false;
  }
  if (!Re.checkDouble(prezzo.val()) || prezzo.val() < 0) {
    alert("Prezzo non valido");
    prezzo.focus();
    return false;
  }

  $.ajax({
    type: "POST",
    url: "/PizzaWeb/Servlet",
    data: {"ajaxAction": "addPizza", "pizza": nome.val(), "ingredienti": ingredienti.val(), "prezzo": prezzo.val()},
    dataType: "html",
    success: function (risposta) {
      if (risposta != "null") {
        $("div#body_article").html($("div#body_article").html() + risposta);
        prezzo.val("");
        ingredienti.val("");
      }
      $("#message").load("js/notify.jsp");

    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("Error");
    }
  });

};


Catalogo.rimuoviPizza = function (elem) {
  var div = elem.parentNode.parentNode.parentNode;
  var pizza = $(div).find("input[name='pizza']").val();

  if (!Re.checkText(pizza)) {
    alert("Nome della pizza non valido");
    return false;
  }
  $.ajax({
    type: "POST",
    url: "/PizzaWeb/Servlet",
    data: {"ajaxAction": "remPizza", "pizza": pizza},
    dataType: "html",
    success: function (risposta) {
      if (risposta != "null") {
        $(div).html(risposta);
      }
      $("#message").load("js/notify.jsp");

    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("Error");
    }
  });

};