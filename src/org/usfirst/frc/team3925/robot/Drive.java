package org.usfirst.frc.team3925.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotDrive;

public class Drive {
	
	RobotDrive drive;
	DoubleSolenoid shifter;
	
	public Drive(int leftmotorport, int rightmotorport, int pcmCanId, int solenoidportA, int solenoidportB) {
		drive = new RobotDrive(leftmotorport, rightmotorport);
		shifter = new DoubleSolenoid(pcmCanId, solenoidportA, solenoidportB);
	}
	
	public void drive(double fwd, double turn, boolean gear) {
		drive.arcadeDrive(fwd, turn);
		shifter.set(gear ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
	}
}
