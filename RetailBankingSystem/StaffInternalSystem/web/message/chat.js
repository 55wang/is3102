
$(document).ready(function () {
    init();
});

function init() {
}

function redirect(id) {
    window.location.href = window.location.href.replace("chat.xhtml", "message.xhtml?conversationId=" + id);
}

function handleConversation(message) {
    console.log(message);
    
}

function createNewConversation() {
    alert();
}
