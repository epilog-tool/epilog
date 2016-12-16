<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:mal="http://projectmallard.org/1.0/"
    xmlns:cache="http://projectmallard.org/cache/1.0/"
    xmlns:e="http://projectmallard.org/experimental/"
    xmlns:exsl="http://exslt.org/common"
    xmlns:str="http://exslt.org/strings"
    xmlns="http://www.daisy.org/z3986/2005/ncx/"
    exclude-result-prefixes="mal cache e exsl str"
    version="1.0">

<xsl:output method="xml"
            doctype-public="-//NISO//DTD ncx 2005-1//EN"
            doctype-system="http://www.daisy.org/z3986/2005/ncx-2005-1.dtd"/>

<xsl:param name="ncx.id"/>

<xsl:include href="file:///opt/share/yelp-xsl/xslt/mallard/common/mal-link.xsl"/>
<xsl:include href="file:///opt/share/yelp-xsl/xslt/mallard/common/mal-sort.xsl"/>

<xsl:param name="mal.cache" select="/cache:cache"/>

<xsl:template match="cache:cache">
  <xsl:variable name="root" select="mal:page[@id = 'index']"/>
  <ncx version="2005-1">
    <xsl:for-each select="document($root/@cache:href)/mal:page">
      <xsl:attribute name="xml:lang">
        <xsl:choose>
          <xsl:when test="@xml:lang">
            <xsl:value-of select="@xml:lang"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>en</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>

      <head>
        <meta name="dtb:uid" content="{$ncx.id}"/>
        <meta name="dtb:depth" content="1"/>
        <meta name="dtb:totalPageCount" content="0"/>
        <meta name="dtb:maxPageNumber" content="0"/>
      </head>
 
      <docTitle>
        <text>
          <xsl:choose>
            <xsl:when test="mal:info/mal:title[@type = 'text']">
              <xsl:value-of select="mal:info/mal:title[@type = 'text'][1]"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="mal:title[1]"/>
            </xsl:otherwise>
          </xsl:choose>
        </text>
      </docTitle>
 
      <xsl:for-each select="mal:info/mal:credit">
        <docAuthor>
          <text>
            <xsl:value-of select="normalize-space(mal:name)"/>
          </text>
        </docAuthor>
      </xsl:for-each>
    </xsl:for-each>
 
    <navMap>
      <xsl:variable name="sorted">
        <xsl:call-template name="mal.sort.tsort">
          <xsl:with-param name="node" select="$root"/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="nodes" select="exsl:node-set($sorted)/mal:link"/>
      <xsl:for-each select="$nodes">
        <xsl:variable name="id" select="@xref"/>
        <xsl:variable name="node" select="$mal.cache/mal:page[@id = $id]"/>
        <navPoint id="page-{$id}" playOrder="{position()}">
          <navLabel>
            <text>
              <xsl:choose>
                <xsl:when test="$node/mal:info/mal:title[@type = 'text']">
                  <xsl:value-of select="$node/mal:info/mal:title[@type = 'text'][1]"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="$node/mal:title[1]"/>
                </xsl:otherwise>
              </xsl:choose>
            </text>
          </navLabel>
          <content src="{$id}.xhtml"/>
        </navPoint>
      </xsl:for-each>
      <xsl:for-each select="mal:page[not(@id = $nodes/@xref)]">
        <navPoint id="page-{@id}" playOrder="{count($nodes) + position()}">
          <navLabel>
            <text>
              <xsl:choose>
                <xsl:when test="mal:info/mal:title[@type = 'text']">
                  <xsl:value-of select="mal:info/mal:title[@type = 'text'][1]"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="mal:title[1]"/>
                </xsl:otherwise>
              </xsl:choose>
            </text>
          </navLabel>
          <content src="{@id}.xhtml"/>
        </navPoint>
      </xsl:for-each>
    </navMap>
  </ncx>
</xsl:template>

</xsl:stylesheet>
