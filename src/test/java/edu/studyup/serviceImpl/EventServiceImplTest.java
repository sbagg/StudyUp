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
	void testAddStudents_goodCase() throws StudyUpException {
		//creates student
		Student studentA = new Student();
		studentA.setFirstName("Jane");
		studentA.setLastName("Smith");
		studentA.setEmail("janeSmith@email.com");
		studentA.setId(2);
		int id1 = 1;
		
		//adds student to the event
		eventServiceImpl.addStudentToEvent(studentA, id1);
		
		//gets the student from the data storage and assign it to student list
		List<Student> temp = DataStorage.eventData.get(id1).getStudents();
		//throws an assert if student A is equal to temporary database event number assigned to it
		assertEquals(studentA, temp.get(id1));
		
	}
	@Test
	void testTooManyStudents_badCase() throws StudyUpException {
		//creates student
		Student studentA = new Student();
		studentA.setFirstName("Jane");
		studentA.setLastName("Smith");
		studentA.setEmail("janeSmith@email.com");
		studentA.setId(2);
		int id1 = 1;
		//creates student 2
		Student studentB = new Student();
		studentB.setFirstName("John");
		studentB.setLastName("Smith");
		studentB.setEmail("johnSmith@email.com");
		studentB.setId(3);
		
		//adds student to the event
		eventServiceImpl.addStudentToEvent(studentA, id1);
		
		//throws an assert if third student added
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(studentB, id1);
		  });
		
	}
	@Test
	void activeEvents_goodCase() throws StudyUpException {
		
		//gets the event from active events and assigns it to a temp list
		List<Event> temp = eventServiceImpl.getActiveEvents();
		//throws an assert if event Id is equal to eventId previously declared
		assertEquals(1, temp.get(0).getEventID());
		
	}
	@Test
	void activeEvents_badCase() throws StudyUpException {
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
		
	}
	@Test
	void getPastEvents_goodCase() {
		int eventID = 1;
		Date date = new Date(20, 3, 13);
		//set new date on the event ID
		DataStorage.eventData.get(eventID).setDate(date);
		//gets the event from active events and assigns it to a temp list
		List<Event> temp = eventServiceImpl.getPastEvents();
		//checks if new created temp article (0) and Something in the DataStorage
		assertEquals(DataStorage.eventData.get(eventID), temp.get(0));
		
	}
	@Test
	void getPastEvents_badCase() {
		int eventId = 1;
		Date date = new Date(50, 3, 10);
		//set new date on the event ID
		DataStorage.eventData.get(eventId).setDate(date);
		//gets the event from active events and assigns it to a temp list
		List<Event> temp = eventServiceImpl.getPastEvents();
				//throws an assert if event Id is equal to eventId previously declared
		//if size is 0 then send an alert
		
		assertEquals(0, temp.size());
//		Assertions.assertThrows(StudyUpException.class, () -> {
//			eventServiceImpl.getPastEvents().contains(o);
//		  });
		
	}
	@Test
	void delete_goodCase() {
		int eventID = 1;
		
		//If datastorage eventId, exists in in eventServiceImple
		assertEquals(DataStorage.eventData.get(eventID), eventServiceImpl.deleteEvent(eventID));
		
	}
	@Test
	void delete_badCase() {
		int eventID = 1;
		
		// exists in in eventServiceImple if it is null
		assertEquals(null, eventServiceImpl.deleteEvent(eventID));
			
	}
}
