package com.markham.FaceCapture;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import org.openimaj.image.FImage;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.tracking.KLTHaarFaceTracker;
import org.openimaj.image.processing.resize.ResizeProcessor;
import org.openimaj.math.geometry.shape.Rectangle;
import org.openimaj.video.Video;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.xuggle.XuggleVideo;

public class VideoFaceCapture {

	private static KLTHaarFaceTracker faceTracker = new KLTHaarFaceTracker(0);

	public static void main(String[] args) throws MalformedURLException {

		Video<MBFImage> video = new XuggleVideo(new File("/Users/ben/Downloads/Grumpout.mp4"));
		VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(video);
		faceTracker.setForceRetry(60);
		display.addVideoListener(new VideoDisplayListener<MBFImage>() {
			public void beforeUpdate(MBFImage frame) {

				FImage grey = frame.flatten();
				grey = ResizeProcessor.halfSize(grey);
				List<DetectedFace> faces = faceTracker.trackFace(grey);

				for (DetectedFace face : faces) {

					Rectangle bounds = face.getBounds();
					bounds.scale(2);
					frame.drawShape(face.getBounds(), 6, RGBColour.GREEN);

				}
			}

			public void afterUpdate(VideoDisplay<MBFImage> display) {
				System.out.println(display.getDroppedFrameCount());
				display.resetDroppedFrameCount();

			}
		});
	}

}
