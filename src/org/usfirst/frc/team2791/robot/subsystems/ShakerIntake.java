package org.usfirst.frc.team2791.robot.subsystems;

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
		intakeSparkA.setSpeed(0.5);
		}
	public void motorOnIntakeB(){//negative is proper direction
		intakeSparkB.setSpeed(0.5);
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
	//public double getCurrentUsageA() {
		//return Robot.pdp.getCurrent(RobotMap.INTAKE_A);
	//}
	//public double getCurrentUsageB() {
		//return Robot.pdp.getCurrent(RobotMap.INTAKE_B);
	//}
	public void debug(){
		//SmartDashboard.putNumber("CurrentMotorA", getCurrentUsageA());
	}
	
}

