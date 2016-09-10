
$(document).ready(function () {

});

function handleMessage(message) {
    var chatContent = $(PrimeFaces.escapeClientId('form:public')),
            text = (message.user) ? message.user + ':' + message.text : message.text;

    chatContent.append(text + '<br />');

    //keep scroll
    chatContent.scrollTop(chatContent.height());

    if (message.updateList) {
        updateList();
    }
}
