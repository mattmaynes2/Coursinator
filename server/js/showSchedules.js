function showSchedule(schedid)
{
	others = document.getElementsByName("schedulediv");
	for (var i=0; i<others.length;i++)
	{
		others[i].style.display = "none";
	}
	sched = document.getElementById("Schedule"+schedid);
	sched.style.display = "block";
}