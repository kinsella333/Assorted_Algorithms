package cs311.hw4;
import java.util.ArrayList;
import java.util.Random;

public class SlowMatrixTest {

	public static void main(String[] args) {
		//Part 3
		SlowMatrix a = new SlowMatrix(2,2, true);
		for(int i = 0; i < a.rows; i++){
			for(int j = 0; j < a.columns; j++){
				int rand = Math.abs((new Random()).nextInt());
				a.setElement(i, j, rand);
			}
		}
		MeasureTimeComplexity t1 = new MeasureTimeComplexity();
		
		int norm1 = t1.normalize(a, 2);
		System.out.println(norm1);
		ArrayList<Result> list1 = (ArrayList<Result>) t1.measure(new SlowMatrixFactory(true), norm1, 2, 100, 1);
		for(int i = 0; i < list1.size(); i++){
			System.out.println(list1.get(i).getSize() + " " + list1.get(i).getTime() );
			
		}
		
		//Part 4
		SlowMatrix b = new SlowMatrix(2,2, false);
		for(int i = 0; i < b.rows; i++){
			for(int j = 0; j < b.columns; j++){
				int rand = Math.abs((new Random()).nextInt());
				b.setElement(i, j, rand);
			}
		}
		MeasureTimeComplexity t2 = new MeasureTimeComplexity();
		
		int norm2 = t2.normalize(b, 2);
		System.out.println(norm2);
		ArrayList<Result> list2 = (ArrayList<Result>) t2.measure(new SlowMatrixFactory(false), norm2, 2, 100, 1);
		for(int i = 0; i < list2.size(); i++){
			System.out.println(list2.get(i).getSize() + " " + list2.get(i).getTime() );
		}
	}

}
