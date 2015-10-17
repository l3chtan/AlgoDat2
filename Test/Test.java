import java.util.Arrays;

public class Test{

	private static int[] nums = {2,4,2};
	private static int len = 3;
	private static int[] costs = {5,5,24,10,19,14,9,14};
	private static int cap = 39; 

	public static void main(String[] args){


		for(int j=0;j<3;j++){
			f1(null,j,0);
		}
	}

	private static int[] initA(){

		int[] a= new int[8];
		Arrays.fill(a,-1);
		return a;
	}

	private static void f1(int[] a, int num,int pos){
			for(int i=pos;i<8;i++){
				if(a == null){
				set(i,initA(),num);
			} else {
				set(i,a.clone(),num);
			}
			}
	}

	private static void set(int idx, int[] a, int num){

		if(a[idx] == -1){
			a[idx] = num;
		} else {
			return;
		}
		if(!capOK(a,num)){
			return;
		}
		if(aSet(a,num) < getMax(num)){
			f1(a,num,idx);
		} else  if(num < len-1){
			f1(a,++num,0);
		}
		if(full(a)){
			System.out.println("############");
			for(int i=0;i<8;i++){
				System.out.println("idx: " + i+ " - num: "+ a[i]);
			}
		}	
	}

	private static boolean capOK(int[] a, int num){
		int cnt = 0;
		for(int i=0; i<a.length;i++){
			if(a[i] == num){
				cnt += costs[i];
			}
		}
		if(cnt > cap){
			return false;
		}
		return true;
	}

	private static boolean full(int[] a){
		for(int i: a){
			if(i == -1){
				return false;
			}
		}
		return true;
	}

	private static int getMax(int idx){
		return nums[idx];
	}

	private static int aSet(int[] a, int idx){
		int cnt = 0;
		for(int i: a){
			if(i == idx){
				cnt++;
			}
		}
		return cnt;
	}

}