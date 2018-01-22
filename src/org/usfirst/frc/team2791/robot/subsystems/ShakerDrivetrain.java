package org.usfirst.frc.team2791.robot.subsystems;

import org.usfirst.frc.team2791.robot.Robot;
import org.usfirst.frc.team2791.robot.RobotMap;
import org.usfirst.frc.team2791.robot.commands.DriveWithJoystick;
import org.usfirst.frc.team2791.robot.util.CONSTANTS;
import org.usfirst.frc.team2791.robot.util.SpeedControllerSet;
import org.usfirst.frc.team2791.robot.util.Util;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class corresponds to the drivetrain. This code is modeled after a drivetrain with four motors and four Spark speed controllers.
 * (Note: Talon and Spark speed controllers work off of the same code.) The controls are done for a tank drive system.
 * In terms, of sensors, this subsystem assumed one encoder on each side of the drivetrain and a gyro within the robot.
 * This code works with the "ADXRS450 gyro" supplied in the FRC Kit-of-parts.
 * 
 * @author team2791: See Robot.java for contact info
 */
public class ShakerDrivetrain extends Subsystem{

	private Spark leftSparkA, leftSparkB;    
	private Spark rightSparkA, rightSparkB;
	
	protected Encoder leftDriveEncoder = null;
	protected Encoder rightDriveEncoder = null;

	public ADXRS450_Gyro gyro;
	public boolean gyroDisabled = false;

	protected RobotDrive shakyDrive = null;
	
	//The next three initialization sections are for encoder and gyro helpers.
	protected double previousRate = 0;
	protected double previousRateTime = 0;
	protected double currentRate = 0;
	protected double currentTime = 0;

	protected double leftPreviousRate = 0;
	protected double leftPreviousTime = 0;
	protected double leftCurrentRate = 0;
	protected double leftCurrentTime = 0;
	protected double leftFilteredAccel = 0;

	protected double rightPreviousRate = 0;
	protected double rightPreviousTime = 0;
	protected double rightCurrentRate = 0;
	protected double rightCurrentTime = 0;
	protected double rightFilteredAccel = 0;

	//Determines the amount of distance traveled for every pulse read on the encoders
	private double distancePerPulse = Util.tickToFeet(CONSTANTS.driveEncoderTicks, CONSTANTS.WHEEL_DIAMETER_IN_FEET);

	public ShakerDrivetrain(){
		
		leftSparkA = new Spark(RobotMap.DRIVE_SPARK_LEFT_PORT_A);
		leftSparkB = new Spark(RobotMap.DRIVE_SPARK_LEFT_PORT_B);
		
		rightSparkA = new Spark(RobotMap.DRIVE_SPARK_RIGHT_PORT_A);
		rightSparkB = new Spark(RobotMap.DRIVE_SPARK_RIGHT_PORT_B);

		leftDriveEncoder = new Encoder(RobotMap.LEFT_DRIVE_ENCODER_PORT_A, RobotMap.LEFT_DRIVE_ENCODER_PORT_B);
		rightDriveEncoder = new Encoder(RobotMap.RIGHT_DRIVE_ENCODER_PORT_A,RobotMap.RIGHT_DRIVE_ENCODER_PORT_B);

		//Uses the Sparks to create a robotDrive (it has methods that allow for easier control of the whole drivetrain at once)
		shakyDrive = new RobotDrive(new SpeedControllerSet(leftSparkA,leftSparkB), new SpeedControllerSet(rightSparkA,rightSparkB));
		
		//Inverts the motor outputs so that the right and left motors both turn the right direction for forward drive
		shakyDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		shakyDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

		// stops all motors right away just in case
		shakyDrive.stopMotor();

		leftDriveEncoder.reset();
		rightDriveEncoder.reset();

		//configures the encoder so that it can be used in drive PID functions
		leftDriveEncoder.setDistancePerPulse(distancePerPulse); 
		rightDriveEncoder.setDistancePerPulse(-distancePerPulse); 

		//keeps the gyro from throwing startCompetition() errors and allows us to troubleshoot errors
		try{
			gyro = new ADXRS450_Gyro();//SPI.Port.kOnboardCS1
			gyro.calibrate(); //takes 5 seconds
			gyro.reset();
		}catch(NullPointerException e){
			gyroDisabled = true;
			System.out.println("Gyro is unplugged, Disabling Gyro");
		}

	}
	
