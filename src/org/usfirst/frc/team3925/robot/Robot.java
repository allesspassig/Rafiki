//         .----------------.  .----------------.  .----------------.  .----------------. 
//        | .--------------. || .--------------. || .--------------. || .--------------. |
//        | |    ______    | || |    ______    | || |    _____     | || |   _______    | |
//        | |   / ____ `.  | || |  .' ____ '.  | || |   / ___ `.   | || |  |  _____|   | |
//        | |   `'  __) |  | || |  | (____) |  | || |  |_/___) |   | || |  | |____     | |
//        | |   _  |__ '.  | || |  '_.____. |  | || |   .'____.'   | || |  '_.____''.  | |
//        | |  | \____) |  | || |  | \____| |  | || |  / /____     | || |  | \____) |  | |
//        | |   \______.'  | || |   \______,'  | || |  |_______|   | || |   \______.'  | |
//        | |              | || |              | || |              | || |              | |
//        | '--------------' || '--------------' || '--------------' || '--------------' |
//         '----------------'  '----------------'  '----------------'  '----------------' 
//2015 FRC Robotics competition

package org.usfirst.frc.team3925.robot;

import static org.usfirst.frc.team3925.robot.RobotMap.CAMERA_IP;
import static org.usfirst.frc.team3925.robot.RobotMap.DRIVE_LEFT_MOTOR;
import static org.usfirst.frc.team3925.robot.RobotMap.DRIVE_RIGHT_MOTOR;
import static org.usfirst.frc.team3925.robot.RobotMap.DRIVE_SOLENOID_A;
import static org.usfirst.frc.team3925.robot.RobotMap.DRIVE_SOLENOID_B;
import static org.usfirst.frc.team3925.robot.RobotMap.ELEVATOR_ENCODER_A;
import static org.usfirst.frc.team3925.robot.RobotMap.ELEVATOR_ENCODER_B;
import static org.usfirst.frc.team3925.robot.RobotMap.ELEVATOR_LEFT_TALON;
import static org.usfirst.frc.team3925.robot.RobotMap.ELEVATOR_RIGHT_TALON;
import static org.usfirst.frc.team3925.robot.RobotMap.ELEVATOR_SWITCH;
import static org.usfirst.frc.team3925.robot.RobotMap.INTAKE_ROLLER;
import static org.usfirst.frc.team3925.robot.RobotMap.INTAKE_VICTOR_LEFT;
import static org.usfirst.frc.team3925.robot.RobotMap.INTAKE_VICTOR_RIGHT;
import static org.usfirst.frc.team3925.robot.RobotMap.JOYSTICK_XBOX_DRIVER;
import static org.usfirst.frc.team3925.robot.RobotMap.JOYSTICK_XBOX_SHOOTER;
import static org.usfirst.frc.team3925.robot.RobotMap.LATCH_PORT;
import static org.usfirst.frc.team3925.robot.RobotMap.LEFT_DRIVE_ENCODER_A;
import static org.usfirst.frc.team3925.robot.RobotMap.LEFT_DRIVE_ENCODER_B;
import static org.usfirst.frc.team3925.robot.RobotMap.PCM_CAN_ID;
import static org.usfirst.frc.team3925.robot.RobotMap.RIGHT_DRIVE_ENCODER_A;
import static org.usfirst.frc.team3925.robot.RobotMap.RIGHT_DRIVE_ENCODER_B;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	int wait = 0;
	
	public static final double AUTONOMOUS_DISTANCE = 10;
	
//	AxisCamera camera;
	Drive drive;
	Elevator elevator;
	Intake intake;
	Latches latches;
	Rumble rumble;
	
	Joystick driverXbox;
	Joystick shooterXbox;
	ToggleButton gearToggle;
	ToggleButton manualElevatorToggle;
	Button zeroElevatorBtn;
	Button liftToteBtn;
	Button lowerToteBtn;
	
	private final double DEADZONE = 0.1;
	double leftDistanceDriven;
	double rightDistanceDriven;
	double leftSpeed;
	double rightSpeed;
	
	Image frame;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
    	
