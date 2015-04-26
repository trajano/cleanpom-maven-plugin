<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xslt"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:output method="xml" indent="yes" encoding="UTF-8"
		xalan:indent-amount="4" />
	<xsl:strip-space elements="*" />

	<!-- default copy -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
	<xsl:template match="/">
		<xsl:text>&#xa;</xsl:text>
		<xsl:if test="comment()">
			<xsl:apply-templates select="comment()" />
			<xsl:text>&#xa;</xsl:text>
		</xsl:if>
		<xsl:apply-templates select="*" />
	</xsl:template>
</xsl:stylesheet>
