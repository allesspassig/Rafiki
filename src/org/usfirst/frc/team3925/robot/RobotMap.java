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
			ELEVATOR_ENCODER_A = 4,
			ELEVATOR_ENCODER_B = 5,
			ELEVATOR_SWITCH = 7,
			INTAKE_VICTOR_LEFT = 2,
			INTAKE_VICTOR_RIGHT = 5,
			INTAKE_ROLLER = 7,
			LATCH_PORT = 0,
			JOYSTICK_XBOX_DRIVER = 0,
			JOYSTICK_XBOX_SHOOTER = 1;
	
}
