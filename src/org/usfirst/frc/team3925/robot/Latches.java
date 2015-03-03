package org.usfirst.frc.team3925.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Latches {
	
	DoubleSolenoid latches;
	
	public Latches(int pcmcanid, int forwardsolenoidport, int reversesolenoidport) {
		latches = new DoubleSolenoid(pcmcanid, forwardsolenoidport, reversesolenoidport);
	}
	
	public void setLatches(boolean out) {
		latches.set(out ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
	}
	
}
