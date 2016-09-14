
$(document).ready(function () {

});

function handleAnnouncement(facesmessage) {
    console.log("Receiving message");
    var announcement = new Announcement(facesmessage.summary, facesmessage.detail);
    announcement.init();
}

function showModal(title, content) {
    console.log("Showing modal");
    $("#modal_title").html(title);
    $("#modal-content").html(content);
    PF('notificationModal').show();
}

function Announcement(title, content) {
    console.log("Creating new Announcement object " + title + content);
    var el = this;
    this.title = title;
    this.content = content;
    this.tableCellView = $("<div class='table-cell border-bottom'>" +
                                "<span class='v-center absolute-left'>" + title + "</span>" +
                                "<span class='v-center absolute-right'>" + new Date() + "</span>" +
                            "</div>");
    this.tableCellView.on('click' , function() {
        console.log("Table view onclick");
        el.showModal();
    });
}
Announcement.prototype.init = function() {
    console.log("Prepending view");
    $("#output").prepend(this.tableCellView);
}
Announcement.prototype.showModal = function() {
    console.log("Showing modal");
    $("#modal_title").html(this.title);
    $("#modal-content").html(this.content);
    PF('notificationModal').show();
}

