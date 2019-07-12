# Twotter.com
## Simple social networking application to read and write short messages
#
### Description
###### Posting
A user is able to post a 140 characters long message.

###### Wall
A user is able to see a list of the messages he have posted in reverse chronological order.

###### Following
A user is able to follow another user. Following is not reciprocal: Alice can follow Bob without Bob having to follow Alice.

###### Timeline
A user able to see a list of the messages posted by all the people he follows, in reverse chronological order.

### Technological stack
Java 8, SpringBoot, Spring Data, H2 embeded database, Maven, Test: JUnit+REST-assured, SWAGGER 2

### Install instruction.
This project is managed by Maven. Clone repository to your hard disk. 
Then build project by **"mvn install"** and run .jar by Java.
Or use your IDE to build and run project.

### Test
Run TwotterApplicationTests.java as JUnit test.
It contains 3 tests: 
* for creating posts, 
* reading own posts, 
* reading posts by users which the user follows.

### API Documentation
After server will start use your browser to open http://localhost:8080/swagger-ui.html
