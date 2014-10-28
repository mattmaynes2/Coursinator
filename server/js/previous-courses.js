function hideProgram()
{
	document.getElementById("course_select").setAttribute("style", "visibility:hidden");
}

function getProgram()
{
	var listbox = document.getElementById("course_select");
	var selectedProgram = document.getElementById("program_select").options[document.getElementById("program_select").selectedIndex].value;
	
	listbox.setAttribute("style", "visibility:visible");
	listbox.innerHTML = listbox.innerHTML + "<option>"+selectedProgram+"</option>";
}