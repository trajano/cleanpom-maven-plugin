<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:m="http://maven.apache.org/POM/4.0.0" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" xalan:line-separator="&#10;" xalan:indent-amount="2"/>
    <xsl:strip-space elements="*"/>
    <!-- default copy -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <!-- New line variable -->
    <xsl:variable name="nl" select="'&#10;'"/>
    <!-- Root document structure with prologue and epilogue comments -->
    <xsl:template match="/">
        <xsl:value-of select="$nl" disable-output-escaping="no"/>
        <xsl:for-each select="*/preceding-sibling::comment()">
            <xsl:comment>
                <xsl:value-of select="."/>
            </xsl:comment>
            <xsl:value-of select="$nl" disable-output-escaping="no"/>
        </xsl:for-each>
        <xsl:apply-templates select="m:project"/>
        <xsl:value-of select="$nl" disable-output-escaping="no"/>
        <xsl:for-each select="*/following-sibling::comment()">
            <xsl:comment>
                <xsl:value-of select="."/>
            </xsl:comment>
            <xsl:value-of select="$nl" disable-output-escaping="no"/>
        </xsl:for-each>
    </xsl:template>
    <xsl:template match="m:project">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:modelVersion"/>
            <xsl:apply-templates select="m:parent"/>
            <xsl:apply-templates select="m:groupId"/>
            <xsl:apply-templates select="m:artifactId"/>
            <xsl:apply-templates select="m:version"/>
            <xsl:apply-templates select="m:packaging"/>
            <xsl:apply-templates select="m:name"/>
            <xsl:apply-templates select="m:description"/>
            <xsl:apply-templates select="m:url"/>
            <xsl:apply-templates select="m:inceptionYear"/>
            <xsl:apply-templates select="m:organization"/>
            <xsl:apply-templates select="m:licenses"/>
            <xsl:apply-templates select="m:developers"/>
            <xsl:apply-templates select="m:contributors"/>
            <xsl:apply-templates select="m:mailingLists"/>
            <xsl:apply-templates select="m:prerequisites"/>
            <xsl:apply-templates select="m:modules"/>
            <xsl:apply-templates select="m:scm"/>
            <xsl:apply-templates select="m:issueManagement"/>
            <xsl:apply-templates select="m:ciManagement"/>
            <xsl:apply-templates select="m:distributionManagement"/>
            <xsl:apply-templates select="m:properties"/>
            <xsl:apply-templates select="m:dependencyManagement"/>
            <xsl:apply-templates select="m:dependencies"/>
            <xsl:apply-templates select="m:repositories"/>
            <xsl:apply-templates select="m:pluginRepositories"/>
            <xsl:apply-templates select="m:build"/>
            <xsl:apply-templates select="m:reporting"/>
            <xsl:apply-templates select="m:profiles"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:modelVersion">
        <xsl:copy>
            <xsl:copy-of select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:parent">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:groupId"/>
            <xsl:apply-templates select="m:artifactId"/>
            <xsl:apply-templates select="m:version"/>
            <xsl:apply-templates select="m:relativePath"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:project/m:organization">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:name"/>
            <xsl:apply-templates select="m:url"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:modules">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:module">
                <xsl:sort select="text()"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:scm">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:connection"/>
            <xsl:apply-templates select="m:developerConnection"/>
            <xsl:apply-templates select="m:tag"/>
            <xsl:apply-templates select="m:url"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:issueManagement">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:system"/>
            <xsl:apply-templates select="m:url"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:ciManagement">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:system"/>
            <xsl:apply-templates select="m:url"/>
            <xsl:apply-templates select="m:notifiers"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:distributionManagement">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:repository"/>
            <xsl:apply-templates select="m:snapshotRepository"/>
            <xsl:apply-templates select="m:site"/>
            <xsl:apply-templates select="m:downloadUrl"/>
            <xsl:apply-templates select="m:relocation"/>
            <xsl:apply-templates select="m:status"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:properties">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates>
                <xsl:sort select="local-name()"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:dependencies">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:dependency[m:scope='import']">
                <xsl:sort select="m:groupId"/>
                <xsl:sort select="m:artifactId"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="m:dependency[not(m:scope) or m:scope='compile' ]">
                <xsl:sort select="m:groupId"/>
                <xsl:sort select="m:artifactId"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="m:dependency[m:scope='system']">
                <xsl:sort select="m:groupId"/>
                <xsl:sort select="m:artifactId"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="m:dependency[m:scope='runtime']">
                <xsl:sort select="m:groupId"/>
                <xsl:sort select="m:artifactId"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="m:dependency[m:scope='test']">
                <xsl:sort select="m:groupId"/>
                <xsl:sort select="m:artifactId"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="m:dependency[m:scope='provided']">
                <xsl:sort select="m:groupId"/>
                <xsl:sort select="m:artifactId"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:dependency[m:scope='compile' ]">
        <!-- Remove compile scope element -->
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:groupId"/>
            <xsl:apply-templates select="m:artifactId"/>
            <xsl:apply-templates select="m:version"/>
            <xsl:apply-templates select="m:type"/>
            <xsl:apply-templates select="m:classifier"/>
            <xsl:apply-templates select="m:systemPath"/>
            <xsl:apply-templates select="m:exclusions"/>
            <xsl:apply-templates select="m:optional"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:dependency[not(m:scope='compile') ]">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:groupId"/>
            <xsl:apply-templates select="m:artifactId"/>
            <xsl:apply-templates select="m:version"/>
            <xsl:apply-templates select="m:type"/>
            <xsl:apply-templates select="m:classifier"/>
            <xsl:apply-templates select="m:scope"/>
            <xsl:apply-templates select="m:systemPath"/>
            <xsl:apply-templates select="m:exclusions"/>
            <xsl:apply-templates select="m:optional"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:exclusions">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:exclusion">
                <xsl:sort select="m:groupId"/>
                <xsl:sort select="m:artifactId"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:exclusion">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:groupId"/>
            <xsl:apply-templates select="m:artifactId"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:pluginManagement/m:plugins">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:plugin">
                <xsl:sort select="m:groupId"/>
                <xsl:sort select="m:artifactId"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:plugin">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:groupId"/>
            <xsl:apply-templates select="m:artifactId"/>
            <xsl:apply-templates select="m:version"/>
            <xsl:apply-templates select="m:reportSets"/>
            <xsl:copy-of select="m:extensions"/>
            <xsl:apply-templates select="m:executions"/>
            <xsl:apply-templates select="m:dependencies"/>
            <xsl:apply-templates select="m:goals"/>
            <xsl:apply-templates select="m:inherited"/>
            <xsl:apply-templates select="m:configuration"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:configuration">
        <xsl:choose>
            <xsl:when test="m:lifecycleMappingMetadata">
                <xsl:copy>
                    <xsl:apply-templates select="node()"/>
                </xsl:copy>
            </xsl:when>
            <xsl:when test="*">
                <xsl:copy>
                    <xsl:copy-of select="@*|node()"/>
                </xsl:copy>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="m:build">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:defaultGoal"/>
            <xsl:apply-templates select="m:finalName"/>
            <xsl:apply-templates select="m:sourceDirectory"/>
            <xsl:apply-templates select="m:scriptSourceDirectory"/>
            <xsl:apply-templates select="m:testSourceDirectory"/>
            <xsl:apply-templates select="m:resources"/>
            <xsl:apply-templates select="m:testResources"/>
            <xsl:apply-templates select="m:filters"/>
            <xsl:apply-templates select="m:directory"/>
            <xsl:apply-templates select="m:outputDirectory"/>
            <xsl:apply-templates select="m:testOutputDirectory"/>
            <xsl:apply-templates select="m:pluginManagement"/>
            <xsl:apply-templates select="m:plugins"/>
            <xsl:apply-templates select="m:extensions"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:build/m:extensions">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:extension">
                <xsl:sort select="m:groupId"/>
                <xsl:sort select="m:artifactId"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:build/m:extensions/m:extension">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:groupId"/>
            <xsl:apply-templates select="m:artifactId"/>
            <xsl:apply-templates select="m:version"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:profiles">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:profile">
                <xsl:sort select="m:id"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:profile">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:id"/>
            <xsl:apply-templates select="m:activation"/>
            <xsl:apply-templates select="m:modules"/>
            <xsl:apply-templates select="m:distributionManagement"/>
            <xsl:apply-templates select="m:properties"/>
            <xsl:apply-templates select="m:dependencyManagement"/>
            <xsl:apply-templates select="m:dependencies"/>
            <xsl:apply-templates select="m:repositories"/>
            <xsl:apply-templates select="m:pluginRepositories"/>
            <xsl:apply-templates select="m:build"/>
            <xsl:apply-templates select="m:reporting"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:profile/m:activation">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="m:activeByDefault"/>
            <xsl:apply-templates select="m:jdk"/>
            <xsl:apply-templates select="m:os"/>
            <xsl:apply-templates select="m:property"/>
            <xsl:apply-templates select="m:file"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:artifactId|m:groupId|m:id|m:module|m:name|m:url|m:version|m:versionRange|m:goal">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:value-of select="normalize-space(.)"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="m:lifecycleMappingMetadata/m:pluginExecutionFilter">
        <m:pluginExecutionFilter>
            <xsl:apply-templates select="m:pluginExecutionFilter/m:groupId"/>
            <xsl:apply-templates select="m:pluginExecutionFilter/m:artifactId"/>
            <xsl:apply-templates select="m:pluginExecutionFilter/m:versionRange"/>
            <xsl:apply-templates select="m:pluginExecutionFilter/m:goals"/>
        </m:pluginExecutionFilter>
    </xsl:template>
</xsl:stylesheet>
