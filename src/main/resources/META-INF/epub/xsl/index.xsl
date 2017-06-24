<?xml version="1.0" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:epub="http://www.idpf.org/2007/ops"
                xmlns:pn="xalan://de.hs.inform.lyuz.cookbook.utils.XalanHelper"
                extension-element-prefixes="pn">
    <xsl:output
        method="xml"
        version="1.0"
        encoding="utf-8"
        indent="yes"
        omit-xml-declaration="no"
        media-type="application/xml+xhtml"/>

    

    <xsl:template match="index">
        <html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops" xml:lang="en" lang="en">
            <head>
                <title>Index</title>
                <link rel="stylesheet" type="text/css" href="css/epub-spec.css"/>
            </head>

            <body>
                <h1>
                    <span epub:type="pagebreak">
                        <xsl:attribute name="id">
                            <xsl:value-of select="pn:getId()"/>
                        </xsl:attribute>
                        <xsl:attribute name="title">
                            <xsl:value-of select="pn:getTitle()"/>
                        </xsl:attribute>
                    </span>
                    Index
                </h1>
                <section epub:type="index-group" id="indexA">
                    <xsl:apply-templates select="igroup"/>
                </section>                
            </body>
        </html>
    </xsl:template>


    <xsl:template match="igroup">
        <h3 class="groupletter">
            <xsl:value-of select="@value"/>
        </h3>
        <xsl:apply-templates select="ilevel1"/>
    </xsl:template>


    <xsl:template match="ilevel1">
        <ul class="indexlevel1">
            <li epub:type="index-entry" class="indexhead1">
                <span epub:type="index-term">
                    <xsl:value-of select="@value"/>
                </span>
                <xsl:apply-templates select="ilevel2"/>               
            </li>
        </ul>       
    </xsl:template>
    
    <xsl:template match="ilevel2">
        <ul class="indexlevel2">
            <li epub:type="index-entry" class="indexhead2">
                <span epub:type="index-term">
                    <xsl:value-of select="."/>
                </span>,
                <span epub:type="index-locator-list">
                    <a>
                        <xsl:attribute name="href">
                            <xsl:value-of select="@href"/>
                        </xsl:attribute>
                        <xsl:value-of select="@page"/>
                    </a> 
                </span>
            </li>
        </ul>       
    </xsl:template>
 
</xsl:stylesheet>
