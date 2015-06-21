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

  console.log(list);
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
