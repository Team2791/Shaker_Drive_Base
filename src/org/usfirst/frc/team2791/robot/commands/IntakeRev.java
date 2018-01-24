package org.usfirst.frc.team2791.robot.commands;

import org.usfirst.frc.team2791.robot.OI;
import org.usfirst.frc.team2791.robot.Robot;
import org.usfirst.frc.team2791.robot.commands.*;
import org.usfirst.frc.team2791.robot.ShakerJoystick.*;
import org.usfirst.frc.team2791.robot.subsystems.*;
import org.usfirst.frc.team2791.robot.util.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Command;


public class IntakeRev extends Command{
	public IntakeRev(){
		requires(Robot.intake); //If this doesnt work remove me
	}
	protected void initialize(){
	}
	
	protected void execute(){
		Robot.intake.motorRevIntakeA();
		Robot.intake.motorRevIntakeB();
		SmartDashboard.putNumber("IntakeA Powah", Robot.intake.getCurrentUsageA());
		SmartDashboard.putNumber("IntakeB Powah", Robot.intake.getCurrentUsageB());
		}
	protected boolean isFinished(){
		return false;
	}
	protected void end(){
		Robot.intake.motorOffIntakeA();
		Robot.intake.motorOffIntakeB();
		SmartDashboard.putNumber("IntakeA Powah", Robot.intake.getCurrentUsageA());
		SmartDashboard.putNumber("IntakeB Powah", Robot.intake.getCurrentUsageB());
	}
	protected void interrupted(){
		end();
	}
}