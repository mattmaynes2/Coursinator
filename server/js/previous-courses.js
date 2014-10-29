//Hides the program courses list
function hideProgram()
{
	document.getElementById("off_pattern_options").style.display = "none";
	document.getElementById("on_pattern_options").style.display = "block";
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
	var add = true;
	
	if (course_list.selectedIndex == -1)
		return;
	
	for(i=0; i<taken_list.options.length; i++)
	{
		if (taken_list.options[i].value === course_list.options[course_list.selectedIndex].value)
		{
			add = false;
		}
	}
	if (add)
	{
		taken_list.add(course_list.options[course_list.selectedIndex]);
	}
}

function removeCourseSelection()
{
	var taken_list = document.getElementById("courses_taken");
	var course_list = document.getElementById("course_select");
	
	if (taken_list.selectedIndex == -1)
	return;
		
	
	course_list.add(taken_list.options[taken_list.selectedIndex]);
}