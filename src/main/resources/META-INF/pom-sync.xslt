<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://maven.apache.org/POM/4.0.0" xmlns:m="http://maven.apache.org/POM/4.0.0" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" exclude-result-prefixes="m" extension-element-prefixes="xalan">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" xalan:indent-amount="4"/>
    <xsl:strip-space elements="*"/>
    <xsl:variable name="maven-checkstyle-plugin.version">2.12.1</xsl:variable>
    <xsl:variable name="maven-jar-plugin.version">2.4</xsl:variable>
    <xsl:variable name="maven-javadoc-plugin.version">2.9.1</xsl:variable>
    <xsl:variable name="maven-jxr-plugin.version">2.4</xsl:variable>
    <xsl:variable name="maven-pmd-plugin.version">3.1</xsl:variable>
    <!-- default copy -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="/">
        <xsl:text/>
        <xsl:if test="comment()">
            <xsl:apply-templates select="comment()"/>
            <xsl:text/>
        </xsl:if>
        <xsl:apply-templates select="m:project"/>
    </xsl:template>
    <!-- Add default properties -->
    <xsl:template match="m:project">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:if test="not(m:properties)">
                <properties>
                    <jdk.version>1.6</jdk.version>
                    <surefire.version>2.17</surefire.version>
                </properties>
            </xsl:if>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:project/m:properties">
        <xsl:copy>
            <xsl:if test="not(m:jdk.version)">
                <jdk.version>1.6</jdk.version>
            </xsl:if>
            <xsl:if test="not(m:surefire.version)">
                <surefire.version>2.17</surefire.version>
            </xsl:if>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:distributionManagement/m:site/m:url">
        <xsl:choose>
            <xsl:when test="starts-with(normalize-space(.), 'github:http://site.trajano.net/') ">
                <url>
                    <xsl:value-of select="normalize-space(text())"/>
                    <xsl:text>/</xsl:text>
                </url>
            </xsl:when>
            <xsl:otherwise>
                <url>
                    <xsl:value-of select="text()"/>
                </url>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- Remove coding standards property -->
    <xsl:template match="m:project/m:properties/m:coding-standards.version"/>
    <!-- Sync surefire plugin versions -->
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'maven-surefire-plugin']/m:version">
        <version>${surefire.version}</version>
    </xsl:template>
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'maven-surefire-report-plugin']/m:version">
        <version>${surefire.version}</version>
    </xsl:template>
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'maven-failsafe-plugin']/m:version">
        <version>${surefire.version}</version>
    </xsl:template>
    <!-- Sync Compiler -->
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'maven-compiler-plugin']/m:version">
        <version>3.1</version>
    </xsl:template>
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'maven-compiler-plugin']/m:configuration/m:source">
        <source>${jdk.version}</source>
    </xsl:template>
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'maven-compiler-plugin']/m:configuration/m:target">
        <target>${jdk.version}</target>
    </xsl:template>
    <!-- Sync other plugins in management -->
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'maven-checkstyle-plugin']">
        <plugin>
            <artifactId>maven-checkstyle-plugin</artifactId>
            <version>
                <xsl:value-of select="$maven-checkstyle-plugin.version"/>
            </version>
            <configuration>
                <linkXRef>true</linkXRef>
                <configLocation>config/checkstyle-configuration.xml</configLocation>
                <includes>**/*.java,**/*.properties</includes>
                <excludes>target/**,src/test/**</excludes>
            </configuration>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'maven-jar-plugin']">
        <plugin>
            <artifactId>maven-jar-plugin</artifactId>
            <version>
                <xsl:value-of select="$maven-jar-plugin.version"/>
            </version>
            <executions>
                <execution>
                    <id>attach-jar</id>
                    <phase>package</phase>
                    <goals>
                        <goal>jar</goal>
                        <goal>test-jar</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'maven-javadoc-plugin']">
        <plugin>
            <artifactId>maven-javadoc-plugin</artifactId>
            <version>
                <xsl:value-of select="$maven-javadoc-plugin.version"/>
            </version>
            <executions>
                <execution>
                    <id>attach-javadoc</id>
                    <phase>package</phase>
                    <goals>
                        <goal>jar</goal>
                        <goal>test-jar</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <detectJavaApiLink>true</detectJavaApiLink>
                <detectLinks>false</detectLinks>
                <detectOfflineLinks>false</detectOfflineLinks>
                <quiet>true</quiet>
                <useStandardDocletOptions>true</useStandardDocletOptions>
                <show>private</show>
                <doclet>org.umlgraph.doclet.UmlGraphDoc</doclet>
                <docletArtifact>
                    <groupId>org.umlgraph</groupId>
                    <artifactId>umlgraph</artifactId>
                    <version>5.6.6</version>
                </docletArtifact>
            </configuration>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'maven-jxr-plugin']">
        <plugin>
            <artifactId>maven-jxr-plugin</artifactId>
            <version>
                <xsl:value-of select="$maven-jxr-plugin.version"/>
            </version>
            <configuration>
                <linkJavadoc>true</linkJavadoc>
            </configuration>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'maven-pmd-plugin']">
        <plugin>
            <artifactId>maven-pmd-plugin</artifactId>
            <version>
                <xsl:value-of select="$maven-pmd-plugin.version"/>
            </version>
            <configuration>
                <rulesets>
                    <ruleset>/config/pmd-rules.xml</ruleset>
                </rulesets>
            </configuration>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'maven-resources-plugin']">
        <plugin>
            <artifactId>maven-resources-plugin</artifactId>
            <version>2.6</version>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'maven-source-plugin']">
        <plugin>
            <artifactId>maven-source-plugin</artifactId>
            <version>2.2.1</version>
            <executions>
                <execution>
                    <id>attach-sources</id>
                    <phase>package</phase>
                    <goals>
                        <goal>jar</goal>
                        <goal>test-jar</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'findbugs-maven-plugin']">
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>findbugs-maven-plugin</artifactId>
            <version>2.5.3</version>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'jacoco-maven-plugin']">
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.7.0.201403182114</version>
            <executions>
                <execution>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </xsl:template>
    <!-- Remove cobertura-maven-plugin -->
    <xsl:template match="m:project/m:build/m:pluginManagement/m:plugins/m:plugin[m:artifactId = 'cobertura-maven-plugin']"/>
    <!-- Remove configurations from reporting and plugins -->
    <xsl:template match="m:project/*[name()= 'build' or name()='reporting']/m:plugins/m:plugin[m:artifactId = 'maven-compiler-plugin']">
        <plugin>
            <artifactId>maven-compiler-plugin</artifactId>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/*[name()= 'build' or name()='reporting']/m:plugins/m:plugin[m:artifactId = 'maven-jar-plugin']">
        <plugin>
            <artifactId>maven-jar-plugin</artifactId>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/*[name()= 'build' or name()='reporting']/m:plugins/m:plugin[m:artifactId = 'maven-javadoc-plugin']">
        <plugin>
            <artifactId>maven-javadoc-plugin</artifactId>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/*[name()= 'build' or name()='reporting']/m:plugins/m:plugin[m:artifactId = 'maven-source-plugin']">
        <plugin>
            <artifactId>maven-source-plugin</artifactId>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/*[name()= 'build' or name()='reporting']/m:plugins/m:plugin[m:artifactId = 'jacoco-maven-plugin']">
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/*[name()= 'build' or name()='reporting']/m:plugins/m:plugin[m:artifactId = 'maven-checkstyle-plugin']">
        <plugin>
            <artifactId>maven-checkstyle-plugin</artifactId>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/*[name()= 'build' or name()='reporting']/m:plugins/m:plugin[m:artifactId = 'maven-pmd-plugin']">
        <plugin>
            <artifactId>
                <xsl:value-of select="m:artifactId"/>
            </artifactId>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/*[name()= 'build' or name()='reporting']/m:plugins/m:plugin[m:artifactId = 'maven-jxr-plugin']">
        <plugin>
            <artifactId>
                <xsl:value-of select="m:artifactId"/>
            </artifactId>
        </plugin>
    </xsl:template>
    <xsl:template match="m:project/*[name()= 'build' or name()='reporting']/m:plugins/m:plugin[m:artifactId = 'findbugs-maven-plugin']">
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>findbugs-maven-plugin</artifactId>
        </plugin>
    </xsl:template>
    <!-- Remove maven-project-info-reports-plugin use the one from parent pom -->
    <xsl:template match="m:project/*[name()= 'build' or name()='reporting']/m:plugins/m:plugin[m:artifactId = 'maven-project-info-reports-plugin']"/>
    <!-- Replace cobertura with jacoco -->
    <xsl:template match="m:project/*[name()= 'build' or name()='reporting']/m:plugins/m:plugin[m:artifactId = 'cobertura-maven-plugin']">
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
        </plugin>
    </xsl:template>
    <!-- Sync coding standards -->
    <xsl:template match="*[m:artifactId = 'coding-standards']/m:version">
        <version>1.1.2</version>
    </xsl:template>
    <!-- Sync m2e profile -->
    <xsl:template match="m:profiles/m:profile[m:id = 'm2e']">
        <profile>
            <id>m2e</id>
            <activation>
                <property>
                    <name>m2e.version</name>
                </property>
            </activation>
            <build>
                <plugins>
                    <plugin>
                        <artifactId>maven-checkstyle-plugin</artifactId>
                        <executions>
                            <execution>
                                <id>check</id>
                                <phase>compile</phase>
                                <goals>
                                    <goal>check</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                    <plugin>
                        <artifactId>maven-pmd-plugin</artifactId>
                        <executions>
                            <execution>
                                <id>pmd</id>
                                <phase>compile</phase>
                                <goals>
                                    <goal>check</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                    <plugin>
                        <groupId>net.trajano.mojo</groupId>
                        <artifactId>m2e-codestyle-maven-plugin</artifactId>
                        <version>1.1.0</version>
                        <executions>
                            <execution>
                                <goals>
                                    <goal>configure</goal>
                                </goals>
                                <configuration>
                                    <codeStyleBaseUrl>codestyle</codeStyleBaseUrl>
                                </configuration>
                            </execution>
                        </executions>
                    </plugin>
                    <plugin>
                        <groupId>org.codehaus.mojo</groupId>
                        <artifactId>findbugs-maven-plugin</artifactId>
                        <executions>
                            <execution>
                                <id>findbugs</id>
                                <phase>compile</phase>
                                <goals>
                                    <goal>findbugs</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </xsl:template>
</xsl:stylesheet>
