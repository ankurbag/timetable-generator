package edu.neu.ga.beans;

/**
 * Schedule is the final output of the program.
 */
public class Schedule {

	private int classId;
	private int groupId;
	private String moduleName;
	private String professorName;
	private String timeslot;
	private String roomNumber;

	/**
	 * Initialize a schedule
	 * 
	 * @param classId
	 * @param groupName
	 * @param moduleName
	 * @param professorName
	 * @param timeslot
	 * @param roomNumber
	 */
	public Schedule(int classId, int groupId, String moduleName, String professorName, String timeslot,
			String roomNumber) {
		super();
		this.classId = classId;
		this.groupId = groupId;
		this.moduleName = moduleName;
		this.professorName = professorName;
		this.timeslot = timeslot;
		this.roomNumber = roomNumber;
	}

	/**
	 * @return the classId
	 */
	public int getClassId() {
		return classId;
	}

	/**
	 * @param classId
	 *            the classId to set
	 */
	public void setClassId(int classId) {
		this.classId = classId;
	}

	/**
	 * @return groupId
	 */
	public int getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId
	 *            the groupId to set
	 */
	public void setGroupName(int groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param moduleName
	 *            the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return the professorName
	 */
	public String getProfessorName() {
		return professorName;
	}

	/**
	 * @param professorName
	 *            the professorName to set
	 */
	public void setProfessorName(String professorName) {
		this.professorName = professorName;
	}

	/**
	 * @return the timeslot
	 */
	public String getTimeslot() {
		return timeslot;
	}

	/**
	 * @param timeslot
	 *            the timeslot to set
	 */
	public void setTimeslot(String timeslot) {
		this.timeslot = timeslot;
	}

	/**
	 * @return the roomNumber
	 */
	public String getRoomNumber() {
		return roomNumber;
	}

	/**
	 * @param roomNumber
	 *            the roomNumber to set
	 */
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

}
