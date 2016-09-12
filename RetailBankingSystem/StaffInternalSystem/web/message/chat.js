
$(document).ready(function () {
    init();
});

function init() {
}

function redirect(id) {
    window.location.href = window.location.href.replace("chat.xhtml", "message.xhtml?conversationId=" + id);
}

function handleMessage(message) {
    console.log(message);
    var c = new Conversation(message);
    c.init();
}

function Conversation(message) {
    console.log("Creating new Conversation object");
    this.label = message.label;
    this.createDate = message.createDate;
    this.senderName = message.senderName;
    this.message = message.message;
    this.conversationId = message.conversationId;
    this.view = $("<div class='conversation' id='"+this.conversationId+"' onclick='redirect(" + this.conversationId + ")'>"
                +   "<div class='user-icon'>"
                +       "<div class='icon-text'>"
                +           this.label
                +       "</div>"
                +   "</div>"
                +   "<div class='conversation-text'>"
                +       "<div class='conversation-username'>"
                +           "<b>" + this.senderName + "</b>"
                +       "</div>"
                +       "<div class='conversation-preview'>"
                +           this.message
                +       "</div>"
                +   "</div>"
                + "</div>");
}
Conversation.prototype.init = function() {
    console.log("Prepending view");
    $("#old-conversations").append(this.view);
    $("#old-conversations").scrollTop(0);
}
