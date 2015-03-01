package sk.fiit.testframework.worldrepresentation.models;

import sk.fiit.robocup.library.geometry.Vector3;

import sk.fiit.robocup.library.math.*;


public class BodyPart {

	private TransformationMatrix transformation;
		
	public BodyPart()
	{
		this.transformation=TransformationMatrix.getIndetity();
	}
	
	public void setTransformation(TransformationMatrix transformation) {
		this.transformation = transformation;
	}

	public TransformationMatrix getTransformation() {
		return transformation;
	}
	
	public Vector3 getTranslation()
	{
		return transformation.getTranslation();
	}
	
	public Vector3 getRotation()
	{
		return transformation.getRotation();
	}
}
