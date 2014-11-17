//Hides the program courses list
function hideProgram()
{
	document.getElementById("off_pattern_options").style.display = "none";
	document.getElementById("on_pattern_options").style.display = "block";
}

function loadTasks()
{
	getAvailableTerms();
	getProgram();
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
				console.log(terms);
				for (var i=0; i<terms.length; i++)
				{
					var newopt = document.createElement("option");
					newopt.value = terms[i].childNodes[0].nodeValue;
					newopt.text = terms[i].childNodes[0].nodeValue;
					termselector.add(newopt);
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
	
	programRequest.open("GET", "api/programs.php?program="+selectedProgram, true);
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