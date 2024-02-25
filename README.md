# JDNP3

This project was migrated from https://sourceforge.net/projects/jdnp3/

# Development
To compile everything using JDK 21.

1. `cd jdnp3` (the subdirectory in the project, not the root directory of the project)
2. `mvn install`

This will generate an outstation executable based on the version:

```
jdnp3/jdnp3-ui-web-outstation/target/jdnp3-ui-web-outstation-<version>-bin.tar.gz

For example
jdnp3/jdnp3-ui-web-outstation/target/jdnp3-ui-web-outstation-0.10.0-bin.tar.gz
```

To run this, extract the file and run:
```
java -cp ".:jdnp3-ui-web-outstation-0.10.0.jar" net.sf.jdnp3.ui.web.outstation.main.App
```

Enjoy

### Roadmap
* March 24 - Double Bit Binary - Implemented as of version 0.10.0 but as of yet is untested
