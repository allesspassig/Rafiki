package org.usfirst.frc.team3925.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;

public class Drive {
	
	RobotDrive drive;
	DoubleSolenoid shifter;
	Encoder leftEncoder;
	Encoder rightEncoder;
	
	public static final double LOW_GEAR_COEFFICIENT = 0.5;
	
	public Drive(int leftmotorport, int rightmotorport, int pcmCanId, int solenoidportA, int solenoidportB, int leftencoderportA, int leftencoderportB, int rightencoderportA, int rightencoderportB) {
		drive = new RobotDrive(leftmotorport, rightmotorport);
		shifter = new DoubleSolenoid(pcmCanId, solenoidportA, solenoidportB);
		leftEncoder = new Encoder(leftencoderportA, leftencoderportB);
		rightEncoder = new Encoder(rightencoderportA, rightencoderportB);
		leftEncoder.setDistancePerPulse(4.25/2048);
		rightEncoder.setDistancePerPulse(4.25/2048);
	}
	
	public void drive(double fwd, double turn, boolean gear) {
		drive.setMaxOutput(gear ? 1 : LOW_GEAR_COEFFICIENT);

		shifter.set(gear ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
		drive.arcadeDrive(fwd, turn);
	}
	
	public void setMotorOutputs(double leftSpeed, double rightSpeed) {
		drive.setLeftRightMotorOutputs(leftSpeed, rightSpeed);
	}
	
	public double getLeftEncoder() {
		return leftEncoder.getDistance();
	}
	
	public double getRightEncoder() {
		return rightEncoder.getDistance();
	}
	
	public void resetLeftEncoder() {
		leftEncoder.reset();
	}
	
	public void resetRightEncoder() {
		rightEncoder.reset();
	}
}
