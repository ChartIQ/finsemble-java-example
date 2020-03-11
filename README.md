# Java native interface for Finsemble

## Testing in the IDE

Pass the following parameters:
```
finsembleWindowName=FinsembleJavaExample-11-126-Finsemble componentType=FinsembleJar uuid=uuid1545252286933_4444 left=316 top=89 width=800 height=600 iac=true serverAddress=ws://127.0.0.1:3376
```

## Maven

You can build the JAR file and the launch4j executable file using the `mvn package` command.

**NOTE:** The executable generated by launch4j is experimental and may not work correctly.

## Testing application from command line

This can be run with the following command from the project root:
``` BASH
./target/FinsembleJavaExample.exe
```

## Configuration for testing 
Copy the _java-example.json_ included in the project to _src/components/java-example/java-example.json_. Update the application manifest to include:
``` JSON
    "finsemble": {
        "applicationRoot": "http://localhost:3375",
        "moduleRoot": "http://localhost:3375/finsemble",
        "servicesRoot": "http://localhost:3375/finsemble/services",
        "notificationURL": "http://localhost:3375/components/notification/notification.html",
        "javaExampleJarRoot": "<path to finsemble-java-example target (e.g. C:/Users/andy/Documents/SourceCode/finsemble-java-example/target)>",
        "importConfig": [
            "$applicationRoot/configs/application/config.json",
            "$applicationRoot/components/java-example/java-example.json"
        ],
        "IAC": {
            "serverAddress" : "ws://127.0.0.1:3376"
        }
    }
```

## Examples provided
- JavaExample - Example JavaFX application
- JavaSwingExample - Example Java Swing application
- JavaHeadlessExample - Example integration without a user interface
- AuthenticationExample - Example component that performs authentication from within Java

**NOTE:** The _java-example.json_ file includes two components: Java Example (local) and Java Example (asset). The "local" component uses `javaExampleRoot` to specify the path to the JAR file on the local system. The "asset" component uses the `appAsset` (described below) to download and run the application.

## Configuration for asset testing

Copy the _FinsembleJavaExample.zip_ from _target_ to the _hosted_ folder in the seed project. Add the _FinsembleJavaExample.zip_ entry to `appAssets` in _configs/openfin/manifest-local.json_.

``` json
/* ... */
    "appAssets": [
        {
            "src": "https://assets.finsemble.com/assimilation/assimilation_3.10.0.1.zip",
            "version": "3.10.0",
            "alias": "assimilation",
            "target": "AssimilationMain.exe"
        },
        {
            "src": "http://localhost:3375/hosted/FinsembleJavaExample.zip",
            "version": "3.10.0",
            "alias": "finsembleJavaExample",
            "target": "FinsembleJavaExample.exe"
        }
    ],
/* ... */
```


## Enable logging
This project includes an example _logging.properties_ file to enable logging for the project. To use this file, add the following argument to the command line to launch the example (e.g. to the `arguments` property of the component config:
```
-Djava.util.logging.config.file="C:\\Users\\andy\\Documents\\SourceCode\\finsemble-java-example\\logging.properties"
```

So the arguments passed to the Java example will look like:
```
finsembleWindowName=FinsembleJar-11-126-Finsemble componentType=FinsembleJar uuid=uuid1545252286933_4444 left=316 top=89 width=800 height=600 iac=true serverAddress=ws://127.0.0.1:3376 -Djava.util.logging.config.file="C:\\Users\\andy\\Documents\\SourceCode\\finsemble-java-example\\logging.properties"
```

Be sure to update `"java.util.logging.config.file"` to point to the location of the _logging.properties_ file. The provided _logging.properties_ file puts the log in the system temp directory (e.g. _%TEMP%_ on Windows).
