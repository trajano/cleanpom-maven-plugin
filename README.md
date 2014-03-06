Clean POM Maven Plugin
======================

This will clean the Maven project descriptor, `pom.xml` file, based on an
XSLT file.  It is organized based on the rules specified in the following
URLs:

* http://maven.apache.org/developers/conventions/code.html
* http://maven.apache.org/ref/3.2.1/maven-model/maven.html

Along with a few common sense rules like

* `groupId` should be before `artifactId`.

* `dependencies`, `plugins`, `extensions` should also be ordered by
   `scope`, `groupId`, and `artifactId`.

The `clean` goal will be bound to the `verify` phase of the project and
will replace the `pom.xml` file.  The typical usage is:

    <plugin>
        <groupId>net.trajano.mojo</groupId>
        <artifactId>cleanpom-maven-plugin</artifactId>
        <executions>
            <execution>
                <goals>
                    <goal>clean</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
