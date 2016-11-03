package com.markham.FaceCapture;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.keypoints.FKEFaceDetector;
import org.openimaj.image.processing.face.detection.keypoints.FacialKeypoint;
import org.openimaj.image.processing.face.detection.keypoints.KEDetectedFace;
import org.openimaj.math.geometry.point.Point2d;
import org.openimaj.math.geometry.shape.Rectangle;

public class App {

	public static void main(String[] args) throws IOException, InterruptedException {
		MBFImage image = ImageUtilities.readMBF(new File("/Users/ben//Downloads/issuesphoto.jpg"));
		DisplayUtilities.display(image);

		// Use KEDectectedFace for key facial features
		FaceDetector<KEDetectedFace, FImage> fd = new FKEFaceDetector(0);
		List<KEDetectedFace> faces = fd.detectFaces(Transforms.calculateIntensity(image));

		// Loop over the detected faces
		for (KEDetectedFace face : faces) {
			image.drawShape(face.getBounds(), 2, RGBColour.RED);

			// Put the keypoints in an array for loop
			FacialKeypoint[] keypoints = face.getKeypoints();

			for (FacialKeypoint key : keypoints) {

				// Find the position of the key points relative to
				// face.getBounds()
				Point2d point = key.position;

				// Find the poistion of the key points relative to the picture
				point.translate(face.getBounds().x, face.getBounds().y);

				// Create a shape to draw on the face
				Rectangle rec = new Rectangle(point.getX(), point.getY(), 2, 2);
				image.drawShape(rec, 2, RGBColour.GREEN);

			}

			// Crop the image of the face
			Point2d pt = face.getBounds().calculateCentroid();

			MBFImage sub = image.extractCentreSubPix(pt, (int) face.getBounds().getWidth(),
					(int) face.getBounds().getHeight());
			DisplayUtilities.display(sub);
		}

	}
}
