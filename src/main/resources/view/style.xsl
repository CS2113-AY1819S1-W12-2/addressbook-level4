<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" omit-xml-declaration="yes" indent="no"/>
    <xsl:template match="/">
        name,phone,email,address,note,tagged
        <xsl:for-each select="//persons">
            <xsl:value-of select="concat('&quot;',name,'&quot;',',','&quot;',phone,'&quot;',',','&quot;',email,'&quot;',',','&quot;',address,'&quot;',',','&quot;',note,'&quot;',',','&quot;',tagged,'&quot;','&#xA;')"/>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
