package cs311.hw4;

import java.util.Random;

public class SlowMatrixFactory implements IMeasureFactory{
	private boolean multiply;
	
	public SlowMatrixFactory(boolean multiply){
		this.multiply = multiply;
	}
	
	@Override
	public IMeasurable createRandom(int size) {
		SlowMatrix ranMatrix = new SlowMatrix(size, size, multiply);
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				ranMatrix.setElement(i, j, (new Random()).nextInt());
			}
		}
		return ranMatrix;
	}

}
