var $: any;

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
