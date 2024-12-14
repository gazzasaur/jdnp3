# JDNP3

This project was migrated from https://sourceforge.net/projects/jdnp3/

# Running
To run one of the release versions. Download and extract the archive of your choice.

1. Install JDK 8 or later.
2. Extract the contents of a release archive.
3. Change to the newly extracted directory.
4. Run the outstation with the command below.

Linux:
```
java -cp ".:jdnp3-ui-web-outstation-0.12.3.jar" net.sf.jdnp3.ui.web.outstation.main.App
```

Windows:
```
java -cp ".;jdnp3-ui-web-outstation-0.12.3.jar" net.sf.jdnp3.ui.web.outstation.main.App
```

# Upgrading from 0.10

Do to an issue with spring config, the following lines must be updated.
* https://github.com/gazzasaur/jdnp3/commit/04999f7210c0fd99f6e34823b973216a51da0d8c

Jetty Config has extra features that need to be enabled.
* https://github.com/gazzasaur/jdnp3/commit/365a411d5b160a456292da6b188010a62282ca8e#diff-1d4bb085158d6e9e8e96fd5a87eb2d9b81fac904e1b6b00a512e084d30b12952
* https://github.com/gazzasaur/jdnp3/commit/8bbe493e19697949abf56d64d2c90d353adaac1c#diff-1d4bb085158d6e9e8e96fd5a87eb2d9b81fac904e1b6b00a512e084d30b12952

# Development
To compile everything using JDK 21.

1. `cd jdnp3` (the subdirectory in the project, not the root directory of the project)
2. `mvn -f jdnp3/pom.xml install`

This will generate an outstation executable based on the version:

```
jdnp3/jdnp3-ui-web-outstation/target/jdnp3-ui-web-outstation-<version>-bin.tar.gz

For example
jdnp3/jdnp3-ui-web-outstation/target/jdnp3-ui-web-outstation-0.12.3-bin.tar.gz
```

To run this, extract the file and run:
```
java -cp ".:jdnp3-ui-web-outstation-0.12.3.jar" net.sf.jdnp3.ui.web.outstation.main.App
```

### UI Development
The UI may be run and dynamically updated to speed up development. To do this:

1. Build the main project using `mvn -f jdnp3/pom.xml install`
2. Change tot he outstation directory `cd jdnp3-ui-web-outstation`
3. Start the service using the development configs `mvn exec:java -Dexec.mainClass=net.sf.jdnp3.ui.web.outstation.main.App`
