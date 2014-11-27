<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:my="http://my.com" exclude-result-prefixes="my">

<my:colorarray>
	<color>red</color>
	<color>blue</color>
	<color>orange</color>
	<color>purple</color>
	<color>green</color>
</my:colorarray>

<xsl:template match="/">
  <html>
		<img src="logo.png"/>
		<div style="background-color:#CC0033; width:100%;"><xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;]]></xsl:text></div>
	<body style="background-color:#DDDDDD">
		<xsl:for-each select="response/errormessage">
			<xsl:message terminate="yes" style="color:red"><xsl:value-of select="current()"/></xsl:message>
		</xsl:for-each>
		<xsl:choose>
			<xsl:when test="response/noschedulemessage">
				<p>
					No conflict-free schedule could be generated for this term <br />
					Either the courses you have left to complete in your pattern are not offered this term<br />
					Or you have no courses left to complete
				</p>
			</xsl:when>
			<xsl:otherwise>
				<h2>Schedule suggestion (does not include electives)</h2>
				<form action="api/register.php" method="POST">
					<table cellpadding="10" border="1" style='border-style: solid; border-bottom: none'>
						<th style='border-style: solid; border-top:none; border-left:none; border-right:none'>Time</th>
						<th style='border-style: solid; border-top:none; border-left:none; border-right:none'>Monday</th>
						<th style='border-style: solid; border-top:none; border-left:none; border-right:none'>Tuesday</th>
						<th style='border-style: solid; border-top:none; border-left:none; border-right:none'>Wednesday</th>
						<th style='border-style: solid; border-top:none; border-left:none; border-right:none'>Thursday</th>
						<th style='border-style: solid; border-top:none; border-left:none; border-right:none'>Friday</th>
						<xsl:for-each select="response/schedule/slot">
							<tr>
								<td><xsl:value-of select="@index"/></td>
								<xsl:for-each select="value">
									<td>
										<xsl:choose>
											<xsl:when test="@depth = 2">
												<xsl:attribute name="style">background-color:red</xsl:attribute>
											</xsl:when>
											<xsl:when test="@depth  = 1">
												<xsl:attribute name="style">background-color:blue</xsl:attribute>
											</xsl:when>
											<xsl:when test="@depth  = 3">
												<xsl:attribute name="style">background-color:green</xsl:attribute>
											</xsl:when>
											<xsl:when test="@depth  = 4">
												<xsl:attribute name="style">background-color:orange</xsl:attribute>
											</xsl:when>
											<xsl:when test="@depth  = 5">
												<xsl:attribute name="style">background-color:purple</xsl:attribute>
											</xsl:when>
											<xsl:when test="@depth  = 0">
												<xsl:attribute name="style">background-color:aqua</xsl:attribute>
											</xsl:when>
											<xsl:when test="@depth  = 6">
												<xsl:attribute name="style">background-color:maroon</xsl:attribute>
											</xsl:when>
											<xsl:when test="@depth  = 7">
												<xsl:attribute name="style">background-color:navy</xsl:attribute>
											</xsl:when>
											<xsl:when test="@depth  = 8">
												<xsl:attribute name="style">background-color:fuschia</xsl:attribute>
											</xsl:when>
											<xsl:when test="@depth  = 9">
												<xsl:attribute name="style">background-color:white</xsl:attribute>
											</xsl:when>
										</xsl:choose>
										<xsl:value-of select=".">
										</xsl:value-of>
									</td>
								</xsl:for-each>
							</tr>
						</xsl:for-each>
					</table>
					<xsl:if test="response/electives/*">
						<h2>Elective Selection</h2>
					</xsl:if>
					<xsl:for-each select="response/electives/elective">
						Select one elective from group <xsl:value-of select="@group"/>:  
						<select type="select" name="electives[]">
							<xsl:for-each select="option">
								<option>
									<xsl:attribute name="value">
										<xsl:value-of select="current()"/>
									</xsl:attribute>
									<xsl:value-of select="current()"/>
								</option>
							</xsl:for-each>
						</select>
						<br />
					</xsl:for-each>
					<br /><br />
					<h2>By pressing Confirm you will be enrolled in the following sections</h2>
					<table border="1">
						<xsl:for-each select="response/sections/section">
							<tr>
								<td>
								<label>
									<xsl:value-of select="concat(course-offering/code,course-offering/section)"/>
										<input checked="true" name="enroll[]" type="checkbox">
											<xsl:attribute  name="value">
												<xsl:value-of select="concat(course-offering/code,course-offering/section)"/>
											</xsl:attribute>
										</input>
									</label>
								</td>
							</tr>
						</xsl:for-each>
					</table>
					<br />
					<input type="submit" value="Confirm Selections"></input>
				</form>
			</xsl:otherwise>
		</xsl:choose>
	  </body>
  </html>
</xsl:template>

</xsl:stylesheet>