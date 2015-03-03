package org.usfirst.frc.team3925.robot;

public final class RobotMap {
	// cannot create instances of this class
	private RobotMap() {}
	
	public static final String CAMERA_IP = "10.39.25.11";
	
	public static final int DRIVE_LEFT_MOTOR = 0,
			DRIVE_RIGHT_MOTOR = 1,
			PCM_CAN_ID = 1,
			DRIVE_SOLENOID_A = 0,
			DRIVE_SOLENOID_B = 1,
			ELEVATOR_LEFT_VICTOR = 3,
			ELEVATOR_RIGHT_VICTOR = 4,
			ELEVATOR_ENCODER_A = 0,
			ELEVATOR_ENCODER_B = 1,
			ELEVATOR_SWITCH = 2,
			INTAKE_VICTOR_LEFT = 2,
			INTAKE_VICTOR_RIGHT = 5,
			INTAKE_ROLLER = 7,
			LATCH_SOLENOID_A = 2,
			LATCH_SOLENOID_B = 3,
			JOYSTICK_XBOX_DRIVER = 0,
			JOYSTICK_XBOX_SHOOTER = 1;
	
}
