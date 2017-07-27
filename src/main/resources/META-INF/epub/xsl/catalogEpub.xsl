<?xml version="1.0" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:pn="xalan://de.hs.inform.lyuz.cookbook.utils.XalanHelper"
                extension-element-prefixes="pn">
    <xsl:output
        method="xml"
        version="1.0"
        encoding="utf-8"
        indent="yes"
        omit-xml-declaration="no"
        media-type="application/xml+xhtml"/>
    <!--doctype-system="about:legacy-compat"-->


    <xsl:template match="catalog" >
        <html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops" xml:lang="en" lang="en">
            <head>
                <title>
                    <xsl:apply-templates select="title"/>
                </title>
                <link rel="stylesheet" type="text/css" href="css/epub-spec.css" />
            </head>

            <body>
        
                <h1>
                    <xsl:apply-templates select="title"/>
                </h1>
                <nav epub:type="toc" id="toc" class="toc">
                    <h2>Inhaltsverzeichnis</h2>
                    <ol>
                        <xsl:apply-templates select="link"/>
                    </ol>
                </nav>
            </body>
        </html>
    </xsl:template>


    <xsl:template match="link">
        <li>
            <a>
                <xsl:attribute name="href">
                    <xsl:value-of select="@href" />
                </xsl:attribute>
                <xsl:value-of select="pn:getCategory(@value)" />
            </a>
            <ol>
                <xsl:apply-templates select="sublink"/>
            </ol>
        </li>
    </xsl:template>


    <xsl:template match="sublink">
        <li>
            <a>
                <xsl:attribute name="href">
                    <xsl:value-of select="@href" />
                </xsl:attribute>
                <xsl:value-of select="@value" />
            </a>
        </li>
    </xsl:template>



    <xsl:template match="title">
        <xsl:value-of select="."/>
    </xsl:template>

</xsl:stylesheet>