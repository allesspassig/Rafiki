//  .----------------.  .----------------.  .----------------.  .----------------.   .----------------.  .----------------.  .----------------.  .----------------. 
// | .--------------. || .--------------. || .--------------. || .--------------. | | .--------------. || .--------------. || .--------------. || .--------------. |
// | |  _________   | || |  _________   | || |      __      | || | ____    ____ | | | |    ______    | || |    ______    | || |    _____     | || |   _______    | |
// | | |  _   _  |  | || | |_   ___  |  | || |     /  \     | || ||_   \  /   _|| | | |   / ____ `.  | || |  .' ____ '.  | || |   / ___ `.   | || |  |  _____|   | |
// | | |_/ | | \_|  | || |   | |_  \_|  | || |    / /\ \    | || |  |   \/   |  | | | |   `'  __) |  | || |  | (____) |  | || |  |_/___) |   | || |  | |____     | |
// | |     | |      | || |   |  _|  _   | || |   / ____ \   | || |  | |\  /| |  | | | |   _  |__ '.  | || |  '_.____. |  | || |   .'____.'   | || |  '_.____''.  | |
// | |    _| |_     | || |  _| |___/ |  | || | _/ /    \ \_ | || | _| |_\/_| |_ | | | |  | \____) |  | || |  | \____| |  | || |  / /____     | || |  | \____) |  | |
// | |   |_____|    | || | |_________|  | || ||____|  |____|| || ||_____||_____|| | | |   \______.'  | || |   \______,'  | || |  |_______|   | || |   \______.'  | |
// | |              | || |              | || |              | || |              | | | |              | || |              | || |              | || |              | |
// | '--------------' || '--------------' || '--------------' || '--------------' | | '--------------' || '--------------' || '--------------' || '--------------' |
//  '----------------'  '----------------'  '----------------'  '----------------'   '----------------'  '----------------'  '----------------'  '----------------' 
//2015 FRC Robotics competition

package org.usfirst.frc.team3925.robot;

import static org.usfirst.frc.team3925.robot.RobotMap.CAMERA_IP;
import static org.usfirst.frc.team3925.robot.RobotMap.DRIVE_LEFT_MOTOR;
import static org.usfirst.frc.team3925.robot.RobotMap.DRIVE_RIGHT_MOTOR;
import static org.usfirst.frc.team3925.robot.RobotMap.DRIVE_SOLENOID_A;
import static org.usfirst.frc.team3925.robot.RobotMap.DRIVE_SOLENOID_B;
import static org.usfirst.frc.team3925.robot.RobotMap.ELEVATOR_ENCODER_A;
import static org.usfirst.frc.team3925.robot.RobotMap.ELEVATOR_ENCODER_B;
import static org.usfirst.frc.team3925.robot.RobotMap.ELEVATOR_LEFT_VICTOR;
import static org.usfirst.frc.team3925.robot.RobotMap.ELEVATOR_RIGHT_VICTOR;
import static org.usfirst.frc.team3925.robot.RobotMap.ELEVATOR_SWITCH;
import static org.usfirst.frc.team3925.robot.RobotMap.INTAKE_ROLLER;
import static org.usfirst.frc.team3925.robot.RobotMap.INTAKE_VICTOR_LEFT;
import static org.usfirst.frc.team3925.robot.RobotMap.INTAKE_VICTOR_RIGHT;
import static org.usfirst.frc.team3925.robot.RobotMap.JOYSTICK_XBOX_DRIVER;
import static org.usfirst.frc.team3925.robot.RobotMap.JOYSTICK_XBOX_SHOOTER;
import static org.usfirst.frc.team3925.robot.RobotMap.LATCH_SOLENOID_A;
import static org.usfirst.frc.team3925.robot.RobotMap.LATCH_SOLENOID_B;
import static org.usfirst.frc.team3925.robot.RobotMap.PCM_CAN_ID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	Camera camera;
	Drive drive;
	Elevator elevator;
	Intake intake;
	Latches latches;
	
	Joystick driverXbox;
	Joystick shooterXbox;
	ToggleButton gearToggle;
	
	private final double DEADZONE = 0.1;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	camera = new Camera(CAMERA_IP);
    	drive = new Drive(DRIVE_LEFT_MOTOR, DRIVE_RIGHT_MOTOR, PCM_CAN_ID, DRIVE_SOLENOID_A, DRIVE_SOLENOID_B);
    	elevator = new Elevator(ELEVATOR_LEFT_VICTOR, ELEVATOR_RIGHT_VICTOR, ELEVATOR_ENCODER_A, ELEVATOR_ENCODER_B, ELEVATOR_SWITCH);
    	intake = new Intake(INTAKE_VICTOR_LEFT, INTAKE_VICTOR_RIGHT, INTAKE_ROLLER);
    	latches = new Latches(PCM_CAN_ID, LATCH_SOLENOID_A, LATCH_SOLENOID_B);
    	
    	driverXbox = new Joystick(JOYSTICK_XBOX_DRIVER);
    	gearToggle = new ToggleButton(driverXbox, 1);
    	shooterXbox = new Joystick(JOYSTICK_XBOX_SHOOTER);
    }

    public void autonomousInit() {
    	
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }
    
    /**
     * This function is called at the beginning of teleop
     */
    public void teleopInit() {
    	gearToggle.reset();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	drivePeriodic();
    	elevatorPeriodic();
    	
    	if (driverXbox.getRawButton(2)) {
    		driverXbox.setRumble(Joystick.RumbleType.kLeftRumble, 1);
    	}else {
    		driverXbox.setRumble(Joystick.RumbleType.kLeftRumble, 0);
    	}
    	
    }

	private void elevatorPeriodic() {
		double elevatorSpeed = driverXbox.getRawAxis(2) - driverXbox.getRawAxis(3);
    	elevator.setElevatorSpeed(elevatorSpeed);
	}

	private void drivePeriodic() {
		double moveValue = driverXbox.getRawAxis(1);
    	double rotateValue = driverXbox.getRawAxis(4);
    	
    	if (Math.abs(moveValue) < DEADZONE)
    		moveValue = 0;
    	if (Math.abs(rotateValue) < DEADZONE)
    		rotateValue = 0;
    	
    	gearToggle.update();
    	boolean gear = gearToggle.get();
    	
		drive.drive(moveValue, rotateValue, gear);
	}
    
    @Override
    public void disabledInit() {
    	drive.drive(0, 0, gearToggle.get());
    }
    
}