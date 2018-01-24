package org.usfirst.frc.team2791.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * 
 * @author team2791: See Robot.java for contact info
 */
public class RobotMap {
	
	// JOYSTICK PORTS
	public static final int JOYSTICK_DRIVER_PORT = 0;
	public static final int JOYSTICK_OPERATOR_PORT = 1;

	// DIO
	public static final int LEFT_DRIVE_ENCODER_PORT_A = 0;
	public static final int LEFT_DRIVE_ENCODER_PORT_B = 1;
	
	public static final int RIGHT_DRIVE_ENCODER_PORT_A = 2;
	public static final int RIGHT_DRIVE_ENCODER_PORT_B = 3;
	
	// PWM PORTS
	public static final int DRIVE_SPARK_LEFT_PORT_A = 8;
	public static final int DRIVE_SPARK_LEFT_PORT_B = 1;
	
	public static final int DRIVE_SPARK_RIGHT_PORT_A = 9;
	public static final int DRIVE_SPARK_RIGHT_PORT_B = 3;
	
	public static final int INTAKE_SPARK_PORTA = 5;
	public static final int INTAKE_SPARK_PORTB = 6;
	// CAN
	public static final int PDP = 0;
	
	//PDP Ports
	public static final int POWER_RIGHT_DRIVE_A = 0;
	public static final int POWER_RIGHT_DRIVE_B = 1;

	public static final int POWER_LEFT_DRIVE_A = 2;
	public static final int POWER_LEFT_DRIVE_B = 7;
	
	public static final int INTAKE_A = 3;
	public static final int INTAKE_B = 12;
}
