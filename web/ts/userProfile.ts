var userProfilePage: userProfile;

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
            url: "/users/2",		// NEED TO FIX SO IT DIRECTS TO THE URL WITH THE SAME USERNAME
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
