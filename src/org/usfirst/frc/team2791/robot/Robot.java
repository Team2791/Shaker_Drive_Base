package org.usfirst.frc.team2791.robot;

import org.usfirst.frc.team2791.robot.subsystems.ShakerDrivetrain;
import org.usfirst.frc.team2791.robot.subsystems.ShakerIntake;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;


/**
 * <b><i>Robot.java for a Shaker Drive Base.</i></b>
 * 
 * </p>
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *</p> 
 * <i>Feel free to email the authors for info on this package or any other aspect of the source code.</i>
 * </p>
 * @author Gaurab Banerjee: 
 * <a href="gaurab.banerjee97@gmail.com"> gaurab.banerjee97@gmail.com </a>
 */
public class Robot extends IterativeRobot {
	public static ShakerIntake intake;
	public static OI oi;
	public static GamePeriod gamePeriod;
	public static PowerDistributionPanel pdp; //CAN ID has to be 0 for current sensing
	public static ShakerDrivetrain drivetrain;

	/**
	 * setting autonomousCommand to a Command will cause that Command to run in autonomous init
	 */
	public Command autonomousCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		System.out.println("****************Starting to init my systems.*********************");
		
		gamePeriod = GamePeriod.DISABLED;

		pdp = new PowerDistributionPanel(RobotMap.PDP); //CAN id has to be 0
		drivetrain = new ShakerDrivetrain();

		oi = new OI();//OI has to be initialized after all subsystems to prevent startCompetition() error
		
		debug();

	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		gamePeriod = GamePeriod.DISABLED;
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		
		if(oi.driver.getButtonSt()){
			drivetrain.reset();
		}

		debug(); //allows us to debug (e.g. encoders and gyro) while disabled
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		
		drivetrain.reset();

		if (autonomousCommand != null)
			autonomousCommand.start();
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();

		debug();
	}

	@Override
	public void teleopInit() {

		if (autonomousCommand != null)
			autonomousCommand.cancel();

		gamePeriod = GamePeriod.TELEOP;
		
		drivetrain.resetEncoders();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		
		debug();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		Scheduler.getInstance().run();
		LiveWindow.run();
		
		debug();
	}

	public void debug() {
		drivetrain.debug();
	}

	public enum GamePeriod {
		AUTONOMOUS, TELEOP, DISABLED
	}
	
	/**
	 * Allows the trajectory auto to be automatically set a certain side (red or blue),
	 * and for the path to be reversed 
	 */
	public enum AutoMode {
		RED(1.0), BLUE(1.0), RED_REVERSED(-1.0), BLUE_REVERSED( -1.0);
		
		
		private double reversingConstant;
		
		AutoMode(double constant){
			this.reversingConstant = constant;
			
		}
		
		/**
		 * @return 1.0 = forward / -1.0 = reverse
		 */
		public double getConstant(){
			return this.reversingConstant;
			
		}
		
	}
	
}