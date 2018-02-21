# jChat - TCP/UDP based multi-threaded chat application

![example_screen](https://user-images.githubusercontent.com/19994887/36468997-3d5ebd1a-1718-11e8-9dd8-43757211805a.png)


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
