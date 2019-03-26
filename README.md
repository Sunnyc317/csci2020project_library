# Free to Read Library
Download the jar file (Library.jar) from https://github.com/Sunnyc317/csci2020project_library

## Contributors: 
- Yiqing Cao 
  - Networking (socket io), file io, multi-threading
  - https://github.com/Sunnyc317
- Yongkun Huang 
  - UI, integration
  - https://github.com/55hyk
- Rilang Chen 
  - Debugging, refactoring, commenting
  - https://github.com/soysauceK

## HOW TO RUN
#### Make sure jave 1.8 is installed
1. download jar
2. in terminal, type `java -cp Library.jar application.Serverlib` and hit enter
3. open another terminal, type `java -cp Library.jar application.Main` and hit enter
4. The server (local host) should be launched and user interface should've poped up
#### Alternative way
1. Use eclipse to run serverlib.java first to start the server (make sure you're using java 1.8)
2. Run Main.java to start the user interface

## User instruction
This application is a free library where user can log in and search for book in the list and read. 
#### Steps: 
1. register with your username and password
2. log in with correct username and password
3. type down the exact book name in the shown book list and click search
4. book content would be shown
5. sign out of your account

## Additional Information
* The program is able to handle multiple clients and keep track of the users that has logged in. 
* Accounts that has logged in could not be used by another user until the account has logged out
* duplicated username is not allowed for a new account
