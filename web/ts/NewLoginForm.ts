var newLoginForm: NewLoginForm;

/**
 * NewLoginForm encapsulates all of the code for the form for logging in
 */
class NewLoginForm {
    /**
     * To initialize the object, we say what method of NewEntryForm should be
     * run in response to each of the form's buttons being clicked.
     */
    constructor() {
        //$("#loginButton").click(this.logIn);
        $("#loginButton").click(this.testAlert);
    }
    testAlert(){
	window.alert("Hello there");
	$("#loginContainer").hide();
	$("#mainBoard").show();
    }

    // Sends user and password to server
    // NEED TO IMPLEMENT
    logIn(){
	 $.ajax({
            type: "POST",
            url: "/login",		// NEED TO COORDINATE W BACKEND
            dataType: "json",

		// NEED TO ADD A USERID/USERTOKEN TO MAKE THIS WORK

            data: JSON.stringify({ mUsername: $("#uname").val(), mPassword: $("#psw").val() }),
            success: this.onLoginResponse,
	    error: window.alert("Something went wrong...")
        });
    }

    /**
     * onLoginResponse runs when the AJAX call in logIn() returns a
     * result.
     *
     * @param data The object returned by the server
     */
    private onLoginResponse(data: any) {
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
    }
} // end class NewLoginForm

