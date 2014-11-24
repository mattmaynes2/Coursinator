<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
	  <body>
		<!-- <h2>Attempting to schedule:</h2>
		<form>
			<table cellpadding="10" border="1">
				<xsl:for-each select="response/courses/course">
					<tr>
						<td>
						<label><xsl:value-of select="code"/><br /><xsl:value-of select="title"/></label>
						<xsl:for-each select="with">
							<br /><b>Concurrently with: <xsl:value-of select="current()"/></b>
						</xsl:for-each>
						</td>
						<td><input name="selected_courses[]" type="checkbox"/></td>
					</tr>
				</xsl:for-each>
			</table>
			<br />
			<input type="submit"/>
		</form>  -->
		<h2>Schedule suggestion</h2>
		<form>
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
								<xsl::attribute  name="rowspan" select="@span"/>
								<xsl:value-of select="current()"/>
							</td>
						</xsl:for-each>
					</tr>
				</xsl:for-each>
			</table>
			<xsl:if test="response/electives/*">
				<h2>Elective Selection</h2>
			</xsl:if>
			<xsl:for-each select="response/electives/elective">
				Select one elective:
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
			<input type="submit"/>
		</form>
	  </body>
  </html>
</xsl:template>

</xsl:stylesheet>