<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" version="4.0" encoding="iso-8859-1" indent="yes"/>

    <xsl:variable name="solution" select="/comparison/assemblies/solution"/>

    <xsl:key name="change" match="/comparison/assemblies/solution/change" use="@name"/>
    <xsl:variable name="project" select="/comparison/assemblies/solution/@name"/>
    <xsl:variable name="imgPath">/plugins/coriander-haarlem/server/metrics/img/small</xsl:variable>

    <xsl:template match="/">
        <xsl:apply-templates select="/comparison/metrics"/>
        <!-- <div class="assemblyList">
            <h2>Assemblies</h2>
            <xsl:call-template name="outputAssemblies"/>
        </div>
        -->
        <!--
        <h1>Historical trends for <xsl:value-of select="$project"/></h1>
        -->
        <!--<div class="charts">-->
            <!--<xsl:call-template name="outputCharts"/>-->
        <!--</div>-->
    </xsl:template>

    <xsl:template name="outputCharts">
        <xsl:for-each select="/comparison/charts/chart">
            <div class="chart">
                <img>
                    <xsl:attribute name="src">
                        <xsl:value-of select="@link"/>
                    </xsl:attribute>
                </img>
            </div>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="outputHeader">
        <h1>
            Project dashboard for <xsl:value-of select="$project"/>
        </h1>
    </xsl:template>

    <xsl:template name="outputAssemblies">
        <xsl:for-each select="/comparison/assemblies/assembly[@name!='Solution']">
            <xsl:value-of select="@name"/>
            <xsl:if test="position() != last()">
                <br/>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="outputMetrics" match="/comparison/metrics">
        <xsl:for-each select="metric">
            <xsl:if test="count(rank) > 0">
                <div class="metric">
                    <div class="values">
                        <xsl:attribute name="id">
                            <xsl:value-of select="@name"/>
                        </xsl:attribute>
                        <xsl:call-template name="outputMetricChange"/>
                    </div>
                </div>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="outputMetricRanks" match="metric">
        <xsl:variable name="metric" select="@name"/>
        <table>
            <xsl:for-each select="rank">
                <tr>
                    <td>
                        <xsl:if test="@name = 'red'">
                            <img src="{$imgPath}/bullet-red.png"/>
                        </xsl:if>
                        <xsl:if test="@name = 'orange'">
                            <img src="{$imgPath}/bullet-orange.png"/>
                        </xsl:if>
                        <xsl:if test="@name = 'green'">
                            <img src="{$imgPath}/bullet-green.png"/>
                        </xsl:if>
                        <xsl:if test="@name = 'star'">
                            <img src="{$imgPath}/bullet-star.png"/>
                        </xsl:if>
                    </td>
                    <td>
                        <xsl:value-of select="@description"/>
                    </td>
                </tr>
            </xsl:for-each>
        </table>

    </xsl:template>

    <xsl:template name="outputMetricChange">
        <table class="metric">
            <tr>
                <td style="vertical-align:top;">
                    <xsl:if test="key('change',@name)/@rank = 'red'">
                        <img src="{$imgPath}/red.png"/>
                    </xsl:if>
                    <xsl:if test="key('change',@name)/@rank = 'orange'">
                        <img src="{$imgPath}/orange.png"/>
                    </xsl:if>
                    <xsl:if test="key('change',@name)/@rank = 'green'">
                        <img src="{$imgPath}/green.png"/>
                    </xsl:if>
                    <xsl:if test="key('change',@name)/@rank = 'star'">
                        <img src="{$imgPath}/star.png"/>
                    </xsl:if>
                </td>
                <td>
                    <h4 class="metric-title">
                        <xsl:element name="a">
                            <xsl:attribute name="href">
                                <xsl:value-of select="@link"/>
                            </xsl:attribute>
                            <xsl:value-of select="@name"/>
                        </xsl:element>
                    </h4>
                    <div class="metric-body">
                        <xsl:if test="key('change',@name)/@direction = 'NoChange'">
                            <div class="metric-icon">
                                <img src="{$imgPath}/nochange.png"/>
                            </div>
                            <div class="metric-percent">
                                0%
                            </div>
                        </xsl:if>

                        <xsl:if test="key('change',@name)/@direction = 'Better'">
                            <div class="metric-icon">
                                <img src="{$imgPath}/better.png"/>
                            </div>
                            <div class="metric-percent">
                                <xsl:value-of select="key('change',@name)/@percent"/>%
                            </div>
                        </xsl:if>

                        <xsl:if test="key('change',@name)/@direction = 'Worse'">
                            <div class="metric-icon">
                                <img src="{$imgPath}/worse.png"/>
                            </div>
                            <div class="metric-percent">
                                <xsl:value-of select="key('change',@name)/@percent"/>%
                            </div>
                        </xsl:if>

                        <div class="metric-some-other-number">
                            <xsl:value-of select="key('change',@name)/@after"/>
                        </div>
                    </div>
                    <!--<div style="margin-top:115px">-->
                        <!--<xsl:call-template name="outputMetricRanks"/>-->
                    <!--</div>-->
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
