
$(document).ready(function () {
    init();
});

function init() {
    initViewState();
    initClickListener();
}

function initViewState() {
    // default to add new conversation
    console.log("initViewState()");
    $("#new-conversation").addClass("active");
    $(".conversation").removeClass("active");
    $("#messages-container").hide();
    $("#staff-list-container").show();
}

function initClickListener() {
    console.log("initClickLister()");
    $("#new-conversation").on('click', function() {
        
    });
}

function redirect(id) {
    window.location.href = window.location.href.replace("chat.xhtml", "message.xhtml?conversationId=" + id);
}

function handleConversation(message) {
    console.log("handleChatMessage() with message:" + message);
//    var chatContent = $(PrimeFaces.escapeClientId('form:public')),
//            text = (message.user) ? message.user + ':' + message.text : message.text;
//
//    chatContent.append(text + '<br />');
//
//    //keep scroll
//    chatContent.scrollTop(chatContent.height());
//
//    if (message.updateList) {
//        updateList();
//    }
}

function createNewConversation() {
    alert();
}
