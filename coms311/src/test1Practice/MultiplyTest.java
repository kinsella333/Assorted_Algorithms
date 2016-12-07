package test1Practice;

public class MultiplyTest {

	public static void main(String[] args) {
		MultiplyTest t = new MultiplyTest();
		System.out.println(t.multiply(3, 2, 45));

	}
	
	public int multiply(int y, int z, int c){
		if(z == 0) return 0;
		else{
			return multiply(c*y, z/c, c) + y*(z%c);
		}
	}

}
