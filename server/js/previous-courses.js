//Hides the program courses list
function hideProgram()
{
	document.getElementById("off_pattern_options").style.display = "none";
	document.getElementById("on_pattern_options").style.display = "block";
}

function addOther()
{
	var code = document.getElementById("othercode").value;
	if (code.match('^[A-Z]{1,8}[0-9]{4}$') == null)
	{
		document.getElementById("errormessage").innerHTML = "Invalid course code";
	}
	else
	{
		document.getElementById("errormessage").innerHTML = "";
		var taken_list = document.getElementById("courses_taken");
		var newopt = document.createElement("option");
		newopt.value = code;
		newopt.text = code;
		taken_list.add(newopt);
	}
}

function setUpSubmission()
{
	var submissionList = document.getElementById("courses_taken");
	for(var i=0; i<submissionList.options.length; i++)
	{
		submissionList.options[i].selected = true;
	}
}

function loadTasks()
{
	getAvailableTerms();
	getPrograms();
	getProgram();
}

function getPrograms()
{
	programRequest = new XMLHttpRequest();
	programRequest.onreadystatechange = function()
	{
		if (programRequest.readyState == 4 && programRequest.status == 200)
		{
			var parser = new DOMParser();
			var doc = parser.parseFromString(programRequest.responseText, "application/xml");
			var programs = doc.getElementsByTagName("program");
			var programselector = document.getElementById("program_select");

			for (var i=0; i<programs.length; i++)
			{
				var newopt = document.createElement("option");
				var programID = programs[i].getElementsByTagName("id")[0].childNodes[0].textContent;

				var programName = programs[i].getElementsByTagName("name")[0].childNodes[0].textContent;
				newopt.text = programName;
				newopt.value = programID;
				programselector.options.add(newopt);
			}
		}
	}
	programRequest.open("GET", "api/programs.php", true);
	programRequest.send();
}

//Gets the terms available for registration from the server
function getAvailableTerms()
{
	termRequest = new XMLHttpRequest();
	termRequest.onreadystatechange=function()	
		{
			if (termRequest.readyState==4 && termRequest.status==200)
			{
				var parser = new DOMParser();
				var doc = parser.parseFromString(termRequest.responseText, "application/xml");
				var terms = doc.getElementsByTagName("term");
				var termselector = document.getElementById("termselect");
				for (var i=0; i<terms.length; i++)
				{
					var newopt = document.createElement("option");
					var termText = terms[i].attributes.getNamedItem("which").value == 0 ? "Fall " : "Winter ";
					newopt.text = termText + terms[i].attributes.getNamedItem("year").value;
					newopt.value = terms[i].attributes.getNamedItem("which").value + "," + terms[i].attributes.getNamedItem("year").value ;
					termselector.options.add(newopt);
				}
			}
		}
	
	termRequest.open("GET", "api/availableterms.php", true);
	termRequest.send();
}

//Fills the program courses list with programs for the selected courses
function getProgram()
{
	var programRequest;
	var listbox = document.getElementById("course_select");
	var selected_courses = document.getElementById("courses_taken");

	if (!document.getElementById("program_select").options[document.getElementById("program_select").selectedIndex])
	{
		return;
	}
	var selectedProgram = document.getElementById("program_select").options[document.getElementById("program_select").selectedIndex].value;
	
	if (!document.getElementById("offpattern").checked)
	{
		return;
	}
	
	document.getElementById("off_pattern_options").style.display = "block";
	document.getElementById("on_pattern_options").style.display = "none";
	
	programRequest = new XMLHttpRequest();
	
	programRequest.onreadystatechange=function()	
		{
			if (programRequest.readyState==4 && programRequest.status==200)
			{
				listbox.innerHTML = "";
				selected_courses.innerHTML = "";
				var parser = new DOMParser();
				var doc = parser.parseFromString(programRequest.responseText, "application/xml");
				var courses = doc.getElementsByTagName("course");
				for (var i=0; i<courses.length; i++)
				{
					var newopt = document.createElement("option");
					newopt.value = courses[i].childNodes[0].nodeValue;
					newopt.text = courses[i].childNodes[0].nodeValue;
					listbox.add(newopt);
				}
			}
		}

	programRequest.open("GET", "api/programelements.php?program="+selectedProgram, true);
	programRequest.send();
}

function addCourseSelection()
{
	var course_list = document.getElementById("course_select");
	var taken_list = document.getElementById("courses_taken");
	var options = course_list && course_list.options;
	var selected = [];
	
	for (var i=0; i<options.length; i++)
	{
		if (options[i].selected)
		{
			selected.push(options[i]);
		}
	}
	
	for (var i=0; i<selected.length; i++)
	{
		taken_list.add(selected[i],0);
	}
}

function removeCourseSelection()
{
	var taken_list = document.getElementById("courses_taken");
	var course_list = document.getElementById("course_select");
	
	var options = taken_list && taken_list.options;
	var selected = [];
	
	for (var i=0; i<options.length; i++)
	{
		if (options[i].selected)
		{
			selected.push(options[i]);
		}
	}
	
	for (var i=0; i<selected.length; i++)
	{
		course_list.add(selected[i],0);
	}
}