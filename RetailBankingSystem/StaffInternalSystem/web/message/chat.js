
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
                +       "<div class='icon-text' style='background-color: " + randColor() +";'>"
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
    $("#old-conversations").prepend(this.view);
    $("#old-conversations").scrollTop(0);
}

function randColor() {
    var colors = ["#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3F51B5", "#CDDC39",
        "#2196F3", "#03A9F4", "#00BCD4", "#009688", "#4CAF50", "#8BC34A",
        "#FFEB3B", "#FFC107", "#FF9800", "#FF5722", "#795548", "#9E9E9E"];
    var index = Math.floor((Math.random() * colors.length));
    return colors[index];
}

    
