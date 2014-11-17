<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
	  <body>
		<h2>Core courses available in the selected term:</h2>
		<form>
			<table cellpadding="10" border="1">
				<xsl:for-each select="response/courses/course">
					<tr>
						<td><label><xsl:value-of select="code"/><br /><xsl:value-of select="title"/></label></td>
						<td><input name="selected_courses[]" type="checkbox"/></td>
					</tr>
				</xsl:for-each>
			</table>
			<br />
			<input type="submit"/>
		</form>
	  </body>
  </html>
</xsl:template>

</xsl:stylesheet>