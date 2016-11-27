 **For using MongoDB in local machine**
 1. Install MongoDB
 2. In terminal window go to folder \MongoDB\Server\3.2\bin
 3. Launch the server Mongo with command "mongod"
 4. In another terminal window go to the same folder \MongoDB\Server\3.2\bin
 5. Launch the MongoDB shell with command "mongo"
 6. You can use command "help" to see available commands
 7. Create user in the admin db
        use admin
        db.createUser(
        {
            user: "root",
            pwd: "root",
            roles: [{role: "root", db: "admin"},{role: "userAdminAnyDatabase", db: "admin"}]
        }
        )
 8. Create or switch to the db gamification
        use gamification
 9. Create user in the gamification db
        db.createUser(
        {
        user: "root",
        pwd: "root",
        roles: [{role: "dbAdmin", db: "gamification"}]
        }
        )
        
 ** For building and launching application**
 1. In the terminal window go to the project folder,
    use command "gradle clean build". "BUILD SUCCESSFUL" means that
    build is successful :)
 2. You can find the built jar-file in gamification/build/libs,
    go to that folder
 3. Use command "java -jar gamification-1.jar"
 4. The string "Started Gamification" means the successful start. 
    Default port – 8080.
 5. Open one of the helper programs to create and test custom HTTP
    requests-responses, e.g. "Advanced REST client"
 6. Choose the tab "Request", content-type - application/json.
 7. Follow the API. Important: the first - to add something,
    then - you can get it.
    
   ** API**

| url         | /achieve                              |
|-------------|---------------------------------------|
| method      | post                                  |
| ------------|---------------------------------------|
| body        | {                                     |
|             |   "userFromId": "sasha",              |
|             |   "userToId": "ira",                  |
|             |   "pointCount": 2,                    |
|             |   "description": "good work"          |
|             | }                                     |
|-------------|---------------------------------------|
| description | to add an achievement and receive the |
|             | response with the achievement id      |