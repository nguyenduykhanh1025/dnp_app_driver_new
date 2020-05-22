var prefix = ctx + "logistic/receiveContFull";

function confirm() {
    parent.finishForm(data);
    $.modal.close();
}

function closeForm() {
    $.modal.close();
}

$("#moveContAmount").html(moveContAmount);
$("#moveContPrice").html(moveContPrice);