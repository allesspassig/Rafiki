package org.usfirst.frc.team3925.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.AxisCamera;

public class Camera {
	
	AxisCamera axisCamera;
	CameraServer cameraServer;
	Image frame;
	
	public Camera(String ip) {
		axisCamera = new AxisCamera(ip);
		CameraServer.getInstance();
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
	}
	
	public void updateImage() {
		if (axisCamera.isFreshImage()) {
			axisCamera.getImage(frame);
			cameraServer.setImage(frame);
		}
	}
	
}
