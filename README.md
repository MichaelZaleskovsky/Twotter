# Twotter.com
## Simple social networking application to read and write short messages
#
### Description
######Posting
A user should be able to post a 140 character message.

######Wall
A user able to see a list of the messages they've posted, in reverse chronological order.

######Following
A user able to follow another user. Following doesn't have to be reciprocal: Alice can follow Bob without Bob having to follow Alice.

######Timeline
A user able to see a list of the messages posted by all the people they follow, in reverse chronological order.

###Technological stack
Java 8, SpringBoot, Spring Data, H2 embeded database, Maven, Test: JUnit+REST-assured, SWAGGER 2

##Install instruction.
This project is managed by Maven. Clone repository to your hard disk. 
Then build project be **"mvn install"** and run .jar by Java.
Or use your IDE to build and run project.

###Test
Run TwotterApplicationTests.java as JUnit test.
It contains 3 tests: 
* for creating posts, 
* reading own posts, 
* reading posts by those users who user follow.

###API Documentation
After server will start use your browse to open http://localhost:8080/APIDoc/swagger-ui.html