	public void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoystick());
	}

	/**
	 * Stops the drivetrain
	 */
	public void disable() {
		shakyDrive.stopMotor();
	}
	
	/** 
	 * Drivetrain motor outputs; Accepts values between -1.0 and +1.0
	 * @param left motor output
	 * @param right motor output*/
	public void setLeftRightMotorOutputs(double left, double right){
		shakyDrive.setLeftRightMotorOutputs(left, right);
	}
	
	/**
	 * Drivetrain sfx outputs
	 */
	public void debug() {

		SmartDashboard.putNumber("Left Drive Encoders Rate", leftDriveEncoder.getRate());
		SmartDashboard.putNumber("Right Drive Encoders Rate", rightDriveEncoder.getRate());
		SmartDashboard.putNumber("Encoder Angle", getAngleEncoder());

		SmartDashboard.putNumber("LEncoderDistance", leftDriveEncoder.getDistance());
		SmartDashboard.putNumber("REncoderDistance", rightDriveEncoder.getDistance());
		SmartDashboard.putNumber("AvgEncoderDistance", getAverageDist());

		SmartDashboard.putNumber("Gyro angle", gyro.getAngle());
		SmartDashboard.putNumber("Gyro rate", gyro.getRate());

		SmartDashboard.putNumber("Avg Acceleration", getAverageAcceleration());
		
		SmartDashboard.putNumber("Drivetrain total current", getCurrentUsage());


		SmartDashboard.putString("LDist vs RDist vs AvgDist", getLeftDistance()+":"+getRightDistance()+":"+getAverageDist());
		SmartDashboard.putString("LVel vs RVel vs AvgVel", getLeftVelocity()+":"+getRightVelocity()+":"+getAverageVelocity());
		SmartDashboard.putString("LAcc vs RAcc vs AvgAcc", getLeftAcceleration()+":"+getRightAcceleration()+":"+getAverageAcceleration());
		
	}
	
	
	
	//************** Gyro and Encoder Helper Methods **************//

	@Deprecated
	public double getAngleEncoder() {
		return (360 / 7.9) * (getLeftDistance() - getRightDistance()) / 2.0;
	}

	public double getGyroAngle() {
		if(!gyroDisabled)  
			return gyro.getAngle();
		System.err.println("Gyro is Disabled, Angle is Incorrect");
		return 0.0;
	}
	
	@Deprecated
	public double getEncoderAngleRate() {
		return (360/7.9) * (leftDriveEncoder.getRate() - rightDriveEncoder.getRate()) / 2.0;
	}
	
	public double getGyroRate() {
		if(!gyroDisabled)
			return gyro.getRate();
		System.err.println("Gyro is Disabled, Rate is Incorrect");
		return 0.0;
	}

	public double getGyroAngleInRadians() {
		return getGyroAngle() * (Math.PI/180);
	}

	/**
	 * resets the left and right drive encoders;
	 * resets the gyro;
	 * stops the drivetrain
	 */
	public void reset() {
		disable();
		resetGyro();
		resetEncoders();
	}

	public void resetEncoders() {
		// zero the encoders
		leftDriveEncoder.reset();
		rightDriveEncoder.reset();
	}
	public void resetGyro() {
		if(!gyroDisabled)
			gyro.reset();
		else{
			System.err.println("Gyro is Disabled, Unable to Reset");
		}
	}
	
	public void calibrateGyro() {
		if(!gyroDisabled){
			System.out.println("Gyro calibrating");
			gyro.calibrate();
			System.out.println("Done calibrating " + " The current rate is " + gyro.getRate());
		}
	}

		
	
	//************** Pos/Vel/Acc Helper Methods **************// 
	
	/**
	 * @return distance traveled by left side based on encoder
	 */
	public double getLeftDistance() {
		return leftDriveEncoder.getDistance();
	}
	
	/**
	 * @return distance traveled by right side based on encoder
	 */
	public double getRightDistance() {
		return rightDriveEncoder.getDistance();
	}

	/**@return average distance of both encoder velocities */
	public double getAverageDist() {
//		return (getLeftDistance() + getRightDistance()) / 2;
//		left side commented out due to potential wiring issues
		return getLeftDistance();
	}
	
	public double getLeftVelocity() {
		return leftDriveEncoder.getRate();
	}

	public double getRightVelocity() {
		return rightDriveEncoder.getRate();
	}

	/**@return average velocity of both encoder velocities */
	public double getAverageVelocity() {
		return (getLeftVelocity() + getRightVelocity()) / 2;
	}
	
	public double getLeftAcceleration() {

		leftCurrentRate = getLeftVelocity();
		leftCurrentTime = Timer.getFPGATimestamp();

		double acceleration = (leftCurrentRate - leftPreviousRate) / (leftCurrentTime - leftPreviousTime);

		leftPreviousRate = leftCurrentRate;
		leftPreviousTime = leftCurrentTime;

		leftFilteredAccel = acceleration * 0.5 + leftFilteredAccel * 0.5;

		return leftFilteredAccel;
	}	

	public double getRightAcceleration() {

		rightCurrentRate = getLeftVelocity();
		rightCurrentTime = Timer.getFPGATimestamp();

		double acceleration = (rightCurrentRate - rightPreviousRate) / (rightCurrentTime - rightPreviousTime);

		rightPreviousRate = rightCurrentRate;
		rightPreviousTime = rightCurrentTime;

		rightFilteredAccel = acceleration * 0.5 + rightFilteredAccel * 0.5;

		return rightFilteredAccel;
	}	

	public double getAverageAcceleration() {

		currentRate = getAverageVelocity();
		currentTime = Timer.getFPGATimestamp();

		double acceleration = (currentRate - previousRate) / (currentTime - previousRateTime);

		previousRate = currentRate;
		previousRateTime = currentTime;

		return acceleration;
	}

	
	
	//*****************Debugging Methods*****************//

	/**
	 * @return total current usage for all 4 motors in the drivetrain
	 */
	public double getCurrentUsage() {
		return 0;//Robot.pdp.getCurrent(RobotMap.POWER_RIGHT_DRIVE_A) + Robot.pdp.getCurrent(RobotMap.POWER_RIGHT_DRIVE_B) + Robot.pdp.getCurrent(RobotMap.POWER_LEFT_DRIVE_A) + Robot.pdp.getCurrent(RobotMap.POWER_LEFT_DRIVE_B);
	}

}
