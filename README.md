# jChat - TCP/UDP based multi-threaded chat application

## Description

Implementing a TCP based chat box application in Java.

## Usage

```
Compile the .java files using javac

1) Start the server
$ java jServerSocket <portNumber>

2) Start one or multiple clients (hostname is localhost by default)
$ java jClientSocket <hostName> <portNumber>
```

## Dependencies

[Java API for JSON Processing](https://javaee.github.io/jsonp/)

### Basic requirements

* [✓] Send ASCII text
* [~] Share file (binary)
* [] Secure encrypted communication
* [✓] Store chat history

### Advanced blocks

* [✓] Private messages
* [~] Authentication, Authorization
* [] Message integrity: Hash verification
