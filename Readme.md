**For using MongoDB in local machine**
 1. Install MongoDB
 2. Set up the MongoDB environment.
    MongoDB requires a data directory to store all data. MongoDB’s 
    default data directory path is \data\db. Before the first usage,
    you should create this folder. For more information visit 
    https://docs.mongodb.com/getting-started/shell/installation/
 3. In terminal window go to folder \MongoDB\Server\3.2\bin
 4. Launch the server Mongo with command _mongod_
 5. In another terminal window go to the same folder \MongoDB\Server\3.2\bin
 6. Launch the MongoDB shell with command _mongo_
 7. You can use command _help_ to see available commands
 8. Create user in the admin db
 
        use admin
        db.createUser(
        {
            user: "root",
            pwd: "root",
            roles: [{role: "root", db: "admin"},
                    {role: "userAdminAnyDatabase", db: "admin"}]
        }
        )
        
 9. Create or switch to the db gamification
 
        use gamification
 10. Create user in the gamification db
 
        db.createUser(
        {
            user: "root",
            pwd: "root",
            roles: [{role: "dbAdmin", db: "gamification"}]
        }
        )

**Using MongoDB for Linux users (local machine install)**

 1. Install MongoDB using your distro package manager. For example, in Fedora: `sudo dnf install mongodb-org`
 2. Start mongodb: `sudo systemctl start mongod`
 3. Check status (should be "active"): `sudo systemctl status mongod`
 4. If you want to start mongodb every time with system: `sudo systemctl enable mongod`
 5. Run mongo shell in console with: `mongo`
 6. Do steps 8-10 from section above
        
**For building and launching application**
 1. In the terminal window go to the project folder,
    use gradle wrapper script to start build process.
    
    On Unix-like (Linux or  MacOS) using the gradlew shell script  - `sh gradlew clean build` 
    
    On Windows using the gradlew.bat batch file  - `gradlew clean build`
    
    Wait for message "BUILD SUCCESSFUL" means that  build is successful :)
 2. You can find the built jar-file in gamification/build/libs,
    go to that folder
 3. Use command `java -jar gamification-1.jar`
 4. The string "Started Gamification" means the successful start. 
    Default port – `8080`.
 5. Open one of the helper programs to create and test custom HTTP
    requests-responses, e.g. "Advanced REST client"
 6. Choose the tab "Request", content-type - application/json.
 7. Follow the API. Important: the first - to add something,
    then - you can get it.

 **API**

[REST APIs](https://github.com/JuniorsJava/gamification/wiki/REST-APIs)