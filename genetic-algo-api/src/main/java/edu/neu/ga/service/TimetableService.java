package edu.neu.ga.service;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.neu.ga.beans.Class;
import edu.neu.ga.beans.GeneticAlgorithm;
import edu.neu.ga.beans.Population;
import edu.neu.ga.beans.Schedule;
import edu.neu.ga.beans.Timetable;
import edu.neu.ga.controller.TimetableController;

/**
 * The real stuff happens in the GeneticAlgorithm class and the Timetable class.
 * 
 * The Timetable class is what the genetic algorithm is expected to create a
 * valid version of -- meaning, after all is said and done, a chromosome is read
 * into a Timetable class, and the Timetable class creates a nicer, neater
 * representation of the chromosome by turning it into a proper list of Classes
 * with rooms and professors and whatnot.
 * 
 * The Timetable class also understands the problem's Hard Constraints (ie, a
 * professor can't be in two places simultaneously, or a room can't be used by
 * two classes simultaneously), and so is used by the GeneticAlgorithm's
 * calcFitness class as well.
 * 
 * @author ankur
 *
 */
public class TimetableService {

	private Timetable timetable;
	private GeneticAlgorithm ga;
	private Population population;
	private int generation;

	private static final Logger logger = LoggerFactory.getLogger(TimetableService.class);

	// GA Parameters
	private static final int POPULATION_SIZE = 1000;
	private static final double MUTATION_RATE = 0.01;
	private static final double CROSSOVER_RATE = 0.9;
	private static final int ELITISCM_COUNT = 2;
	private static final int TOURNAMENT_SIZE = 5;

	/**
	 * Constructor initializing the GA Parameters
	 */
	public TimetableService() {
		timetable = initializeTimetable();

		// Initialize GA
		ga = new GeneticAlgorithm(POPULATION_SIZE, MUTATION_RATE, CROSSOVER_RATE, ELITISCM_COUNT, TOURNAMENT_SIZE);

		// Initialize population
		population = ga.initPopulation(timetable);

		// Evaluate population
		ga.evalPopulation(population, timetable);

		// Keep track of current generation
		generation = 1;
	}

