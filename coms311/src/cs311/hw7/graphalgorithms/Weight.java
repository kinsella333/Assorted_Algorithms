package cs311.hw7.graphalgorithms;

public class Weight implements IWeight{
	private double weight;
	
	public Weight(double weight){
		this.weight = weight;
	}
	
	@Override
	public double getWeight() {
		return weight;
	}

}
