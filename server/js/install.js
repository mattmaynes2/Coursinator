function create_onsubmit(e) {
	e.preventDefault();
	
	var force = document.getElementById("create_force").checked;
	var res   = document.getElementById("create_result");
	
	var req = new XMLHttpRequest();
	req.open("POST", "/api/create.php"+(force? "?force" : ""));
	req.send();
	
	res.textContent = "Sending request…";
	
	req.onreadystatechange = function create_onsubmit_res(){
		if (req.readyState != 4) return;
		if (200 <= req.status && req.status < 300) {
			// Success!
			res.textContent = "Creation Successfull!";
		} else {
			// Error!
			res.textContent = req.statsText + ": " + res.textContent;
		}
	};
}

function pre_onsubmit(e) {
	e.preventDefault();
	
	var data = document.getElementById("pre_data");
	var res  = document.getElementById("pre_result");
	
	data = data.files[0];
	if (!data) {
		res.textContent = "Please select a file.";
		return;
	}
	
	var req = new XMLHttpRequest();
	req.open("POST", "/api/prerequsites.php");
	req.setRequestHeader("Content-Type", "application/xml");
	req.send(data);
	
	res.textContent = "Sending request…";
	
	req.onreadystatechange = function create_onsubmit_res(){
		if (req.readyState != 4) return;
		if (200 <= req.status && req.status < 300) {
			// Success!
			res.textContent = "Creation Successfull!";
		} else {
			// Error!
			res.textContent = req.statsText + ": " + res.textContent;
		}
	};
}

function course_onsubmit(e) {
	e.preventDefault();
	
	var data = document.getElementById("course_data");
	var res  = document.getElementById("course_result");
	
	data = data.files[0];
	if (!data) {
		res.textContent = "Please select a file.";
		return;
	}
	
	var req = new XMLHttpRequest();
	req.open("POST", "/api/courseofferings.php");
	req.setRequestHeader("Content-Type", "application/xml");
	req.send(data);
	
	res.textContent = "Sending request…";
	
	req.onreadystatechange = function create_onsubmit_res(){
		if (req.readyState != 4) return;
		if (200 <= req.status && req.status < 300) {
			// Success!
			res.textContent = "Creation Successfull!";
		} else {
			// Error!
			res.textContent = req.statsText + ": " + res.textContent;
		}
	};
}
