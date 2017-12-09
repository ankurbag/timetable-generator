package edu.neu.ga.beans;

public class Individual {

	/**
	 * In this case, the chromosome is an array of integers rather than a
	 * string.
	 */
	private int[] chromosome;
	private double fitness = -1;

	/**
	 * Initializes random individual based on a timetable
	 * 
	 * The Timetable class is a bit overloaded. It knows both fixed information
	 * (the courses that MUST be scheduled, the professors that MUST be given
	 * jobs, the classrooms that DO exist) -- but it also understands how to
	 * parse and unpack chromosomes which contain variable information (which
	 * professor teaches which class and when?)
	 * 
	 * In this case, we use the Timetable for the fixed information only, and
	 * generate a random chromosome, making guesses at the variable information.
	 * 
	 * Given the fixed information in a Timetable, we create a chromosome that
	 * randomly assigns timeslots, rooms, and professors to the chromosome for
	 * each student group and module.
	 * 
	 * @param timetable
	 *            The timetable information
	 */
	public Individual(Timetable timetable) {
		int numClasses = timetable.getNumClasses();

		// 1 gene for room, 1 for time, 1 for professor
		int chromosomeLength = numClasses * 3;
		// Create random individual
		int newChromosome[] = new int[chromosomeLength];
		int chromosomeIndex = 0;
		// Loop through groups
		for (Group group : timetable.getGroupsAsArray()) {
			// Loop through modules
			for (int moduleId : group.getModuleIds()) {
				// Add random time
				int timeslotId = timetable.getRandomTimeslot().getTimeslotId();
				newChromosome[chromosomeIndex] = timeslotId;
				chromosomeIndex++;

				// Add random room
				int roomId = timetable.getRandomRoom().getRoomId();
				newChromosome[chromosomeIndex] = roomId;
				chromosomeIndex++;

				// Add random professor
				Module module = timetable.getModule(moduleId);
				newChromosome[chromosomeIndex] = module.getRandomProfessorId();
				chromosomeIndex++;
			}
		}

		this.chromosome = newChromosome;
	}

	/**
	 * Initializes random individual
	 * 
	 * @param chromosomeLength
	 *            The length of the individuals chromosome
	 */
	public Individual(int chromosomeLength) {
		// Create random individual
		int[] individual;
		individual = new int[chromosomeLength];

		for (int gene = 0; gene < chromosomeLength; gene++) {
			individual[gene] = gene;
		}

		this.chromosome = individual;
	}

	/**
	 * Initializes individual with specific chromosome
	 * 
	 * @param chromosome
	 *            The chromosome to give individual
	 */
	public Individual(int[] chromosome) {
		// Create individual chromosome
		this.chromosome = chromosome;
	}

	/**
	 * Gets individual's chromosome
	 * 
	 * @return The individual's chromosome
	 */
	public int[] getChromosome() {
		return this.chromosome;
	}

	/**
	 * Gets individual's chromosome length
	 * 
	 * @return The individual's chromosome length
	 */
	public int getChromosomeLength() {
		return this.chromosome.length;
	}

	/**
	 * Set gene at offset
	 * 
	 * @param gene
	 * @param offset
	 */
	public void setGene(int offset, int gene) {
		this.chromosome[offset] = gene;
	}

	/**
	 * Get gene at offset
	 * 
	 * @param offset
	 * @return gene
	 */
	public int getGene(int offset) {
		return this.chromosome[offset];
	}

	/**
	 * Store individual's fitness
	 * 
	 * @param fitness
	 *            The individuals fitness
	 */
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	/**
	 * Gets individual's fitness
	 * 
	 * @return The individual's fitness
	 */
	public double getFitness() {
		return this.fitness;
	}

	public String toString() {
		String output = "";
		for (int gene = 0; gene < this.chromosome.length; gene++) {
			output += this.chromosome[gene] + ",";
		}
		return output;
	}

}
