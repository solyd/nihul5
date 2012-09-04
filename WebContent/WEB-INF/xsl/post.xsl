<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<kml xmlns="http://www.opengis.net/kml/2.2">
			<Document>
				<xsl:for-each select="app306932039/post">
					<Placemark id="{@id}">
						<name><xsl:value-of select="title" /></name>
						<description>
							<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
								<div>Posted By: <xsl:value-of select="username"/></div>
								<div>Creation Date: <xsl:value-of select="creation_date"/></div>
								<div>Content: <xsl:value-of select="content"/></div>
							<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
						</description>
						<Point>
							<coordinates><xsl:value-of select="lng"/>,<xsl:value-of select="lat" /></coordinates>
						</Point>
					</Placemark>
				</xsl:for-each>
			</Document>
		</kml>
	</xsl:template>
</xsl:stylesheet>