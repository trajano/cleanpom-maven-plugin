<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0">
  <xsl:output method="xml" indent="yes" encoding="UTF-8" xalan:indent-amount="2"/>
  <xsl:strip-space elements="*"/>
  <!-- default copy -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
  <xsl:param name="dtdname"/>
  <xsl:param name="doctype-public"/>
  <xsl:param name="doctype-system"/>
  <xsl:variable name="nl" select="'&#10;'"/>
  <xsl:template match="/">
    <xsl:if test="$dtdname">
      <xsl:value-of select="$nl" disable-output-escaping="no"/>
      <xsl:text disable-output-escaping="yes">&lt;!DOCTYPE </xsl:text>
      <xsl:value-of select="$dtdname"/>
      <xsl:if test="$doctype-public">
        <xsl:text> PUBLIC "</xsl:text>
        <xsl:value-of select="$doctype-public"/>
        <xsl:text>" "</xsl:text>
        <xsl:value-of select="$doctype-system"/>
        <xsl:text>"</xsl:text>
      </xsl:if>
      <xsl:text disable-output-escaping="yes">&gt;</xsl:text>
    </xsl:if>
    <xsl:value-of select="$nl" disable-output-escaping="no"/>
    <xsl:for-each select="processing-instruction()">
      <xsl:copy-of select="."/>
      <xsl:value-of select="$nl" disable-output-escaping="no"/>
    </xsl:for-each>
    <xsl:for-each select="*/preceding-sibling::comment()">
      <xsl:comment>
        <xsl:value-of select="."/>
      </xsl:comment>
      <xsl:value-of select="$nl" disable-output-escaping="no"/>
    </xsl:for-each>
    <xsl:apply-templates select="*"/>
    <xsl:value-of select="$nl" disable-output-escaping="no"/>
    <xsl:for-each select="*/following-sibling::comment()">
      <xsl:comment>
        <xsl:value-of select="."/>
      </xsl:comment>
      <xsl:value-of select="$nl" disable-output-escaping="no"/>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
