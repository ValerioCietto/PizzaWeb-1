/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function Re() {
}


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
    return Re.checkNumber(double);
};

Re.checkNumber = function (double) {
    return !isNaN(double) || Number(double) === double;
};

Re.checkData = function (date) {
    return /(0000-00-00T00:00)|((20\d\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][0-9]|2[0-3]):([0-5][0-9]))$/.test(date);
}; 