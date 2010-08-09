package am24.util;

public class PlanetConstraints {
	
	/***
	 * RobotEnergy - the amount of energy a robot has
	 */
	public static final int robotEnergy = 5000;
	
	/***
	 * nbOfBases - Number of bases.
	 */
	public static final int nbOfBases = 1;
	
	/***
	 * oreDensity - Density of ore.
	 */
	public static final double oreDensity = 0.05;
	
	
	/***
	 * gridSizeHeight - The Height;
	 */
	public static final int gridSizeHeight = 200;
	

	/***
	 * gridSizeWidth - The Width;
	 */
	public static final int gridSizeWidth = 200;
	
	/**
	 * mode - Coordination mode between bases,cooperative = 1 or competitive = 0.
	 */
	public static final int mode = 1;
	
	/*** 
	 * nbOfExplorers - Number of explorers per base.
	 */
	public static final int nbOfExplorers = 10;

	/***
	 * nbOfTransporter - Number of transporters per base.
	 */
	public static final int nbOfTransporter = 10;
	
	/***
	 * robotPerceptionScope
	 */
	public static final int robotPerceptionScope = 5;
	
	/***
	 * I - fixed communication scope.
	 */
	public static final int fixedRobotCommunicationScope = 10;
	
	/***
	 * robotMemorySize
	 */
	public static final int robotMemorySize = 10;
	
	/***
	 * C - Capacity of each base in number of ore samples.
	 */
	public static final int baseCapacity = 200;
	
	/***
	 * W - Maximal number of ore a robot can grab and/or carry.
	 */
	public static final int transporterOreCapacity = 8;
	
	/***
	 * maxSimulationTime  - Maximal number of cycles.
	 */
	public static final int maxSimulationTime = 10000;
}