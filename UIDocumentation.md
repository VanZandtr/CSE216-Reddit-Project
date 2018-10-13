# UI Documentation
##### Pages 
* Login Page
	* Username field
	* Password field
	* Logs in user based on username and password
* Message Page
	* Can post messages
	* Add a title and a message for message to be valid
	* Displays on the page as a list of messages
	* Message Layout
		* Title in bold
		* Below title
			* Message directly under title
			* Number of upvotes and downvotes under each emssage
			* Then a list of comments
		* To the right of the title 
			* Username of the message poster
			* Timestamp of when the message was posted
			* Comment button
* User Profile
	* Click on the username of the posted message to get to the user profile page
	* Page format
		* Username at top in bold
		* First and last name
		* Email
		* Date of registration
##### Authentication
	Per every authentication, the user should post their email and password 
    to the /users/login endpoint
    
    A usertoken is sent back determining the success and failure of the
    authenication
    
    Once they are logged in this user token is unique to the user and allows
    them to make the requests associated to their profile through the controller

	