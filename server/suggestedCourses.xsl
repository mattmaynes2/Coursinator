<?xml version="1.0"?>

<xsl:stylesheet version="3.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:template match="/">
  <html>
  <head>
	<script type="text/javascript" src="js/showSchedules.js"/>
  </head>
		<div style="background-color:#CC0033; width:100%;"><xsl:text>&#xA0;</xsl:text></div>
	<body style="background-color:#DDDDDD">
		<header style="background-color:#CC0033; padding:0.4em">
			<img src="logo.png"/>
		</header>

		<xsl:choose>
			<xsl:when test="response/noschedulemessage">
				<p>
					No conflict-free schedule could be generated for this term <br />
					Either the courses you have left to complete in your pattern are not offered this term<br />
					Or you have no courses left to complete
				</p>
			</xsl:when>
			<xsl:otherwise>
				<h2>Timetable suggestion</h2>
				<xsl:for-each select= "response/schedule">
					<a href="javascript:void(0)">
						<xsl:attribute name="onclick">showSchedule(<xsl:value-of select="position()"/>)</xsl:attribute>
						Schedule <xsl:value-of select="position()"/>
					</a>
					<xsl:text disable-output-escaping="yes">&#xA0;</xsl:text>
				</xsl:for-each>
				<xsl:for-each select = "response/schedule">
					<div name="schedulediv" style="display:none">
						<xsl:if test="position()=1">
							<xsl:attribute name="style">display:block</xsl:attribute>
						</xsl:if>
						<xsl:attribute name="id">Schedule<xsl:value-of select="position()"/></xsl:attribute>
						<form action="api/register.php" method="GET">
							<table cellpadding="10" border="1" style='border-style: solid; border-bottom: none; width:50%; table-layout: fixed;'>
								<th style='border-style: solid; border-top:none; border-left:none; border-right:none'>Time</th>
								<th style='border-style: solid; border-top:none; border-left:none; border-right:none'>Monday</th>
								<th style='border-style: solid; border-top:none; border-left:none; border-right:none'>Tuesday</th>
								<th style='border-style: solid; border-top:none; border-left:none; border-right:none'>Wednesday</th>
								<th style='border-style: solid; border-top:none; border-left:none; border-right:none'>Thursday</th>
								<th style='border-style: solid; border-top:none; border-left:none; border-right:none'>Friday</th>
								<xsl:for-each select="slot">
									<tr>
										<td><xsl:value-of select="@index"/></td>
										<xsl:for-each select="value">
											<td>
												<xsl:choose>
													<xsl:when test="@depth = 2">
														<xsl:attribute name="style">border-style:solid; border-width:3px; border-color:red</xsl:attribute>
													</xsl:when>
													<xsl:when test="@depth  = 1">
														<xsl:attribute name="style">border-style:solid; border-width:3px; border-color:blue</xsl:attribute>
													</xsl:when>
													<xsl:when test="@depth  = 3">
														<xsl:attribute name="style">border-style:solid; border-width:3px; border-color:green</xsl:attribute>
													</xsl:when>
													<xsl:when test="@depth  = 4">
														<xsl:attribute name="style">border-style:solid; border-width:3px; border-color:orange</xsl:attribute>
													</xsl:when>
													<xsl:when test="@depth  = 5">
														<xsl:attribute name="style">border-style:solid; border-width:3px; border-color:purple</xsl:attribute>
													</xsl:when>
													<xsl:when test="@depth  = 0">
														<xsl:attribute name="style">border-style:solid; border-width:3px; border-color:aqua</xsl:attribute>
													</xsl:when>
													<xsl:when test="@depth  = 6">
														<xsl:attribute name="style">border-style:solid; border-width:3px; border-color:maroon</xsl:attribute>
													</xsl:when>
													<xsl:when test="@depth  = 7">
														<xsl:attribute name="style">border-style:solid; border-width:3px; border-color:navy</xsl:attribute>
													</xsl:when>
													<xsl:when test="@depth  = 8">
														<xsl:attribute name="style">border-style:solid; border-width:3px; border-color:fuscia</xsl:attribute>
													</xsl:when>
													<xsl:when test="@depth  = 9">
														<xsl:attribute name="style">border-style:solid; border-width:3px; border-color:white</xsl:attribute>
													</xsl:when>
												</xsl:choose>
												<xsl:value-of select=".">
												</xsl:value-of>
											</td>
										</xsl:for-each>
									</tr>
								</xsl:for-each>
							</table>
							<table>
								<xsl:for-each select="notimes/course-offering">
									<tr>
										<td style="color:red">Please note that <xsl:value-of select="concat(code,section)"/> 
										has no assigned meeting time. It is not displayed in your schedule; if you do not wish to register for this course please deselect it below</td>
									</tr>
								</xsl:for-each>
							</table>
							<h2>By pressing Confirm you will be enrolled in the following sections</h2>
							<table border="1">
								<xsl:for-each select="sections/course-offering">
									<tr>
										<td>
										<label>
											<xsl:value-of select="concat(code,section)"/>
												<input checked="true" name="enroll[]" type="checkbox">
													<xsl:attribute  name="value">
														<xsl:value-of select="concat(code,section)"/>
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
					</div>
				</xsl:for-each>
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
					<br /><br />
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	  </body>
  </html>
</xsl:template>

</xsl:stylesheet>