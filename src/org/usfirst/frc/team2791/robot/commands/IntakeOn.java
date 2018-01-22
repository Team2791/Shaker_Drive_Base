package org.usfirst.frc.team2791.robot.commands;

import org.usfirst.frc.team2791.robot.OI;
import org.usfirst.frc.team2791.robot.Robot;
import org.usfirst.frc.team2791.robot.commands.*;
import org.usfirst.frc.team2791.robot.ShakerJoystick.*;
import org.usfirst.frc.team2791.robot.subsystems.*;
import org.usfirst.frc.team2791.robot.util.*;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Command;


public class IntakeOn extends Command{
	public IntakeOn(){
	}
	protected void initialize(){
	}
	
	protected void execute(){
		Robot.intake.motorOnIntakeA();
		}
	protected boolean isFinished(){
		return false;
	}
	protected void end(){
	}
	protected void interrupted(){
		end();
	}
}