package cs311.hw4;

import java.util.ArrayList;
import java.util.List;

public class MeasureTimeComplexity implements IMeasureTimeComplexity{

	@Override
	public int normalize(IMeasurable m, long timeInMilliseconds) {
		double time = System.currentTimeMillis();
		int counter = 0;
		while((System.currentTimeMillis() - time) < timeInMilliseconds){
			m.execute();
			counter++;
		}
		return counter;
	}

	@Override
	public List<? extends IResult> measure(IMeasureFactory factory, int nmeasures, int startsize, int endsize,
			int stepsize) {
		List<Result> list = new ArrayList<Result>();
		while(startsize <= endsize){
			long time = System.currentTimeMillis();
			int counter = 0;
			SlowMatrix temp = (SlowMatrix) factory.createRandom(startsize);
			while(counter < nmeasures){
				temp.execute();
				counter++;
			}
			list.add(new Result(startsize, System.currentTimeMillis() - time));
			startsize += stepsize;
		}
		return list;
	}

}
