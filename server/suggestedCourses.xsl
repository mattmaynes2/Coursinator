<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
	  <body>
		<h2>Eligible courses:</h2>
		<form>
			<table cellpadding="10" border="1">
				<xsl:for-each select="response/courses/course">
					<tr>
						<td>
						<label><xsl:value-of select="code"/><br /><xsl:value-of select="title"/></label>
						<xsl:for-each select="with">
							<br />Concurrently with: <xsl:value-of select="current()"/>
						</xsl:for-each>
						</td>
						<td><input name="selected_courses[]" type="checkbox"/></td>
					</tr>
				</xsl:for-each>
			</table>
			<br />
			<input type="submit"/>
		</form> 
		<h2>Schedule suggestion</h2>
		<table cellpadding="10" border="1">
			<th>Time</th>
			<th>Monday</th>
			<th>Tuesday</th>
			<th>Wednesday</th>
			<th>Thursday</th>
			<th>Friday</th>
			<xsl:for-each select="response/schedule/slot">
				<tr>
					<td><xsl:value-of select="@index"/></td>
					<xsl:for-each select="value">
						<td><xsl:value-of select="current()"/></td>
					</xsl:for-each>
				</tr>
			</xsl:for-each>
		</table>
	  </body>
  </html>
</xsl:template>

</xsl:stylesheet>