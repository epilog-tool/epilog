<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:mal="http://projectmallard.org/1.0/"
    xmlns:cache="http://projectmallard.org/cache/1.0/"
    xmlns:e="http://projectmallard.org/experimental/"
    xmlns:exsl="http://exslt.org/common"
    xmlns:str="http://exslt.org/strings"
    xmlns:opf="http://www.idpf.org/2007/opf"
    xmlns="http://www.idpf.org/2007/opf"
    exclude-result-prefixes="mal cache e exsl str"
    version="1.0">

<xsl:param name="opf.id"/>
<xsl:param name="opf.data"/>

<xsl:include href="file:///opt/share/yelp-xsl/xslt/mallard/common/mal-link.xsl"/>
<xsl:include href="file:///opt/share/yelp-xsl/xslt/mallard/common/mal-sort.xsl"/>

<xsl:param name="mal.cache" select="/cache:cache"/>

<xsl:template match="cache:cache">
  <xsl:variable name="root" select="mal:page[@id = 'index']"/>

  <package version="2.0" unique-identifier="id">
    <metadata xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:opf="http://www.idpf.org/2007/opf">
      <xsl:for-each select="document($root/@cache:href)/mal:page">
        <dc:title>
          <xsl:choose>
            <xsl:when test="mal:info/mal:title[@type = 'text']">
              <xsl:value-of select="mal:info/mal:title[@type = 'text'][1]"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="mal:title[1]"/>
            </xsl:otherwise>
          </xsl:choose>
        </dc:title>
        <dc:language>
          <xsl:choose>
            <xsl:when test="@xml:lang">
              <xsl:value-of select="@xml:lang"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:text>en</xsl:text>
            </xsl:otherwise>
          </xsl:choose>
        </dc:language>
        <dc:identifier id="id" opf:scheme="uuid">
          <xsl:value-of select="$opf.id"/>
        </dc:identifier>
        <xsl:for-each select="mal:info/mal:credit">
          <xsl:variable name="type" select="concat(' ', @type, ' ')"/>
          <xsl:choose>
            <xsl:when test="contains($type, ' author ')">
              <dc:creator opf:role="aut">
                <xsl:if test="opf:file-as">
                  <xsl:attribute name="opf:file-as" select="normalize-space(opf:file-as)"/>
                </xsl:if>
                <xsl:value-of select="normalize-space(mal:name)"/>
              </dc:creator>
            </xsl:when>
            <xsl:otherwise>
              <dc:contributor>
                <xsl:if test="opf:file-as">
                  <xsl:attribute name="opf:file-as" select="normalize-space(opf:file-as)"/>
                </xsl:if>
                <xsl:attribute name="opf:role">
                  <xsl:choose>
                    <xsl:when test="contains($type, ' editor ')">
                      <xsl:text>edt</xsl:text>
                    </xsl:when>
                    <xsl:when test="contains($type, ' collaborator ')">
                      <xsl:text>clb</xsl:text>
                    </xsl:when>
                    <xsl:when test="contains($type, ' translator ')">
                      <xsl:text>trl</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:text>oth</xsl:text>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:attribute>
                <xsl:value-of select="normalize-space(mal:name)"/>
              </dc:contributor>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:for-each>
      </xsl:for-each>
    </metadata>
 
    <manifest>
      <item id="ncx" href="ncx.ncx" media-type="application/x-dtbncx+xml"/>

      <xsl:for-each select="str:split(normalize-space($opf.data))">
        <item id="data-{.}" href="{.}">
          <xsl:attribute name="media-type">
            <xsl:choose>
              <xsl:when test="substring(., string-length(.) - 3) = '.png'">
                <xsl:text>image/png</xsl:text>
              </xsl:when>
              <xsl:when test="substring(., string-length(.) - 3) = '.css'">
                <xsl:text>text/css</xsl:text>
              </xsl:when>
              <xsl:when test="substring(., string-length(.) - 2) = '.js'">
                <xsl:text>text/javascript</xsl:text>
              </xsl:when>
            </xsl:choose>
          </xsl:attribute>
        </item>
      </xsl:for-each>

      <xsl:variable name="media">
        <xsl:for-each select="mal:page">
          <xsl:for-each select="document(@cache:href)/mal:page">
            <xsl:for-each select="//mal:media | //e:mouseover">
              <item id="media-{translate(@src, '/', '-')}" href="{@src}">
                <xsl:attribute name="media-type">
                  <xsl:choose>
                    <xsl:when test="@mime">
                      <xsl:value-of select="@mime"/>
                    </xsl:when>
                    <xsl:when test="translate(substring(@src, string-length(@src) - 3),
                                    'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')
                                    = '.png'">
                      <xsl:text>image/png</xsl:text>
                    </xsl:when>
                    <xsl:when test="translate(substring(@src, string-length(@src) - 3),
                                    'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')
                                    = '.jpg'">
                      <xsl:text>image/jpeg</xsl:text>
                    </xsl:when>
                    <xsl:when test="translate(substring(@src, string-length(@src) - 4),
                                    'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')
                                    = '.jpeg'">
                      <xsl:text>image/jpeg</xsl:text>
                    </xsl:when>
                    <xsl:when test="translate(substring(@src, string-length(@src) - 3),
                                    'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')
                                    = '.gif'">
                      <xsl:text>image/gif</xsl:text>
                    </xsl:when>
                    <xsl:when test="translate(substring(@src, string-length(@src) - 3),
                                    'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')
                                    = '.ogg'">
                      <xsl:text>audio/ogg</xsl:text>
                    </xsl:when>
                    <xsl:when test="translate(substring(@src, string-length(@src) - 3),
                                    'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')
                                    = '.ogv'">
                      <xsl:text>video/ogg</xsl:text>
                    </xsl:when>
                    <xsl:when test="translate(substring(@src, string-length(@src) - 4),
                                    'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')
                                    = '.webm'">
                      <xsl:text>video/webm</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:text>application/octet-stream</xsl:text>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:attribute>
              </item>
            </xsl:for-each>
          </xsl:for-each>
        </xsl:for-each>
      </xsl:variable>
      <xsl:for-each select="exsl:node-set($media)/*">
        <xsl:sort select="@id"/>
        <xsl:if test="not(@id = preceding-sibling::*/@id)">
          <xsl:copy-of select="."/>
        </xsl:if>
      </xsl:for-each>

      <xsl:for-each select="mal:page">
        <item id="page-{@id}" href="{@id}.xhtml" media-type="application/xhtml+xml"/>
      </xsl:for-each>
    </manifest>
 
    <spine toc="ncx">
      <xsl:variable name="sorted">
        <xsl:call-template name="mal.sort.tsort">
          <xsl:with-param name="node" select="$root"/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:variable name="nodes" select="exsl:node-set($sorted)/mal:link"/>
      <xsl:for-each select="$nodes">
        <itemref idref="page-{@xref}"/>
      </xsl:for-each>
      <xsl:for-each select="mal:page[not(@id = $nodes/@xref)]">
        <itemref idref="page-{@id}" linear="no"/>
      </xsl:for-each>
    </spine>
 
    <guide>
      <!--
          <reference type="loi" title="List Of Illustrations" href="appendix.html#figures" />
      -->
    </guide>
  </package>
</xsl:template>

</xsl:stylesheet>
