package org.usfirst.frc.team2791.robot.commands;

import org.usfirst.frc.team2791.robot.OI;
import org.usfirst.frc.team2791.robot.Robot;
import org.usfirst.frc.team2791.robot.commands.*;
import org.usfirst.frc.team2791.robot.ShakerJoystick.*;
import org.usfirst.frc.team2791.robot.subsystems.*;
import org.usfirst.frc.team2791.robot.util.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Timer;
//Right now it the intake automatically stops rolling once the current spike is exceeded. What it should do is once the current spike starts, wait .5 seconds, and check if the cube is in through the current sensor. If it is, stop the roller and if it is not, run one roller faster than the other for .25 seconds and then check the sensor again.

public class IntakeOn extends Command{
	private Timer timer = new Timer();
	private boolean init = true; //Is true if the motors are still spinning up
	private boolean canIIntake = true; //Is true if the robot is allowed to intake
	private boolean initalCubeContact = true;
	private AnalogInput CubeSensor;
	private boolean amIJammed=false;
	private boolean lastSpin=false;
	private boolean doOnce=false;
	
	public IntakeOn(){
		requires(Robot.intake);
		CubeSensor = new AnalogInput(0);
	}
	protected void initialize(){
		timer.stop();
		timer.reset();
		timer.start();
		init = true;
		initalCubeContact=true;
		canIIntake=true;
		amIJammed=false;
		lastSpin=false;
		doOnce=false;
	}
	protected void finalSpin() {
		
	}
	protected void execute(){
		if(doOnce==true) {
			timer.stop();
			timer.reset();
			timer.start();
			doOnce=false;
		}
		if(canIIntake==true&&amIJammed==false) { //If I'm allowed to intake
		Robot.intake.motorOnIntakeA(); //Run intakes A and B
		Robot.intake.motorOnIntakeB();
		}
		else if(canIIntake==false&&lastSpin==false) {
			Robot.intake.motorOffIntakeA(); //Run intakes A and B
			Robot.intake.motorOffIntakeB();
		}
		else if(canIIntake==false&&lastSpin==true) {
			if(timer.get()>=0.1) {
			lastSpin=false;	
			}
			else {
			Robot.intake.motorOnIntakeA(); //Run intakes A and B
			Robot.intake.motorOnIntakeB();
			}
		}
		if(timer.get()>Robot.intake.getCurrentSpikeTimer() && init==true) { //If the timer is greater than the current spike cutoff AND i'm still spinning up my motors
			init=false; //I'm no longer spinning up my motors
			timer.stop(); //Reset the timer to 0
			timer.reset();
			timer.start();
		}
		SmartDashboard.putNumber("IntakeA Powah", Robot.intake.getCurrentUsageA());
		SmartDashboard.putNumber("IntakeB Powah", Robot.intake.getCurrentUsageB());
		SmartDashboard.putBoolean("SensorValue", getSensorReading());
		SmartDashboard.putBoolean("initialCubeContact", initalCubeContact);
		SmartDashboard.putBoolean("CanIIntake", canIIntake);
		//SmartDashboard.putBoolean("init", init); //For debugging
		//SmartDashboard.putNumber("Timer", timer.get()); //For debugging
		if(DoIHaveCube()==true) { //As SOON AS I CONTACT THE CUBE
			if(initalCubeContact==true) {
				//Runs only the first time I touch the cube
				timer.stop();
				timer.reset();
				timer.start();
				initalCubeContact=false;
			}
			if(getSensorReading() && timer.get()>=Robot.intake.getTimeToFullyIntake()&&initalCubeContact==false) {
				//THE CUBE IS PROPERLY INTAKED AND EVERYTHING IS GOOD
				//getSensorReading()<=Robot.intake.getSensorCutoff() might be >= idrk
				canIIntake=false;
				lastSpin=true;
				doOnce=true;
		}
			else if(getSensorReading() == false && timer.get() >= Robot.intake.getTimeToFullyIntake() && initalCubeContact==false){
				//THE CUBE IS JAMMED
				//getSensorReading()<=Robot.intake.getSensorCutoff() might be >= idrk
				amIJammed = true;
				Robot.intake.motorOnIntakeA();
				Robot.intake.motorOffIntakeB();
				
			}
		}
	}
	protected boolean DoIHaveCube() { //Returns true AS SOON AS I CONTACT A CUBE
		if(init==false) { //In order to check that I have a cube, first make sure I'm not still spinning up the motors
			if((Robot.intake.getCurrentUsageA()>Robot.intake.getCurrentLimit() || Robot.intake.getCurrentUsageB()>=Robot.intake.getCurrentLimit())) {
			return true;	
			}
			}
		return false;
	}
	protected boolean getSensorReading() { 
		if(CubeSensor.getValue() <= Robot.intake.getSensorCutoff()) { //0 - 4096
			return true;
		}
		return false;
		}
	protected boolean isFinished(){
		return false;
	}
	protected void end(){
		timer.stop();
		timer.reset();
		Robot.intake.motorOffIntakeA();
		Robot.intake.motorOffIntakeB();
		SmartDashboard.putNumber("IntakeA Powah", Robot.intake.getCurrentUsageA());
		SmartDashboard.putNumber("IntakeB Powah", Robot.intake.getCurrentUsageB());
	}
	protected void interrupted(){
		end();
	}
}