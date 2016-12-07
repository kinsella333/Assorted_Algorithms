package cs311.hw4;

public class Result implements IResult{
	
	private int size;
	private long time;
	
	public Result(int size, long time){
		this.size = size;
		this.time = time;
	}
	
	@Override
	public int getSize() {
		return this.size;
	}

	@Override
	public long getTime() {
		return this.time;
	}

}
