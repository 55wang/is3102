
$(document).ready(function () {
    $("#messages").scrollTop($("#messages")[0].scrollHeight);
});

function handleMessage(message) {
    console.log(message);
    var m = new Message(message);
    m.init();
}

function scrollDown() {
    $("#messages").scrollTop($("#messages")[0].scrollHeight);
}

function Message(message) {
    console.log("Creating new message object");
    this.label = message.label;
    this.createDate = message.createDate;
    this.senderName = message.senderName;
    this.message = message.message;
    this.view = $("<div class='message float-left'>"
                    +   "<div class='left-profile-pic'>"
                    +       "<div class='user-icon' style='background-color:" + $("#receiver-color").val() + ";'>"
                    +           "<div class='icon-text'>"
                    +               this.label
                    +           "</div>"
                    +       "</div>"
                    +   "</div>"
                    +   "<div class='panel panel-quote panel-quote-flush no-margin'>"
                    +   "<div class='panel-body'>"
                    +           "<div class='message-text'>"
                    +               "<p>" + this.message + "</p>"
                    +           "</div>"
                    +   "</div>"
                    +   "</div>"
                    +   "<div class='time-container'>"
                    +       "<span>"
                    +           this.createDate
                    +       "</span>"
                    +   "</div>"
                +"</div>");
}
Message.prototype.init = function() {
    console.log("Prepending view");
    $("#messages").append(this.view);
}