//    	camera = new AxisCamera(CAMERA_IP);
    	drive = new Drive(DRIVE_LEFT_MOTOR, DRIVE_RIGHT_MOTOR, PCM_CAN_ID, DRIVE_SOLENOID_A, DRIVE_SOLENOID_B, LEFT_DRIVE_ENCODER_A, LEFT_DRIVE_ENCODER_B, RIGHT_DRIVE_ENCODER_A, RIGHT_DRIVE_ENCODER_B);
    	latches = new Latches(LATCH_PORT);
    	elevator = new Elevator(ELEVATOR_LEFT_TALON, ELEVATOR_RIGHT_TALON, ELEVATOR_ENCODER_A, ELEVATOR_ENCODER_B, ELEVATOR_SWITCH, latches);
    	intake = new Intake(INTAKE_VICTOR_LEFT, INTAKE_VICTOR_RIGHT, INTAKE_ROLLER);
    	
    	shooterXbox = new Joystick(JOYSTICK_XBOX_SHOOTER);
    	driverXbox = new Joystick(JOYSTICK_XBOX_DRIVER);
    	gearToggle = new ToggleButton(driverXbox, 1);
    	manualElevatorToggle = new ToggleButton(shooterXbox, 8 /* start */);
    	zeroElevatorBtn = new Button(shooterXbox, 2);
    	liftToteBtn = new Button(shooterXbox, 1);
    	lowerToteBtn = new Button(shooterXbox, 4);
    	
    	leftDistanceDriven = 0;
    	rightDistanceDriven = 0;
    }

    public void autonomousInit() {
    	elevator.zeroElevator();
    	gearToggle.reset();
    	drive.resetLeftEncoder();
    	drive.resetRightEncoder();
    	elevator.liftStack();
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	leftDistanceDriven = drive.getLeftEncoder();
    	rightDistanceDriven = drive.getRightEncoder();
    	leftSpeed = (AUTONOMOUS_DISTANCE - leftDistanceDriven) / 6;
    	rightSpeed = (AUTONOMOUS_DISTANCE - rightDistanceDriven) / 6;
//    	if (leftSpeed > 1)
//    		leftSpeed = 1;
//    	if (rightSpeed > 1) 
//    		rightSpeed = 1;   Possibly don't need?
    	drive.setMotorOutputs(leftSpeed, rightSpeed);
    }
    
    /**
     * This function is called at the beginning of teleop
     */
    public void teleopInit() {
    	//elevator.zeroElevator();
    	gearToggle.reset();
    	manualElevatorToggle.reset();
    	//elevator.liftStack();
    	
    	drive.resetLeftEncoder();
    	drive.resetRightEncoder();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	drivePeriodic();
		SmartDashboard.putNumber("elevator height", elevator.getCurrentHeight());
    	elevatorPeriodic();
    	intakePeriodic();
//    	cameraPeriodic(); disabled, camera commented out
    	
    }
    
    public void cameraPeriodic() {
    	NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);
        if (isOperatorControl() && isEnabled()) {

//            camera.getImage(frame);
            NIVision.imaqDrawShapeOnImage(frame, frame, rect, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);

            CameraServer.getInstance().setImage(frame);
        }
    }
    
	/**
	 * Controls elevator based on user input
	 */
	private void elevatorPeriodic() {
		
		manualElevatorToggle.update();
		
		if (manualElevatorToggle.get()) {
			elevator.idle();
			
			double elevatorSpeed = -shooterXbox.getRawAxis(1);
			elevator.setElevatorSpeed(elevatorSpeed, true);
		} else {
			if (lowerToteBtn.get()) {
				elevator.lowerStack();
			}
			if (liftToteBtn.get()) {
				elevator.liftStack();
			}
			if (zeroElevatorBtn.get()) {
				SmartDashboard.putBoolean("zero'd", true);
				elevator.zeroElevator();
			}
			elevator.elevatorRun();
		}
	}
	
	private void intakePeriodic() {
		double intakeSpeed = shooterXbox.getRawAxis(2) - shooterXbox.getRawAxis(3);
		intake.setSpeed(intakeSpeed);
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
