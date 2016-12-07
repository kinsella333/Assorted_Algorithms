package test1Practice;

import java.util.Random;

public class HeapSort {
	private int N;
	
	public HeapSort(int n){
		this.N = n;
	}
	
	public void sort(int A[]){
		heapify(A);
		for(int i = N; i > 0; i--){
			swap(A, 0, i); 
			N = N - 1;
			maxHeap(A,0);
		}
	}
	public void heapify(int[] A){
		N = A.length - 1;
		for(int i = N/2; i >= 0; i--){
			maxHeap(A, i);
		}
	}
	public void maxHeap(int[] A, int i){
		int left = 2*i;
		int right = 2*i + 1;
		int max = i;
		
		if(left <= N && A[left] > A[i]){
			max = left;
		}
		if(right <= N && A[right] > A[max]){
			max = right;
		}
		
		if(max != i){
			swap(A, i, max);
			maxHeap(A, max);
		}
	}
	public void swap(int A[], int i, int j){
		int temp = A[i];
		A[i] = A[j];
		A[j] = temp;
	}
	public static void main(String[] args){
		int[] A = new int[5];
		for(int i = 0; i < A.length; i++){
			A[i] = Math.abs((new Random()).nextInt()%50);
		}
		for(int i = 0; i < A.length; i++){
			System.out.print(A[i] + ",");
		}
		int[] B = A;
		HeapSort t = new HeapSort(A.length);
		t.sort(A);
		System.out.println("\n");
		for(int i = 0; i < A.length; i++){
			System.out.print(A[i] + ",");
		}
	
    }
}
