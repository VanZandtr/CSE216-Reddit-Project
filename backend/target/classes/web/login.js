"use strict";
// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $;
// The 'this' keyword does not behave in JavaScript/TypeScript like it does in
// Java.  Since there is only one NewEntryForm, we will save it to a global, so
// that we can reference it from methods of the NewEntryForm in situations where
// 'this' won't work correctly.
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
        window.location.href = "/main.html";
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
            window.location.href = "/main.html";
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
// Run some configuration code when the web page loads
$(document).ready(function () {
    // Create the object that controls the "New Entry" form
    newLoginForm = new NewLoginForm();
});
