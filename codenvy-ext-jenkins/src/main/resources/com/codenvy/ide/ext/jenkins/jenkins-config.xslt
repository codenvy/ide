<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ CODENVY CONFIDENTIAL
  ~ __________________
  ~
  ~ [2012] - [2013] Codenvy, S.A.
  ~ All Rights Reserved.
  ~
  ~ NOTICE:  All information contained herein is, and remains
  ~ the property of Codenvy S.A. and its suppliers,
  ~ if any.  The intellectual and technical concepts contained
  ~ herein are proprietary to Codenvy S.A.
  ~ and its suppliers and may be covered by U.S. and Foreign Patents,
  ~ patents in process, and are protected by trade secret or copyright law.
  ~ Dissemination of this information or reproduction of this material
  ~ is strictly forbidden unless prior written permission is obtained
  ~ from Codenvy S.A..
  -->

<xs:stylesheet version="2.0" xmlns:xs="http://www.w3.org/1999/XSL/Transform">
    <xs:output method="xml" encoding="UTF-8"/>
    <xs:param name="git-repository"/>
    <xs:param name="mavenName"/>
    <xs:param name="userName"/>
    <xs:param name="userEmail"/>

    <xs:template match="@*|node()">
        <xs:copy>
            <xs:apply-templates select="@*|node()"/>
        </xs:copy>
    </xs:template>

    <xs:template match="org.spearce.jgit.transport.RemoteConfig">
        <xs:copy>
            <xs:for-each select="*">
                <xs:choose>
                    <xs:when test=".='${git-repository}'">
                        <xs:copy>
                            <xs:value-of select="$git-repository"/>
                        </xs:copy>
                    </xs:when>
                    <xs:otherwise>
                        <xs:copy>
                            <xs:apply-templates/>
                        </xs:copy>
                    </xs:otherwise>
                </xs:choose>
            </xs:for-each>
        </xs:copy>
    </xs:template>

    <xs:template match="mavenName">
        <xs:copy>
            <xs:value-of select="$mavenName"/>
        </xs:copy>
    </xs:template>

    <xs:template match="userName">
        <xs:copy>
            <xs:value-of select="$userName"/>
        </xs:copy>
    </xs:template>

    <xs:template match="userEmail">
        <xs:copy>
            <xs:value-of select="$userEmail"/>
        </xs:copy>
    </xs:template>

</xs:stylesheet>