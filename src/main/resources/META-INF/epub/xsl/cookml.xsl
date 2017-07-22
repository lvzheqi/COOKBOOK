<?xml version="1.0" ?>
<xsl:stylesheet version="1.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:epub="http://www.idpf.org/2007/ops"
                xmlns:pn="xalan://de.hs.inform.lyuz.cookbook.utils.XalanHelper"
                extension-element-prefixes="pn">
    <xsl:output
        method="xml"
        version="1.0"
        encoding="utf-8"
        indent="yes"
        standalone="no"
        omit-xml-declaration="no"
        media-type="application/xml+xhtml"
    />

    <xsl:template match="cookml">
        <html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops" xml:lang="en" lang="en">
            <head>
                <meta charset="utf-8" />
                <title>
                    <xsl:value-of select="pn:getCategory()"/>
                </title>
                <link rel="stylesheet" type="text/css" href="css/epub-spec.css"/>
            </head>

            <body>
                <div class="recipe">
                    <h1>
                        <xsl:value-of select="pn:getCategory()"/>
                    </h1>
                    <xsl:value-of select="pn:setRecipeNum(1)" />
                    <xsl:apply-templates select="recipe"/>
                </div>
            </body>
            
        </html>
    </xsl:template>


    <xsl:template match="recipe">
        <xsl:apply-templates select="head"/>
        <span epub:type="pagebreak">
            <xsl:attribute name="id">
                <xsl:value-of select="pn:getId()"/>
            </xsl:attribute>
            <xsl:attribute name="title">
                <xsl:value-of select="pn:getTitle()"/>
            </xsl:attribute>
        </span>            
        
        <div>
            <table class="table">
                <xsl:apply-templates select="part"/>
            </table>
        </div>
        
        <xsl:apply-templates select="preparation"/>
        
        <div>
            <xsl:if test="pn:isHasRemark()=1">
            
                <xsl:if test="pn:isHasTime()=1">
                    <xsl:if test="head/@timeprepqty or head/@timecookqty or head/@timeallqty">
                        <div>
                            <span class="box">
                                <b>
                                    <xsl:text>Zeit: </xsl:text>
                                </b>
                                <xsl:value-of select="pn:getTime(head/@timecookqty,head/@timeprepqty,head/@timeallqty)"/> 
                            </span>
                        </div>
                    </xsl:if>
                </xsl:if>
            
                <xsl:if test="head/content">
                    <div>
                        <span class="box">
                            <b>
                                <xsl:text>Gesamt:  </xsl:text>
                            </b>
                            <xsl:apply-templates select="head/content"/>
                            <xsl:value-of select="pn:getContent()"/>
                        </span>
                    </div>
                </xsl:if>
                <xsl:if test="remark and remark!='' "> 
                    <xsl:apply-templates select="remark"/>
                </xsl:if>
            </xsl:if>
        
            <xsl:if test="pn:isHasSource()=1">
                <xsl:apply-templates select="head/sourceline"/>
            </xsl:if>
        </div>
    </xsl:template>


    <xsl:template match="part">
        <tr class="tr">
            <th colspan="3">
                <xsl:value-of select="@title"/>
            </th>
        </tr>
        <xsl:apply-templates select="ingredient"/>
    </xsl:template>


    <xsl:template match="head">
        <xsl:if test="pn:getRecipeNum()=1">
            <h2>
                <xsl:attribute name="id">
                    <xsl:value-of select="pn:getRefTitle(@title)"/>
                </xsl:attribute>
                <xsl:value-of select="@title"/>
            </h2>
        </xsl:if>
        <xsl:if test="pn:getRecipeNum()&gt;1">
            <h2 class="break">
                <xsl:attribute name="id">
                    <xsl:value-of select="pn:getRefTitle(@title)"/>
                </xsl:attribute>
                <xsl:value-of select="@title"/>
            </h2>
        </xsl:if>
        <xsl:value-of select="pn:setRecipeNum(pn:getRecipeNum()+1)" />

        <div class="headBox">
            <div class="servingBox">
                <xsl:value-of select="@servingqty"/>
                <xsl:text> </xsl:text>
                <xsl:value-of select="@servingtype"/>
                <xsl:value-of select="pn:setServQty(@servingqty)"/>
            </div>
            <div class="qualityBox">
                <xsl:if test="@quality">
                    <xsl:variable name="count" select="5" />
                    <xsl:variable name="quality" select="@quality" />
                    <xsl:for-each select="(//*)[position()&lt;=$count]" >
                        <xsl:if test="$quality &gt;= position()">
                            <div class="icon">
                                <img src="icons/star.png" width="20px" height="20px"/>
                            </div>
                        </xsl:if>
                        <xsl:if test="$quality &lt; position()">
                            <div class="icon">
                                <img src="icons/star_board.png"  width="20px" height="20px"/>
                            </div>
                        </xsl:if>
                    </xsl:for-each>
                </xsl:if>
            </div>
        </div>
        
        <xsl:if test="pn:isHasPic()=1">
            <xsl:apply-templates select="picture"/>
        </xsl:if>
    </xsl:template>


    <xsl:template match="preparation">
        <!--<xsl:value-of select="."/>-->
        <div class="preparation">
            <xsl:apply-templates select="step"/>
            <xsl:apply-templates select="text"/>
            <br/>
        </div>
    </xsl:template>
    
    <xsl:template match="step">
        <div class="step">
            <xsl:value-of select="text"/>
        </div>
    </xsl:template>
    
    <xsl:template match="text">
        <pre class="text">
            <xsl:value-of select="."/>
        </pre>
    </xsl:template>
    
    <xsl:template match="remark">        
        <div>
            <span lass="box">
                <b>
                    <xsl:text>Bemerkung:  </xsl:text>
                </b>
                <xsl:value-of select="."/>
            </span>
        </div>
    </xsl:template>

    <xsl:template match="head/content">
        <xsl:value-of select="pn:setContent(@type,@value)" />
    </xsl:template>

    <xsl:template match="head/sourceline">
        <div>
            <span class="box">
                <b>
                    <xsl:text>Quelle:  </xsl:text>
                </b>
                <xsl:value-of select="."/>
            </span>
        </div>
    </xsl:template>

    <xsl:template match="ingredient">
        <tr class="tr">
            <td class="td">
                <xsl:if test="@qty">
                    <xsl:value-of select="pn:setQty(@qty)"/>
                </xsl:if>
            </td>
            <td class="td">
                <xsl:value-of select="@unit"/>
            </td>
            <td class="td">
                <xsl:value-of select="@item"/>
            </td>
        </tr>

        <xsl:if test="inote">
            <tr class="tr">
                <td colspan="3">
                    <span class="inote">
                        <xsl:apply-templates select="inote"/>
                    </span>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <xsl:template match="inote">
        <xsl:value-of select="."/>
    </xsl:template>

    <xsl:template match="picture">
        <div class="image">
            <img width="50%" >
                <xsl:attribute name="src">
                    <xsl:value-of select="@file"/>
                </xsl:attribute>
            </img>
        </div>
    </xsl:template>
    
   
</xsl:stylesheet>
