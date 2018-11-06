// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $: any;

// The 'this' keyword does not behave in JavaScript/TypeScript like it does in
// Java.  Since there is only one NewEntryForm, we will save it to a global, so
// that we can reference it from methods of the NewEntryForm in situations where
// 'this' won't work correctly.
var newEntryForm: NewEntryForm;
var newLoginForm: NewLoginForm;
var userProfilePage: userProfile;

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


// a global for the main ElementList of the program.  See newEntryForm for
// explanation
var mainList: ElementList;

/**
 * ElementList provides a way of seeing all of the data stored on the server.
 */
class ElementList {
    /**
     * refresh is the public method for updating messageList
     */
    refresh() {
        // Issue a GET, and then pass the result to update()
        $.ajax({
            type: "GET",
            url: "/messages",
            dataType: "json",
            success: mainList.update
        });
    }

    /**
     * update is the private method used by refresh() to update messageList
     */
    private update(data: any) {
        $("#messageList").html("<table>");
	for (let i = 0; i < data.mData.length; ++i) {
		// Visits each /messages/id page for the message content
		$.getJSON("/messages/"+data.mData[i].id, (a:any)=>{
			// HTML for the message itself and attached buttons
			$("#messageList").append("<p><tr><td><b>" + data.mData[i].mTitle + "</b></td><td><button class='link' value='" + data.mData[i].userId + "'>"+data.mData[i].userId+"</button>" + a.mData.cDate + "<button class='comment' value='"+data.mData[i].id+"'>Comment</button></td></tr><tr><td colspan=2>" + a.mData.mContent + "</td></tr><tr><td><button class='upvote' value='" + data.mData[i].id + "'>+</button>" + a.mData.upVotes + " /<button class='downvote' value='" + data.mData[i].id + "'>-</button>" + a.mData.downVotes + "</td></tr>");

			// HTML for the comments div and attached buttons
			$("#messageList").append("<tr><td><div id='thread"+data.mData[i].id+"'><table>");
			
			// Visits each comment for each message and adds to div. Waiting for endpoints
			// $.getJSON("/messages/comments/"+data.mData[i].id, (a:any)=>{});
			$("#messageList").append("<tr><td colspan=2>LOOK DIS COMMENT</td></tr>");
			$("#messageList").append("<tr><td colspan=2>HAVE ANOTHER</td></tr>");
			$("#messageList").append("</table></div></td></tr></p>");

		});	
		
        }
        $("#messageList").append("</table>");
//
	
	// Passes clicks on userid to function
	$("#messageList").on("click", ".link", function(e:any){
		userProfilePage.userButton(e.target.value);
	});

	// IMPLEMENT UPVOTE FUNCTION
	$("#messageList").on("click", ".upvote", function(e:any){
		window.alert("You clicked upboat for message#"+e.target.value);	
	});
	// IMPLEMENT DOWNVOTE FUNCTION
	$("#messageList").on("click", ".downvote", function(e:any){
		window.alert("You clicked downboat for message#"+e.target.value);	
	});

	// IMPLEMENT COMMENT FUNCTION
	$("#messageList").on("click", ".comment", function(e:any){
		window.alert("You clicked comment for message#"+e.target.value);

	});
    }
    
    /**
     * buttons() returns html for upvote/downvote buttons
     */
    private buttons(id: string): string {
	var uId = "uvote"+id;        
	var dId = "dvote"+id;	
	return ('<td><button class="upvote" id="'+uId+'" value="'+id+'">+</button>'+
		'</td><td><button class="downvote" id="'+dId+'" value="'+id+'">-</button></td>');
    }

} // end class ElementList

/**
 * Controls user profile
 */
class userProfile{
    // CONSTRUCT USER PROFILE PAGE
    userButton(id: string) {
	window.alert("You clicked "+id);
	// IMPLEMENT "userDeets" SIMILARLY TO "messageList"
	$("#userprofile").show();
	$.ajax({
            type: "GET",
            url: "/users/"+id,		// NEED TO FIX SO IT DIRECTS TO THE URL WITH THE SAME USERNAME
            dataType: "json",
            success: userProfilePage.update
        });
	$("#mainBoard").hide();
	$("#returnUP").click(this.returnMainPage);
    }

    private update(data: any){
	$("#userDeets").html("<table><h3>"+data.mData.username+"</h3><tr><td>"+
	data.mData.ufirst+" "+data.mData.ulast+"</td></tr><tr><td>"+data.mData.email+
	"</td></tr><tr><td><i>registered "+data.mData.cDate+"</i></td></tr>");
    }

    returnMainPage(){
	$("#mainBoard").show();
	$("#userprofile").hide();
    }
}

// Run some configuration code when the web page loads
$(document).ready(function () {
    $("#mainBoard").hide();
    $("#userprofile").hide();
    // Create the object that controls the "New Login" form
    newLoginForm = new NewLoginForm();
    // Create the object that controls the "New Entry" form
    newEntryForm = new NewEntryForm();
    // Create the object that controls the user profile page
    userProfilePage = new userProfile();
    // Create the object for the main data list, and populate it with data from
    // the server
    mainList = new ElementList();
    mainList.refresh();
});
