package org.usfirst.frc.team2791.robot.subsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PWM;
import org.usfirst.frc.team2791.robot.Robot;
import org.usfirst.frc.team2791.robot.RobotMap;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class corresponds to the combined intake/climber subsystem. There are two 775pros running off of two Sparks which
 * receive signal from a branched PWM. This motor output controls the intake belts and also the climbing axle. The system
 * has a ratcheting gear which is joined with a pall during climbing. The pall is activated with a piston. This piston is
 * controlled by the same solenoid that controls the wing expansions at the beginning of the game. The intake is also on 
 * two pistons which push it out to pick up balls.
 * 
 * @author team2791: See Robot.java for contact info
 */
public class ShakerIntake extends Subsystem {
	
	public Talon intakeSparkA;
	public Talon intakeSparkB;
	
	public ShakerIntake(){
		
		intakeSparkA = new Talon(RobotMap.INTAKE_SPARK_PORTA);
		intakeSparkB = new Talon(RobotMap.INTAKE_SPARK_PORTB);
	}
	
	public void initDefaultCommand(){}
	
	//***********Intake/Climber Helper Methods***********//

	/**
	 * @param inOrOut true = open intake to pull in balls / false = keep intake closed
	 */
	public void motorOnIntakeA(){//negative is proper direction
		intakeSparkA.setSpeed(getIntakeSpeed());
		}
	public double getCurrentSpikeTimer() {
		return SmartDashboard.getDouble("CurrentSpikeTimer", 0.5);
	}
	public double getTimeToFullyIntake() {
		return SmartDashboard.getDouble("TimeToFullyIntake", 0.5);
	}
	public double getReverseSpeed() {
		return SmartDashboard.getDouble("ReverseIntakeSpeed",-0.8);
	}
	public double getIntakeSpeed() {
		return SmartDashboard.getDouble("IntakeSpeed",0.5);
	}
	public double getCurrentLimit() {
		return SmartDashboard.getDouble("CurrentLimit",8.0);
	}
	public double getSensorCutoff() {
		return SmartDashboard.getDouble("SensorCutoff",1200);
	}
	public void motorOnIntakeB(){//negative is proper direction
		intakeSparkB.setSpeed(getIntakeSpeed());
	}
	public void motorRevIntakeA(){//negative is proper direction
		intakeSparkA.setSpeed(getReverseSpeed());
		}
	public void motorRevIntakeB(){//negative is proper direction
		intakeSparkB.setSpeed(getReverseSpeed());
	}
	public void motorOffIntakeA(){
		intakeSparkA.setSpeed(0.0);
	}
	public void motorOffIntakeB(){
		intakeSparkB.setSpeed(0.0);
	}
	
	//***********Debug Helper Methods***********//
	
	/**
	 * @return total intake/climber usage
	 */
	public double getCurrentUsageA() {
		return Robot.pdp.getCurrent(RobotMap.INTAKE_A);
	}
	public double getCurrentUsageB() {
		return Robot.pdp.getCurrent(RobotMap.INTAKE_B);
	}
	public void debug(){
	}
	
}
