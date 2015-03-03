package org.usfirst.frc.team3925.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

public class Elevator {
	
	private static final double TOLERANCE = 1;
	private static final double SPEED = 1;
	
	Victor leftElevatorMotor;
	Victor rightElevatorMotor;
	Encoder elevatorEncoder;
	DigitalInput limitSwitch;
	
	public Elevator(int leftelevatormotor, int rightelevatormotor, int encoderportA, int encoderportB, int limitswitchport) {
		leftElevatorMotor = new Victor(leftelevatormotor);
		rightElevatorMotor = new Victor(rightelevatormotor);
		elevatorEncoder = new Encoder(encoderportA, encoderportB);
		// TODO: set encoder to 1 inch per tick
		limitSwitch = new DigitalInput(limitswitchport);
	}
	
	public void setHeight(double height) {
		double current = elevatorEncoder.getDistance();
		if (Math.abs(height-current) > TOLERANCE) {
			if (current > height) {
				setElevatorSpeed(-SPEED);
			}else {
				setElevatorSpeed(SPEED);
			}
		}else {
			setElevatorSpeed(0);
		}
	}

	public void setElevatorSpeed(double speed) {
		leftElevatorMotor.set(speed);
		rightElevatorMotor.set(speed);
	}
	
}
