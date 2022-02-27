# bitazza-test
# Persistence
- Room

# Architecture
- MVVM
- Kotlin

# Libraries
- Hilt
- Lifecycle
- Room
- Retrofit 2
- OkHttp for Websocket

# Summary
- Made use of Room Database for persistence. For UI, strictly native android UI widgets are used for clear uniformed code without unnecessary layout designing parameters.
- Made use of constraint layout
- Made use of Hilt for easier to read code by removing boilerplate di classes.

# Instructions
- Make sure to download Java 11 from https://www.oracle.com/java/technologies/downloads/ since the project is compiled to run on the latest SDK
- Navigate to File -> Project Structure -> SDK Location, then make sure that JDK Location is pointed to Java 11
- Build app

# Issues Encountered
- I had issues connecting to some API Endpoints i.e. https://api-doc.bitazza.com/#subscribelevel1, and https://api-doc.bitazza.com/#getaccountpositions, I am guessing this is due to Permission issues. I have tried some of the endpoints to make use of the API Key I created for my account with Trading permission, but I find it conflicting with the requirement to show a Login page that accepts a Username and Password, hence I used that request payload instead of the one with the API Key. I did not have luck on this part.
- Under package data -> remote -> Functions.kt, the endpoints that I have tried to use in order to have access to the endpoints above can be seen, but server is responding with endpoint not found error hence I also did not have luck on this.
- I also tried passing the KYC just in case, but also did not have luck on this.
- Therefore, the app only has the login, logout, instruments list applied.
