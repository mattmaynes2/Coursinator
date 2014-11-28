<!DOCTYPE html>
<htmL><head>
	<title>Install — Coursinator</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<script src="js/install.js"></script>
</head><body>
	<h1>Setup and Install</h1>
	<p>Welcome to the install wizard.  You can either use the auto-install to install with the default data or use the other tools to upload custom data.</p>
	
	<h2>Auto-Install</h2>
	<p>The MySQL username and password must be set at the top of server/api/lib/db.php using the variables $defaultuser and $defaultpass</p>
	
	<p>Use the default data to create a complete working database.</p>
	<form onsubmit="auto_onsubmit(event)">
		<button type="submit">Create Everything</button>
	</form>
	<p id="auto_result"></p>
	
	<h2>Set up tables</h2>
	<p>This will create the required tables in the database.</p>
	<form onsubmit="create_onsubmit(event)">
		<label>
			Delete existing tables:
			<input id="create_force" type="checkbox"/>
		</label><br/>
		<button type="submit">Submit</button>
	</form>
	<p id="create_result"></p>
	
	<h2>Load Course Data</h2>
	<p>Load in the course offerings.</p>
	<p>The example data is in database/course_data.csv</p>
	<form onsubmit="course_onsubmit(event)">
		<label>
			Select data file (CSV):
			<input id="course_data" type="file"/>
		</label><br/>
		<button type="submit">Submit</button>
	</form>
	<p id="course_result"></p>
	
	<h2>Load Prerequisite Information</h2>
	<p>Load the prerequisite information into the database.</p>
	<p>The example data is in database/prerequsites.xml file.</p>
	<form onsubmit="pre_onsubmit(event)">
		<label>
			Select data file (CSV):
			<input id="pre_data" type="file"/>
		</label><br/>
		<button type="submit">Submit</button>
	</form>
	<p id="pre_result"></p>
</body></html>
