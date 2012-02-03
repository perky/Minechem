package net.minecraft.minechem;

public class ElectrolysisRecipe {
	
	public boolean isRandom;
	public Molecule[] outputs;
	public double[] weights;
	
	public ElectrolysisRecipe(boolean isRandom, Molecule[] outputs, double[] weights) {
		this.isRandom = isRandom;
		this.outputs = outputs;
		this.weights = null;
		if(weights != null)
			this.weights = weights;
	}
	
}
