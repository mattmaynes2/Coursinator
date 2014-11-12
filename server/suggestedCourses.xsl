<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <body>
    <h2>Courses you can take:</h2>
	<form>
		<table>
			<xsl:for-each select="response/courses/course">
				<tr>
					<td><label><xsl:value-of select="code"/>
						<input name="selected_courses[]" type="checkbox"/></label></td>
				</tr>
			</xsl:for-each>
		</table>
		<input type="submit"/>
	</form>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>