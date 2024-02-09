# JDNP3

# Development
To compile everything using JDK 21.

1. `cd jdnp3` (the subdirectory in the project, not the root directory of the project)
2. `mvn install`

This will generate an outstation executable based on the version:

```
jdnp3/jdnp3-ui-web-outstation/target/jdnp3-ui-web-outstation-<version>-bin.tar.gz

For example
jdnp3/jdnp3-ui-web-outstation/target/jdnp3-ui-web-outstation-0.9.8-bin.tar.gz
```

To run this, extract the file and run:
```
java -cp ".:jdnp3-ui-web-outstation-0.9.8.jar" net.sf.jdnp3.ui.web.outstation.main.App
```