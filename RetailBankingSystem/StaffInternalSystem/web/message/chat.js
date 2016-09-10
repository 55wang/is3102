
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
//var wsUri = "wss://" + document.location.hostname + ":" + document.location.port + document.location.pathname + "/chat/1";//chat number need to be generated
//var websocket = new WebSocket(wsUri);
//
//var username;
//websocket.onopen = function(evt) { onOpen(evt) };
//websocket.onmessage = function(evt) { onMessage(evt) };
//websocket.onerror = function(evt) { onError(evt) };
//var output = document.getElementById("output");
//
//function join() {
//    username = textField.value;
//    websocket.send(username + " joined");
//}
//
//function send_message() {
//    websocket.send(username + ": " + textField.value);
//}
//
//function onOpen() {
//    writeToScreen("Connected to " + wsUri);
//}
//
//function onMessage(evt) {
//    console.log("onMessage: " + evt.data);
//    if (evt.data.indexOf("joined") != -1) {
//        userField.innerHTML += evt.data.substring(0, evt.data.indexOf(" joined")) + "\n";
//    } else {
//        chatlogField.innerHTML += evt.data + "\n";
//    }
//}
//
//function onError(evt) {
//    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
//}
//
//function writeToScreen(message) {
//    output.innerHTML += message + "<br>";
//}

