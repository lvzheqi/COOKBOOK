<?xml version="1.0" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output
            method="xml"
            version="1.0"
            encoding="utf-8"
            indent="yes"
            omit-xml-declaration="no"
            media-type="application/xml+xhtml"
            doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
            doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"/>

    <xsl:template match="cookml">
        <html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops" xml:lang="en" lang="en">
            <head>
                <title>CookML Transformation in eine XHTML-Anzeige</title>
                <!--<link rel="stylesheet" type="text/css" href="css/epub.css" />-->
            </head>

            <body>
                <p>Diese XML-Datei enthaelt folgende Rezepte:</p>
                <xsl:apply-templates/>
            </body>
        </html>
    </xsl:template>


    <xsl:template match="recipe">
        <p></p>
        <hr></hr>
        <table>
            <tr>
                <td>
                    <h2>
                        <xsl:attribute name="id">
                            <xsl:value-of select="head/@title" />
                        </xsl:attribute>
                        <xsl:apply-templates select="head"/>
                    </h2>
                    <table>
                        <xsl:apply-templates select="part"/>
                    </table>
                </td>
            </tr>
        </table>
        <xsl:apply-templates select="preparation"/>
    </xsl:template>


    <xsl:template match="part">
        <xsl:value-of select="@title"/>
        <br></br>
        <xsl:apply-templates select="ingredient"/>
    </xsl:template>


    <xsl:template match="head">
        <xsl:value-of select="@title"/>
        <br></br>
        <xsl:value-of select="@servingqty"/>
        <xsl:value-of select="@servingtype"/>
        <br></br>
        <xsl:apply-templates select="picture"/>
    </xsl:template>

    <xsl:template match="head/@title">
        <xsl:value-of select="."/>
    </xsl:template>

    <xsl:template match="preparation">
        <xsl:value-of select="."/>
    </xsl:template>


    <xsl:template match="ingredient">
        <tr>
            <td>
                <xsl:value-of select="@qty"/>
            </td>
            <td>
                <xsl:value-of select="@unit"/>
            </td>
            <td>
                <xsl:value-of select="@item"/>
                <xsl:value-of select="@title"/>
            </td>
        </tr>
    </xsl:template>


    <xsl:template match="picture">
        <img>
        <xsl:attribute name="src">
            <xsl:value-of select="@file" />
        </xsl:attribute>
        </img>
    </xsl:template>

</xsl:stylesheet>
