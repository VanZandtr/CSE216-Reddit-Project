var newEntryForm: NewEntryForm;

/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
class NewEntryForm {
    /**
     * To initialize the object, we say what method of NewEntryForm should be
     * run in response to each of the form's buttons being clicked.
     */
    constructor() {
        $("#addCancel").click(this.clearForm);
        $("#addButton").click(this.submitForm);
    }

    /**
     * Clear the form's input fields
     */
    clearForm() {
        $("#newMessage").val("");
        $("#newTitle").val("");
    }

    /**
     * Check if the input fields are both valid, and if so, do an AJAX call.
     */
    submitForm() {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        let msg = "" + $("#newMessage").val();
        if (msg === "") {
            window.alert("Error: message is not valid");
            return;
        }
	let tit = "" + $("#newTitle").val();
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
    }

    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a
     * result.
     *
     * @param data The object returned by the server
     */
    private onSubmitResponse(data: any) {
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
    }
} // end class NewEntryForm
