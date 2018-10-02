"use strict";
// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $;
// The 'this' keyword does not behave in JavaScript/TypeScript like it does in
// Java.  Since there is only one NewEntryForm, we will save it to a global, so
// that we can reference it from methods of the NewEntryForm in situations where
// 'this' won't work correctly.
var newEntryForm;
/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
var NewEntryForm = /** @class */ (function () {
    /**
     * To initialize the object, we say what method of NewEntryForm should be
     * run in response to each of the form's buttons being clicked.
     */
    function NewEntryForm() {
        $("#addCancel").click(this.clearForm);
        $("#addButton").click(this.submitForm);
    }
    /**
     * Clear the form's input fields
     */
    NewEntryForm.prototype.clearForm = function () {
        $("#newMessage").val("");
    };
    /**
     * Check if the input fields are both valid, and if so, do an AJAX call.
     */
    NewEntryForm.prototype.submitForm = function () {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        var msg = "" + $("#newMessage").val();
        if (msg === "") {
            window.alert("Error: message is not valid");
            return;
        }
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: "/messages",
            dataType: "json",
            data: JSON.stringify({ mMessage: msg }),
            success: newEntryForm.onSubmitResponse
        });
    };
    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a
     * result.
     *
     * @param data The object returned by the server
     */
    NewEntryForm.prototype.onSubmitResponse = function (data) {
        // If we get an "ok" message, clear the form
        if (data.mStatus === "ok") {
            newEntryForm.clearForm();
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    };
    return NewEntryForm;
}()); // end class NewEntryForm
// a global for the main ElementList of the program.  See newEntryForm for
// explanation
var mainList;
/**
 * ElementList provides a way of seeing all of the data stored on the server.
 */
var ElementList = /** @class */ (function () {
    function ElementList() {
    }
    /**
     * refresh is the public method for updating messageList
     */
    ElementList.prototype.refresh = function () {
        // Issue a GET, and then pass the result to update()
        $.ajax({
            type: "GET",
            url: "/messages",
            dataType: "json",
            success: mainList.update
        });
    };
    /**
     * update is the private method used by refresh() to update messageList
     */
    ElementList.prototype.update = function (data) {
        $("#messageList").html("<table>");
        for (var i = 0; i < data.mData.length; ++i) {
            $("#messageList").append("<tr><td>" +
                mainList.buttons(data.mData[i].mId) +
                "</td><td> <button id=upvote>Up Vote</button>" +
                "</td><td> <button id=dnvote>Down Vote</button></td></tr>");
        }
        $("#messageList").append("</table>");
    };
    /**
     * buttons() doesn't do anything yet
     */
    ElementList.prototype.buttons = function (id) {
        return "";
    };
    return ElementList;
}()); // end class ElementList
// Run some configuration code when the web page loads
$(document).ready(function () {
    // Create the object that controls the "New Entry" form
    newEntryForm = new NewEntryForm();
    // Create the object for the main data list, and populate it with data from
    // the server
    mainList = new ElementList();
    mainList.refresh();
});
