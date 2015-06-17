/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function Re () {}


Re.checkUsername = function (str) {
    var re = /^\w+$/;
    return re.test(str);
};

Re.checkPassword = function (str) {
    var re = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}$/;
    return re.test(str);
};


Re.checkText = function (str) {
  var re = /^[\w\s\,]+$/;
   return str === "" || re.test(str);
};

Re.checkDouble = function (double) {
  return !isNaN(double) || Number(double)===double; 
}