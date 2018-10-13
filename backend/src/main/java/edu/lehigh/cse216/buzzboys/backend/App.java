package edu.lehigh.cse216.buzzboys.backend;

// Import the Spark package, so that we can make use of the "get" function to 
// create an HTTP GET route
import spark.Spark;

// Import Google's JSON library
import com.google.gson.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.protocol.HTTP;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;

/**
 * For now, our app creates an HTTP server that can only get and add data.
 * REST ENDPOINTS:
 * /users (GET, POST, DELETE) 		-> all users
 * /messages (GET, POST, DELETE)  		-> all messages
 * /users/id (GET, POST, DELETE)		->one user
 * /user/id/messages (GET)			->messages for a given user
 * /messages/id	(GET, PUT, DELETE)		->one message
 * /messages/id/upvote (PUT)			->(action) upvotes a post
 * /messages/id/downvote (PUT)		->(action) downvotes a post
 */
public class App {
    public static void main(String[] args) {

        // Get the port on which to listen for requests
        Spark.port(getIntFromEnv("PORT", 5432));


        //get system env variables to connect to postgres db
        // Map<String, String> env = System.getenv();
        // String ip = env.get("POSTGRES_IP");
        // String port = env.get("POSTGRES_PORT");
        // String user = env.get("POSTGRES_USER");
        // String pass = env.get("POSTGRES_PASS");
        // gson provides us with a way to turn JSON into objects, and objects
        // into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe.  See 
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();

        // dataStore holds all of the data that has been provided via HTTP 
        // requests
        //
        // NB: every time we shut down the server, we will lose all data, and 
        //     every time we start the server, we'll have an empty dataStore,
        //     with IDs starting over from 0.
        final StoreHandler store = new StoreHandler();
        
        /**
         * Set up the location for serving static files
         * Set up the location for serving static files.  If the STATIC_LOCATION
         * environment variable is set, we will serve from it.  Otherwise, serve
         * from "/web"
         */
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }

        //create tables
        // Database db = Database.getDatabase();
        // db.createMessagesTable();
        // db.createUsersTable();
        // db.createVotesTable();
    
        // /messages
        

