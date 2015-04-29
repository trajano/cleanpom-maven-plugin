<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" xalan:line-separator="&#10;" xalan:indent-amount="4"/>
    <xsl:strip-space elements="*"/>
    <xsl:variable name="nl" select="'&#10;'"/>
    <!-- default copy -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="/">
        <xsl:value-of select="$nl" disable-output-escaping="no"/>
        <xsl:for-each select="comment()">
            <xsl:comment>
                <xsl:value-of select="."/>
            </xsl:comment>
            <xsl:value-of select="$nl" disable-output-escaping="no"/>
        </xsl:for-each>
        <xsl:apply-templates select="*"/>
    </xsl:template>
</xsl:stylesheet>
