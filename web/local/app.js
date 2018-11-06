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
var newLoginForm;
/**
 * NewLoginForm encapsulates all of the code for the form for logging in
 */
var NewLoginForm = /** @class */ (function () {
    /**
     * To initialize the object, we say what method of NewEntryForm should be
     * run in response to each of the form's buttons being clicked.
     */
    function NewLoginForm() {
        //$("#loginButton").click(this.logIn);
        $("#loginButton").click(this.testAlert);
    }
    NewLoginForm.prototype.testAlert = function () {
        window.alert("Hello there");
        $("#loginContainer").hide();
        $("#mainBoard").show();
    };
    // Sends user and password to server
    // NEED TO IMPLEMENT
    NewLoginForm.prototype.logIn = function () {
        $.ajax({
            type: "POST",
            url: "/login",
            dataType: "json",
            // NEED TO ADD A USERID/USERTOKEN TO MAKE THIS WORK
            data: JSON.stringify({ mUsername: $("#uname").val(), mPassword: $("#psw").val() }),
            success: this.onLoginResponse,
            error: window.alert("Something went wrong...")
        });
    };
    /**
     * onLoginResponse runs when the AJAX call in logIn() returns a
     * result.
     *
     * @param data The object returned by the server
     */
    NewLoginForm.prototype.onLoginResponse = function (data) {
        // If we get an "ok" message, set user token and move to main message board
        if (data.mStatus === "ok") {
            // NewLoginForm.setUserToken();		NEED TO IMPLEMENT
            $("#loginContainer").hide();
            $("#mainBoard").show();
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
    return NewLoginForm;
}()); // end class NewLoginForm
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
        $("#newTitle").val("");
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
        var tit = "" + $("#newTitle").val();
        if (tit === "") {
            window.alert("Error: Title is not valid");
            return;
        }
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: "/messages",
            dataType: "json",
            // NEED TO ADD A USERID/USERTOKEN TO MAKE THIS WORK
            data: JSON.stringify({ mTitle: tit, mMessage: msg }),
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
        var _loop_1 = function (i) {
            contentURL = "/messages/" + data.mData[i].id;
            // Visits each /messages/id page for the content
            $.getJSON(contentURL, function (a) {
                $("#messageList").append('<p class="posts"><tr><td><b>' + data.mData[i].mTitle +
                    '</td></tr></b><tr><td>' +
                    '<button class="link" id="' + data.mData[i].userId + '">' +
                    data.mData[i].userId + '</button><i> ' + a.mData.cDate +
                    '</i></td></tr><tr><td>' + a.mData.mContent +
                    '</td></tr><tr><td><button class="upvote" id="uvote' + data.mData[i].id +
                    '" value="' + data.mData[i].id + '">+</button>' + a.mData.upVotes +
                    ' /<button class="downvote" id="dvote' + data.mData[i].id +
                    '" value="' + data.mData[i].id +
                    '">-</button>' + a.mData.downVotes + '</td></tr></p>');
            });
        };
        var contentURL;
        for (var i = 0; i < data.mData.length; ++i) {
            _loop_1(i);
        }
        $("#messageList").append("</table>");
        // Passes clicks on userid to function
        $("#messageList").on("click", ".link", function (e) {
            mainList.userButton(e.target.id);
        });
        // IMPLEMENT UPVOTE FUNCTION
        $("#messageList").on("click", ".upvote", function (e) {
            window.alert("You clicked upboat for message#" + e.target.value);
        });
        // IMPLEMENT DOWNVOTE FUNCTION
        $("#messageList").on("click", ".downvote", function (e) {
            window.alert("You clicked downboat for message#" + e.target.value);
        });
    };
    ElementList.prototype.userButton = function (id) {
        window.alert("You clicked " + id);
        // IMPLEMENT USER PROFILE SIMILARLY TO messageList
        $("#userprofile").show();
        //$("#mainBoard").hide();
    };
    /**
     * buttons() returns html for upvote/downvote buttons
     */
    ElementList.prototype.buttons = function (id) {
        var uId = "uvote" + id;
        var dId = "dvote" + id;
        return ('<td><button class="upvote" id="' + uId + '" value="' + id + '">+</button>' +
            '</td><td><button class="downvote" id="' + dId + '" value="' + id + '">-</button></td>');
    };
    return ElementList;
}()); // end class ElementList
// Run some configuration code when the web page loads
$(document).ready(function () {
    $("#mainBoard").hide();
    $("#userprofile").hide();
    newLoginForm = new NewLoginForm();
    // Create the object that controls the "New Entry" form
    newEntryForm = new NewEntryForm();
    // Create the object for the main data list, and populate it with data from
    // the server
    mainList = new ElementList();
    mainList.refresh();
});
