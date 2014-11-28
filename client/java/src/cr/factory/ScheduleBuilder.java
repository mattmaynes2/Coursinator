package cr.factory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cr.CourseOffering;
import cr.Schedule;
import xml.XMLBuilder;

public class ScheduleBuilder extends XMLBuilder<Schedule> {

	@Override
	public Schedule buildObject(Element node) {
		CourseOfferingBuilder cbuilder = new CourseOfferingBuilder();
		Schedule schedule = new Schedule();
		NodeList list;
		
		list = node.getElementsByTagName(CourseOffering.SCHEMA_IDENTIFIER);
		for(int i = 0; i < list.getLength(); i++){
			schedule.addCourseOffering(cbuilder.buildObject((Element)list.item(i)));
		}
		
		
		return schedule;
	}

	@Override
	public String getSchemaIdentifier() {
		return Schedule.SCHEMA_IDENTIFIER;
	}

	@Override
	public Schedule[] getEmptyArray() {
		return new Schedule[0];
	}

}
