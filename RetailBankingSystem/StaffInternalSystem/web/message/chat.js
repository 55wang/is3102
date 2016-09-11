
$(document).ready(function () {
    init();
});

function init() {
    initViewState();
    initClickListener();
}

function initViewState() {
    // default to add new conversation
    $("#new-conversation").addClass("active");
    $(".conversation").removeClass("active");
    $("#messages-container").hide();
    $("#staff-list-container").show();
}

function initClickLister() {
    $("#new-conversation").on('click', function() {
        
    });
}

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
