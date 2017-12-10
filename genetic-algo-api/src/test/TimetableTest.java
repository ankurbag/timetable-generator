import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.neu.ga.beans.Group;
import edu.neu.ga.beans.Individual;
import edu.neu.ga.beans.Population;
import edu.neu.ga.beans.Timetable;
import edu.neu.ga.controller.TimetableController;
import edu.neu.ga.service.TimetableService;
import edu.neu.ga.beans.Class;
import edu.neu.ga.beans.GeneticAlgorithm;

/**
 * This is the Test case to test the Schedule/Timetable preparation.
 * 
 * @author ankurbag/elton/ishita
 *
 */
public class TimetableTest {

	private static final Logger logger = LoggerFactory.getLogger(TimetableTest.class);

	// Test case to see whether there is a clash in the schedule
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

		GeneticAlgorithm ga = new GeneticAlgorithm(1000, 0.09, 0.1, 2, 5);

		// Initialize population
		Population population = ga.initPopulation(timetable);

		// Evaluate population
		ga.evalPopulation(population, timetable);

		// Keep track of current generation
		int generation = 1;
		// Start evolution loop
		while (ga.isTerminationConditionMet(generation, 1000) == false
				&& ga.isTerminationConditionMet(population) == false) {

			// Apply crossover
			population = ga.crossoverPopulation(population);

			// Apply mutation
			population = ga.mutatePopulation(population, timetable);

			// Evaluate population
			ga.evalPopulation(population, timetable);

			// Increment the current generation
			generation++;
		}

		// Print fitness
		timetable.createClasses(population.getFittest(0));
		// The clash should be 0
		int clash = timetable.calcClashes();
		assertSame(0, clash);
	}

	// Test case to see performance
	@Test
	public void singlethreaded_test() {
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

		GeneticAlgorithm ga = new GeneticAlgorithm(1000, 0.09, 0.1, 2, 5);

		// Initialize population
		Population population = ga.initPopulation(timetable);

		// Evaluate population
		ga.evalPopulation(population, timetable);

		// Record start Time
		long start = System.currentTimeMillis();

		// Keep track of current generation
		int generation = 1;
		// Start evolution loop
		while (ga.isTerminationConditionMet(generation, 1000) == false
				&& ga.isTerminationConditionMet(population) == false) {

			// Apply crossover
			population = ga.crossoverPopulation(population);

			// Apply mutation
			population = ga.mutatePopulation(population, timetable);

			// Evaluate population
			ga.evalPopulation(population, timetable);

			// Increment the current generation
			generation++;
		}
		// Record end time
		long end = System.currentTimeMillis();
		long total = end - start;
		// Get classes
		int classes = timetable.getNumClasses();

		// Pass if number of classes are more than 0
		assertTrue(classes > 0);
		logger.info("Time taken to generate without Parrellization " + total + " MS");

	}

	// Test case to see performance
	@Test
	public void parallelga_test() throws InterruptedException, ExecutionException {
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

		// Divide 2 The population in 2 partitions
		GeneticAlgorithm ga = new GeneticAlgorithm(50000, 0.09, 0.1, 2, 5);
		GeneticAlgorithm ga1 = new GeneticAlgorithm(50000, 0.09, 0.1, 2, 5);

		// Initialize population
		Population population = ga.initPopulation(timetable);
		Population population1 = ga.initPopulation(timetable);

		// Evaluate population
		ga.evalPopulation(population, timetable);
		ga1.evalPopulation(population1, timetable);

		// Record start Time
		long start = System.currentTimeMillis();

		// Run Evolution for Partition 1
		CompletableFuture<Map<Integer, Population>> partition1 = CompletableFuture.supplyAsync(() -> {
			return new TimetableService().evolve(ga, timetable);
		});

		// Run Evolution for Partition 2
		CompletableFuture<Map<Integer, Population>> patition2 = CompletableFuture.supplyAsync(() -> {
			return new TimetableService().evolve(ga1, timetable);
		});

		// Merge the results
		CompletableFuture<Map<Integer, Population>> combinedFuture = patition2.thenCombine(partition1, (xs1, xs2) -> {
			logger.info("In merge..");
			Map<Integer, Population> result = new LinkedHashMap<Integer, Population>(xs1.size() + xs2.size());
			return new TimetableService().merge(result, xs1, xs2);
		});

		// Get the fittest population
		Map<Integer, Population> result = combinedFuture.get();
		population = result.get(result.size());

		// Record end time
		long end = System.currentTimeMillis();
		long total = end - start;
		// Get classes
		int classes = timetable.getNumClasses();

		// Pass if number of classes are more than 0
		assertTrue(classes > 0);
		logger.info("Time taken to generate with Parrellization " + total + " MS");

	}

}