	/**
	 * Returns List of Class for a schedule. It is a single threaded execution.
	 * 
	 * @return
	 */
	public List<Schedule> execute_without_parallisation() {

		// Start evolution loop
		while (ga.isTerminationConditionMet(generation, 1000) == false
				&& ga.isTerminationConditionMet(population) == false) {
			// Print fitness
			logger.info("G" + generation + " Best fitness: " + population.getFittest(0).getFitness());

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
		logger.info("Solution found in " + generation + " generations");
		logger.info("Final solution fitness: " + population.getFittest(0).getFitness());
		logger.info("Clashes: " + timetable.calcClashes());

		// Store in the Schedule
		Class classes[] = timetable.getClasses();
		List<Schedule> schedule = new ArrayList<>();
		int classIndex = 1;
		for (Class bestClass : classes) {
			Schedule sc = new Schedule(classIndex, timetable.getGroup(bestClass.getGroupId()).getGroupId(),
					timetable.getModule(bestClass.getModuleId()).getModuleName(),
					timetable.getProfessor(bestClass.getProfessorId()).getProfessorName(),
					timetable.getTimeslot(bestClass.getTimeslotId()).getTimeslot(),
					timetable.getRoom(bestClass.getRoomId()).getRoomNumber());
			schedule.add(sc);
			classIndex++;
		}
		return schedule;
	}

	/**
	 * Returns List of Class for Schedule. Parallelized execution using
	 * CompletableFuture
	 * 
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public List<Schedule> execute() throws InterruptedException, ExecutionException {

		// Start evolution loop

		// Run Evolution for Partition 1
		CompletableFuture<Map<Integer, Population>> partition1 = CompletableFuture.supplyAsync(() -> {
			return evolve(ga, timetable);
		});

		// Run Evolution for Partition 2
		CompletableFuture<Map<Integer, Population>> patition2 = CompletableFuture.supplyAsync(() -> {
			return evolve(ga, timetable);
		});

		// Merge the results
		CompletableFuture<Map<Integer, Population>> combinedFuture = patition2.thenCombine(partition1, (xs1, xs2) -> {
			System.out.println("In merge..");
			Map<Integer, Population> result = new LinkedHashMap<Integer, Population>(xs1.size() + xs2.size());
			return merge(result, xs1, xs2);
		});

		// Get the fittest population
		Map<Integer, Population> result = combinedFuture.get();
		population = result.get(result.size());

		// Print fitness
		timetable.createClasses(population.getFittest(0));
		System.out.println();
		System.out.println("Solution found in " + result.size() + " generations");
		System.out.println("Final solution fitness: " + population.getFittest(0).getFitness());
		System.out.println("Clashes: " + timetable.calcClashes());

		// Store in the Schedule
		Class classes[] = timetable.getClasses();
		List<Schedule> schedule = new ArrayList<>();
		int classIndex = 1;
		for (Class bestClass : classes) {
			Schedule sc = new Schedule(classIndex, timetable.getGroup(bestClass.getGroupId()).getGroupId(),
					timetable.getModule(bestClass.getModuleId()).getModuleName(),
					timetable.getProfessor(bestClass.getProfessorId()).getProfessorName(),
					timetable.getTimeslot(bestClass.getTimeslotId()).getTimeslot(),
					timetable.getRoom(bestClass.getRoomId()).getRoomNumber());
			schedule.add(sc);
			classIndex++;
		}
		return schedule;
	}

	// Helper Methods
	/**
	 * Creates a Timetable with all the necessary course information.
	 * 
	 * Normally you'd get this info from a database.
	 * 
	 * @return
	 */
	private Timetable initializeTimetable() {
		// Create timetable
		Timetable timetable = new Timetable();

		// Set up rooms
		timetable.addRoom(1, "A1", 15);
		timetable.addRoom(2, "B1", 30);
		timetable.addRoom(4, "D1", 20);
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
		timetable.addGroup(2, 30, new int[] { 2, 3, 5, 6 });
		timetable.addGroup(3, 18, new int[] { 3, 4, 5 });
		timetable.addGroup(4, 25, new int[] { 1, 4 });
		timetable.addGroup(5, 20, new int[] { 2, 3, 5 });
		timetable.addGroup(6, 22, new int[] { 1, 4, 5 });
		timetable.addGroup(7, 16, new int[] { 1, 3 });
		timetable.addGroup(8, 18, new int[] { 2, 6 });
		timetable.addGroup(9, 24, new int[] { 1, 6 });
		timetable.addGroup(10, 25, new int[] { 3, 4 });
		return timetable;
	}

	/**
	 * Creates a map of generation and population in ascending order.
	 * 
	 * @param ga
	 * @param timetable
	 * @return Map <Integer,Population>
	 */
	private Map<Integer, Population> evolve(GeneticAlgorithm ga, Timetable timetable) {
		// Initialize Result
		Map<Integer, Population> res = new HashMap();

		// Initialize Population
		Population population = ga.initPopulation(timetable);

		// Evaluate population
		ga.evalPopulation(population, timetable);

		// Keep track of current generation
		int generation = 1;

		// Start evolution loop
		while (ga.isTerminationConditionMet(generation, 1000) == false
				&& ga.isTerminationConditionMet(population) == false) {
			// Print fitness
			System.out.println("G" + generation + " Best fitness: " + population.getFittest(0).getFitness());

			// Apply crossover
			population = ga.crossoverPopulation(population);

			// Apply mutation
			population = ga.mutatePopulation(population, timetable);

			// Evaluate population
			ga.evalPopulation(population, timetable);

			// Add to the Map
			res.put(generation, population);

			// Increment the current generation
			generation++;

		}
		return res;

	}

	/**
	 * Merge the results of 2 partitions in the order of population fitness.
	 * 
	 * @param result
	 * @param left
	 * @param right
	 * @return
	 */
	private Map<Integer, Population> merge(Map<Integer, Population> result, Map<Integer, Population> left,
			Map<Integer, Population> right) {
		int i1 = 1;
		int i2 = 1;
		System.out.println(left.size() + right.size());
		int size = left.size() + right.size();
		for (int i = 1; i < size; i++) {
			if (i2 > right.size() || (i1 < left.size()
					&& left.get(i1).getPopulationFitness() <= right.get(i2).getPopulationFitness())) {
				result.put(i, left.get(i1));
				i1++;
			} else {
				result.put(i, right.get(i2));
				i2++;
			}
		}
		return result;
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		new TimetableService().execute();
	}

}
