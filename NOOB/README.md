# Noob Java API
This API allows you to connect to the NOOB-server with a Java-application to keep the websockets alive. It also runs a Spring webserver, so you can send requests to it from your web-API.

## Installation
Installing this API is very simple.

### Using maven
Right now, we only have a `pom.xml` so you can only compile with [maven](https://github.com/apache/maven). Of course, we would love it if you would create the other build files too. To install the API, just run:

```
mvn install
```

This will create a `target` folder. If you decide to clean up, you can just use:

```
mvn clean
```

This will remove the `target` folder again.

## Usage
Using this API is fairly simple. You have to run the API on a server because it has to maintain the connection. To run it, just pass the name of your bank. This should be the same as in your IBAN. For example:

```
java -jar noob-api-0.1.0.jar COBA
```

Sometimes, the connection is not established correctly. We strongly recommend to always test your connection before testing anything else.

## How does this work?
I don't know. Figure it out and write a documentation.

## Contributing
We don't need contributions, because this is a personal project. If you find any bugs, you can submit an issue.

## License
This API is available under the [GNU General Public License v3.0](https://github.com/Bowero/noob-java-api/blob/master/LICENSE). This means that you can do almost anything you want with it, but we are not liable for anything. You do have to include the same license then. Seems fair, right?

Of course, this does not mean that you can simply use this API for your project. If you want to do so, we recommend you to ask your teacher about that. The code sucks however, and that will be your responsibility then.