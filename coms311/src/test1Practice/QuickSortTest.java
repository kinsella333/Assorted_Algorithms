package test1Practice;

import java.util.Random;

public class QuickSortTest {

	public static void main(String[] args) {
		int[] A = new int[5];
		for(int i = 0; i < A.length; i++){
			A[i] = Math.abs((new Random()).nextInt()%50);
		}
		for(int i = 0; i < A.length; i++){
			System.out.print(A[i] + ",");
		}
		int[] B = A;
		A = (new QuickSortTest()).quickSort(A);
		System.out.println("\n");
		for(int i = 0; i < A.length; i++){
			System.out.print(A[i] + ",");
		}
	}

	private int[] quickSort(int[] A){
		return quickSortRec(A, 0, A.length - 1);
	}
	
	private int[] quickSortRec(int[] A, int low, int high){
		if(low < high){
			int p = partition(A, low, high);
			quickSortRec(A, low, p);
			quickSortRec(A, p + 1, high);
		}
		return A;
	}
	
	private int partition(int[] A, int low, int high){
		int p = A[low];
		int i = low -1;
		int j = high + 1;
		int temp;
		
		while(true){
			do{
				i = i + 1;
			}while(A[i] < p);
			do{
				j = j - 1;
			}while(A[j] > p);
			
			if(i >= j){
				return j;
			}
			temp = A[j];
			A[j] = A[i];
			A[i] = temp;
		}
	}
}
