package org.usfirst.frc.team2791.robot.util;

/**
 * This class holds all important constants that are used in the code.
 * Our main utility in here is for PID tuning constants which we have found experimentally.
 * 
 * @author team2791: See Robot.java for contact info
 * @see Robot
 */
public class CONSTANTS { //for constants ONLY; ports are in RobotMap
	
	// JOYSTICK deadzone
	public static final double DEADZONE = 0;
	
	//Drive constants
	public static final double driveEncoderTicks = 256;//Greyhill standard encoder
	
	public static final double WHEEL_DIAMETER_IN_IN = 4.0;//Drive wheels - Colson
	public static final double WHEEL_DIAMETER_IN_FEET = WHEEL_DIAMETER_IN_IN/12.0;
	
	public static final double DRIVETRAIN_SPEED_MULTIPLIER = 1.0; //Used if you want to slow down the drivetrain.
}