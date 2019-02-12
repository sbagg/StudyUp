package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	@Test
	void testUpdateEvent_LongName_badCase() {
		int eventID = 1;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID,  "Renamed and updated Event 1");
		});
	}
	
	@Test
	void testUpdateEvent_Name_20chars()
	{
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID,  "Renamed Event 1 & up");
		assertEquals("Renamed Event 1 & up", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testGetActiveEvents_GoodCase()
	{
		List<Event> temp = eventServiceImpl.getActiveEvents();
		assertEquals(1, temp.get(0).getEventID());
	}
	
	@Test
	void testGetActiveEvents_BadCase()
	{
		int year = 95;
		int month = 1;
		int day = 3;
		int eventID = 1;
		Date date1 = new Date(year, month, day);
		DataStorage.eventData.get(eventID).setDate(date1);
		
		List<Event> temp = eventServiceImpl.getActiveEvents();
		assertEquals(0, temp.size());
	}
	
	@Test
	void testAddStudentToEvent_GoodCase() throws StudyUpException
	{
		int eventID = 1;
		Student student2 = new Student();
		student2.setFirstName("Jane");
		student2.setLastName("Doe");
		student2.setEmail("JaneDoe@email.com");
		student2.setId(2);
		
		eventServiceImpl.addStudentToEvent(student2, eventID);
		
		List<Student> temp = DataStorage.eventData.get(eventID).getStudents();
		assertEquals(student2, temp.get(1));
	}
	
	@Test
	void testAddStudentToEvent_TooManyStudents() throws StudyUpException
	{
		int eventID = 1;
		Student student2 = new Student();
		student2.setFirstName("Jane");
		student2.setLastName("Doe");
		student2.setEmail("JaneDoe@email.com");
		student2.setId(2);
		
		Student student3 = new Student();
		student3.setFirstName("Jane");
		student3.setLastName("Smith");
		student3.setEmail("JaneSmith@email.com");
		student3.setId(3);
		
		eventServiceImpl.addStudentToEvent(student2, eventID);
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student3, eventID);
		  });
	}
	
	@Test
	void testGetPastEvents_BadCase()
	{
		int eventID = 1;
		DataStorage.eventData.get(eventID).setDate(new Date(2020,1,3));
		List<Event> temp = eventServiceImpl.getPastEvents();
		assertEquals(0, temp.size());
	}
	
	@Test
	void testGetPastEvents_GoodCase()
	{
		int eventID = 1;
		DataStorage.eventData.get(eventID).setDate(new Date(95,1,3));
		List<Event> temp = eventServiceImpl.getPastEvents();
		assertEquals(DataStorage.eventData.get(eventID), temp.get(0));
	}
	
	@Test
	void testDelete_GoodCase()
	{
		int eventID = 1;
		assertEquals(DataStorage.eventData.get(eventID), eventServiceImpl.deleteEvent(eventID));
	}
	
	@Test
	void testDelete_BadCase()
	{
		int eventID = 3;
		assertEquals(null, eventServiceImpl.deleteEvent(eventID));
	}
	
}
