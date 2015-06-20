function Prenotazione() {}

Prenotazione.addPrenotazione = function () {
  
};


Prenotazione.addPizza = function () {
  var div=  $(".slot_add_pizza");
  div.load("view/catalogo/prenotapizza.jsp") ;
  div.removeClass("slot_add_pizza");
  $(".pren_pizza_list").append("<div class='slot_add_pizza'></div>");
};