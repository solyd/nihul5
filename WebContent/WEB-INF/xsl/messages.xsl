<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<kml xmlns="http://www.opengis.net/kml/2.2">
			<Document>
				<Style id="globeIcon">
					<IconStyle>
						<Icon>
							<href>http://maps.google.com/mapfiles/kml/pal3/icon23.png</href>
						</Icon>
					</IconStyle>
					<LineStyle>
						<width>2</width>
					</LineStyle>
				</Style>
				<Style id="mainLine">
					<LineStyle>
						<color>ffff0000</color>
						<width>4</width>
					</LineStyle>
				</Style>
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
						<styleUrl>#globeIcon</styleUrl>
						<Point>
							<coordinates><xsl:value-of select="lng"/>,<xsl:value-of select="lat" /></coordinates>
						</Point>
					</Placemark>
				</xsl:for-each>
				<xsl:for-each select="app306932039/event">
					<Placemark id="{@id}">
						<name><xsl:value-of select="title" /></name>
						<description>
							<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
								<div>Posted By: <xsl:value-of select="username"/></div>
								<div>Creation Date: <xsl:value-of select="creation_date"/></div>
								<div>Content: <xsl:value-of select="content"/></div>
								<div>Event Date: <xsl:value-of select="event_date"/></div>
								<div>Capacity: <xsl:value-of select="capacity"/></div>
								<div>Number of Registered: <xsl:value-of select="num_of_registered"/></div>
							<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
						</description>
						<styleUrl>#globeIcon</styleUrl>
						<Point>
							<coordinates><xsl:value-of select="lng"/>,<xsl:value-of select="lat" /></coordinates>
						</Point>
					</Placemark>
				</xsl:for-each>
				<Placemark>
					<name>Main Line</name>
					<styleUrl>#mainLine</styleUrl>
					<extrude>1</extrude>
					<tessellate>1</tessellate>
					<LineString>
						<coordinates>
							<xsl:for-each select="app306932039/*">
								<xsl:value-of select="lng"/>,<xsl:value-of select="lat" />,0
							</xsl:for-each>
						</coordinates>
					</LineString>
				</Placemark>
			</Document>
		</kml>
	</xsl:template>
</xsl:stylesheet>