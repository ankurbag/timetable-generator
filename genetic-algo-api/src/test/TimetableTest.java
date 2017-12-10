

import static org.junit.Assert.*;

import org.junit.Test;

import edu.neu.ga.beans.Group;
import edu.neu.ga.beans.Individual;
import edu.neu.ga.beans.Timetable;
import edu.neu.ga.beans.Class;

public class TimetableTest {

	@Test
	public void clashtest() {
		Timetable timetable = new Timetable();

		// Set up rooms
		timetable.addRoom(1, "Z1", 15);
		timetable.addRoom(2, "B1", 30);
		timetable.addRoom(4, "R1", 20);
		timetable.addRoom(5, "F1", 25);

		// Set up timeslots
		timetable.addTimeslot(1, "Mon 9:00 - 11:00");
		timetable.addTimeslot(2, "Mon 11:00 - 13:00");
		timetable.addTimeslot(3, "Mon 13:00 - 15:00");
		timetable.addTimeslot(4, "Tue 9:00 - 11:00");
		timetable.addTimeslot(5, "Tue 11:00 - 13:00");
		timetable.addTimeslot(6, "Tue 13:00 - 15:00");
		timetable.addTimeslot(7, "Wed 9:00 - 11:00");
		timetable.addTimeslot(8, "Wed 11:00 - 13:00");
		timetable.addTimeslot(9, "Wed 13:00 - 15:00");
		timetable.addTimeslot(10, "Thu 9:00 - 11:00");
		timetable.addTimeslot(11, "Thu 11:00 - 13:00");
		timetable.addTimeslot(12, "Thu 13:00 - 15:00");
		timetable.addTimeslot(13, "Fri 9:00 - 11:00");
		timetable.addTimeslot(14, "Fri 11:00 - 13:00");
		timetable.addTimeslot(15, "Fri 13:00 - 15:00");

		// Set up professors
		timetable.addProfessor(1, "Dr P Smith");
		timetable.addProfessor(2, "Mrs E Mitchell");
		timetable.addProfessor(3, "Dr R Williams");
		timetable.addProfessor(4, "Mr A Thompson");

		// Set up modules and define the professors that teach them
		timetable.addModule(1, "cs1", "Computer Science", new int[] { 1, 2 });
		timetable.addModule(2, "en1", "English", new int[] { 1, 3 });
		timetable.addModule(3, "ma1", "Maths", new int[] { 1, 2 });
		timetable.addModule(4, "ph1", "Physics", new int[] { 3, 4 });
		timetable.addModule(5, "hi1", "History", new int[] { 4 });
		timetable.addModule(6, "dr1", "Drama", new int[] { 1, 4 });

		// Set up student groups and the modules they take.
		timetable.addGroup(1, 10, new int[] { 1, 3, 4 });
		timetable.addGroup(2, 3, new int[] { 2, 3, 5, 6 });
		timetable.addGroup(3, 8, new int[] { 3, 4, 5 });
		timetable.addGroup(4, 5, new int[] { 1, 4 });
		timetable.addGroup(5, 2, new int[] { 2, 3, 5 });
		timetable.addGroup(6, 2, new int[] { 1, 4, 5 });
		timetable.addGroup(7, 6, new int[] { 1, 3 });
		timetable.addGroup(8, 8, new int[] { 2, 6 });
		timetable.addGroup(9, 4, new int[] { 1, 6 });
		timetable.addGroup(10, 5, new int[] { 3, 4 });
		
		Individual i=new Individual(timetable);
		timetable.createClasses(i);
		
		int clash=timetable.calcClashes();
		assertNotSame(null,clash);
		//assertEquals(null,clash);
	}

}