        // GET route that returns all message titles and Ids.  All we do is get 
        // the data, embed it in a StructuredResponse, turn it into JSON, and 
        // return it.  If there's no data, we return "[]", so there's no need 
        // for error handling.
        Spark.get("/messages", (request, response) -> {
            
            // ensure status 200 OK, with a MIME type of JSON
            MessageLite req = gson.fromJson(request.body(), MessageLite.class);
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, store.msg.readAll()));
        });

         // POST route for adding a new element to the DataStore.  This will read
        // JSON from the body of the request, turn it into a SimpleRequest 
        // object, extract the title and message, insert them, and return the 
        // ID of the newly created row.
        Spark.post("/messages", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            MessageReq req = gson.fromJson(request.body(), MessageReq.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId = store.msg.createEntry(req.mTitle, req.mContent, req.userId);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        // /messages/:id
        // GET route that returns everything for a single row in the DataStore.
        // The ":id" suffix in the first parameter to get() becomes 
        // request.params("id"), so that we can get the requested row ID.  If 
        // ":id" isn't a number, Spark will reply with a status 500 Internal
        // Server Error.  Otherwise, we have an integer, and the only possible 
        // error is that it doesn't correspond to a row with data.
        Spark.get("/messages/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Message data = store.msg.readOne(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

       

        // PUT route for updating a row in the DataStore.  This is almost 
        // exactly the same as POST
        Spark.put("/messages/:id", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            
            int idx = Integer.parseInt(request.params("id"));
            MessageReq req = gson.fromJson(request.body(), MessageReq.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Message result = store.msg.updateMessage(idx, req.mTitle, req.mContent);

            if (result == null) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });

        // DELETE route for removing a row from the DataStore
        Spark.delete("/messages/:id", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the 
            //     message sent on a successful delete
            boolean result = store.msg.deleteOne(idx);
            if (!result) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });

        // GET route that returns all message titles and Ids.  All we do is get 
        // the data, embed it in a StructuredResponse, turn it into JSON, and 
        // return it.  If there's no data, we return "[]", so there's no need 
        // for error handling.
        Spark.get("/users", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            UserLite req = gson.fromJson(request.body(), UserLite.class);
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, store.user.readAll()));
        });

        // POST route for adding a new element to the DataStore.  This will read
        // JSON from the body of the request, turn it into a SimpleRequest 
        // object, extract the title and message, insert them, and return the 
        // ID of the newly created row.
        Spark.post("/users", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            UserReq req = gson.fromJson(request.body(), UserReq.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            byte[] salt = Security.generateSalt();
            byte[] hashedPass = Security.hashPassword(req.password, salt);
            int newId = store.user.createEntry(req.realName, req.userName, req.email, hashedPass, salt);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        // GET route that returns everything for a single row in the DataStore.
        // The ":id" suffix in the first parameter to get() becomes 
        // request.params("id"), so that we can get the requested row ID.  If 
        // ":id" isn't a number, Spark will reply with a status 500 Internal
        // Server Error.  Otherwise, we have an integer, and the only possible 
        // error is that it doesn't correspond to a row with data.
        Spark.get("/users/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            UserLite data = store.user.readOne(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        

        // PUT route for updating a row in the DataStore.  This is almost 
        // exactly the same as POST
        Spark.put("/users/:id", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            
            int idx = Integer.parseInt(request.params("id"));
            UserReq req = gson.fromJson(request.body(), UserReq.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            byte[] salt = Security.generateSalt();
            byte[] hashedPass = Security.hashPassword(req.password, salt);
            UserLite result = store.user.updateOne(idx, req.realName, req.userName, req.email, hashedPass, salt);
            if (result == null) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });

        // DELETE route for removing a row from the DataStore
        Spark.delete("/users/:id", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the 
            //     message sent on a successful delete
            boolean result = store.user.deleteOne(idx);
            if (!result) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });

        Spark.post("/users/login", (request, response) -> {
            
            UserLoginReq req = gson.fromJson(request.body(), UserLoginReq.class);
            String email = req.email;
            User u = new User();
            u.uEmail = email;
            User foundUser = store.user.readOneWithProperties(u);
            byte[] salt = foundUser.uSalt;
            byte[] hashedPass = Security.hashPassword(req.password, salt);
            if (Arrays.equals(foundUser.uPassword, hashedPass))
            {
                response.status(200);
                response.type("application/json");
                SecureRandom random = new SecureRandom();
                byte[] bytes = new byte[20];
                random.nextBytes(bytes);
                String token = bytes.toString();
                Session.Login(foundUser.uUserName, token);
                return gson.toJson(new StructuredResponse("OK", null, token));
            }
            response.status(401);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("Error", "Authentication Failed", null));

        });

        // GET route that returns every message for a userId.
        // The ":id" suffix in the first parameter to get() becomes 
        // request.params("id"), so that we can get the requested row ID.  If 
        // ":id" isn't a number, Spark will reply with a status 500 Internal
        // Server Error.  Otherwise, we have an integer, and the only possible 
        // error is that it doesn't correspond to a row with data.
        Spark.get("/users/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            UserLite data = store.user.readOne(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        /*Spark.get("/users/id/messages"), (request, response) -> {
            query joining tables and backend method must be made
        } figure this one out last*/

        Spark.put("/messages/:id/upvote", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            
            response.type("application/json");
            String token = gson.fromJson(request.body(), DefaultReq.class).userToken;
            String username = Session.getUsername(token);

            //Check to see if the user already voted
            int msgId = Integer.parseInt(request.params("id"));
            Vote existingVote = store.vote.readVoteByMessageAndUsername(msgId, username);
            
            if (existingVote != null) {
                if (existingVote.is_upvote == 1) {
                    //do nothing
                    response.status(304);
                    return gson.toJson(new StructuredResponse("ok", "Not Updated", null));
                }
                else if (existingVote.is_upvote == 0) {
                    store.vote.deleteOne(existingVote.id);
                    store.msg.updateDownvote(msgId, -1);
                }
            }
            int result = store.vote.createEntry(msgId, username, 1);
            Boolean data = store.msg.updateUpvote(idx, 1);
            if (!data) {
                response.status(304);
                return gson.toJson(new StructuredResponse("error", idx + "not found or updated failed", null));
            } else {
                response.status(200);
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });

        Spark.put("/messages/:id/downvote", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            
            response.type("application/json");
            String token = gson.fromJson(request.body(), DefaultReq.class).userToken;
            String username = Session.getUsername(token);

            //Check to see if the user already voted
            int msgId = Integer.parseInt(request.params("id"));
            Vote existingVote = store.vote.readVoteByMessageAndUsername(msgId, username);
            
            if (existingVote != null) {
                if (existingVote.is_upvote == 0) {
                    //do nothing
                    response.status(304);
                    return gson.toJson(new StructuredResponse("ok", "Not Updated", null));
                }
                else if (existingVote.is_upvote == 1) {
                    store.vote.deleteOne(existingVote.id);
                    store.msg.updateUpvote(msgId, -1);
                }
            }
            int result = store.vote.createEntry(msgId, username, 0);
            Boolean data = store.msg.updateDownvote(idx, 1);
            if (!data) {
                response.status(304);
                return gson.toJson(new StructuredResponse("error", idx + "not found or updated failed", null));
            } else {
                response.status(200);
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });
        

        Spark.get("/message/:id/comments", (request, response) -> {
            response.status(200);
            response.type("application/json");
            int msgId = Integer.parseInt(request.params("id"));
            return gson.toJson(new StructuredResponse("ok", null, store.comment.readAllWithMessageId(msgId)));
        });

        Spark.post("/message/:id/comments", (request, response) -> {
            response.type("application/json");
            
            String token = gson.fromJson(request.body(), DefaultReq.class).userToken;
            String username = Session.getUsername(token);
            User user = new User();
            user.uUserName = username;
            user = store.user.readOneWithProperties(user);
            int userId = user.id;
            int msgId = Integer.parseInt(request.params("id"));

            CommentReq req = gson.fromJson(request.body(), CommentReq.class);
            int newId = store.comment.createEntry(userId, msgId, req.content);
            if (newId == -1) {
                response.status(500);
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });
    }

    /**
     * Get an integer environment varible if it exists, and otherwise return the
     * default value.
     * 
     * @envar      The name of the environment variable to get.
     * @defaultVal The integer value to use as the default if envar isn't found
     * 
     * @returns The best answer we could come up with for a value for envar
     */
    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }
}