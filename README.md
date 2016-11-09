## Encrypted Chat

Chat server and client. Applying different encryption algorithm.

## Coding Preference

* IntelliJ.
* Google Style Guild. [Installing the google styleguide settings in intellij and eclipse] (https://github.com/HPI-Information-Systems/Metanome/wiki/Installing-the-google-styleguide-settings-in-intellij-and-eclipse)

## Architecture

### Server

* Always up and running.
* Receives a message appends the sender username and send it to all connected clients (broadcast).

### Client

* Connects to a server using address and port.
* Send a string message.
* Receives a message, string content, string which client sent that, (time stamp, no later we add that)

### Encryption
