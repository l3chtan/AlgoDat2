package ads2.ss15.cflp;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;


/**
 * Klasse zum Berechnen der L&ouml;sung mittels Branch-and-Bound.
 * Hier sollen Sie Ihre L&ouml;sung implementieren.
 */
public class CFLP extends AbstractCFLP {

	private CFLPInstance instance;
	private int upperBound;
	private int[] sol;
	private int numC, numF;
	private int[] shortest;
	private int cost;
	private int[] cheapest;
	private int[][] lst;
	private int[][] arr;
	private int counter;
	private int[][] bList;
	private int[] shortList;	
	private boolean flag;
	private boolean[] full;
	private int[] cList;


	public CFLP(CFLPInstance instance) {
		// TODO: Hier ist der richtige Platz fuer Initialisierungen
		this.instance = instance;
		numC = instance.getNumCustomers();
		numF = instance.getNumFacilities();
		this.upperBound = Integer.MAX_VALUE;
		setSolution(Integer.MAX_VALUE, initArray(instance.getNumCustomers()));
		sol =  initArray(instance.getNumCustomers());

		int counter = 0;
		flag = false;
		full = new boolean[numF];
		Arrays.fill(full,false);
		shortest = new int[numC];
		int len = 0; 
		// int[][] t = new int[numC][2];
		for(int i=0;i<numC;i++){
			len = Integer.MAX_VALUE;
			for(int j=0;j<numF;j++){
				if(instance.distance(j,i) < len){
					len = instance.distance(j,i);

				}
			}	

			shortest[i] = len;
		}
		

		lst = new int[numC][numF];

			for(int j=0;j<numC;j++){
				for(int i=0;i<numF;i++){
					lst[j][i] = instance.distances[i][j];
				}
			}


		cheapest = new int[numF];
		int[][] near = new int[numC][numF];
		int[][] t = new int[numF][2];
		for(int i=0;i<numC;i++){
			for(int j=0;j<numF;j++){
			t[j][0] = j;
			t[j][1] = lst[i][j];
		}
		Arrays.sort(t, new Comparator<int[]>() {
		    @Override
		    public int compare(int[] o1, int[] o2) {
		        return Integer.compare(o2[1], o1[1]);
		    }
		});
		for(int k=0;k<numF;k++){
			cheapest[k] = t[k][0];
		}
		near[i] = cheapest;
		}
		lst = near;



		arr = new int[numF][numC];	

		int[][] custs = new int[numC][2];
		for(int j=0;j<numF;j++){
		for(int i=0;i<numC;i++){
			custs[i][0] = i;
			custs[i][1] = instance.distances[j][i];
		}

		Arrays.sort(custs, new Comparator<int[]>() {
		    @Override
		    public int compare(int[] o1, int[] o2) {
		        return Integer.compare(o1[1], o2[1]);
		    }
		});
			for(int m=0;m<numC;m++){
			arr[j][m] = custs[m][0];
		}
			}


		bList = new int[numC][3];
		shortList = new int[numC];

		for(int i=0;i<numC;i++){
			bList[i][0] = i;
			bList[i][1] = instance.bandwidthOf(i);
		}
		Arrays.sort(custs, new Comparator<int[]>() {
		    @Override
		    public int compare(int[] o1, int[] o2) {
		        return Integer.compare(o2[1], o1[1]);
		    }
		});
		for(int i=0;i<numC;i++){
			bList[i][2] = getMinF(bList[i][0]);
			shortList[i] = bList[i][0];
		}
		cost = value(sol,shortest,true);

		int ratio = numC/numF;
		System.out.println("ratio: "+ratio);
		cList = new int[numC];
		List<Integer> al = new ArrayList<Integer>();
		for(int i=0;i<numC;i++){
			al.add(i);
			cList[i] = i;
		}
		if(ratio <= 3){
		Collections.shuffle(al);
		for(int i=0;i<numC;i++){
			cList[i] = al.get(i);
		}
		}
	}

	private int getMinF(int n){
		int cnt = Integer.MAX_VALUE;
		int idx = 0;
		for(int i=0;i<numF;i++){
			if(instance.distances[i][n] < cnt){
				cnt = instance.distances[i][n];
				idx = i;
			}
		}
		return idx;
	}

	/**
	 * Diese Methode bekommt vom Framework maximal 30 Sekunden Zeit zur
	 * Verf&uuml;gung gestellt um eine g&uuml;ltige L&ouml;sung
	 * zu finden.
	 * 
	 * <p>
	 * F&uuml;gen Sie hier Ihre Implementierung des Branch-and-Bound Algorithmus
	 * ein.
	 * </p>
	 */
	@Override
	public void run(){
		branch(0, sol,0);	
		System.out.println("####### " + counter + " #######");
	}


	private int[] initArray(int length){
		int[] array = new int[length];
		Arrays.fill(array,-1);
		return array;
	}

	
	private void branch(int facility, int[] solution, int idx){
		// System.out.println(idx);

		for(int i=0;i<numF;i++){
			if(!flag){
				if(!full[i]){
					if(idx < numC)
					bound(i,solution,idx);
				}
			}else {
				flag = false;
				return;
			}
	 		// if(notTooMany(solution) && bandOK(solution))
	 	// 	if(idx < numC-1){
	 	// 		if(solution[arr[i][idx]] == -1){
			// 	System.out.println("i: "+i+" - idx: "+ arr[i][idx]);
			// 	bound(i/*lst[idx][i]*/, solution, idx);	
			// 	}else{
			// 		while( idx < numC-1 && solution[arr[i][++idx]] != -1){
			// 		}
			// 	System.out.println("??? i: "+i+" - idx: "+ arr[i][idx]);
			// 		bound(i,solution, idx);
			// }
			// }
		}
	}

	private void bound(int facility, int[] solution, int pos){
		counter++;
		// System.out.println("--- " +idx+" ---");
		// for(int j=0;j<numC;j++){
		// 	System.out.println("idx: "+j+" - solution: " + solution[j]);
		// }
		
		if(!cont(-1,solution)/* && notTooMany(solution) && bandOK(solution)*/){
			flag = true;
			if(setSolution(cost, solution.clone())){
				upperBound = cost; 
				// if(upperBound <= instance.getThreshold()){
				// 	flag = true;	
				// }
			}
			if(pos < numC)
			solution[cList[pos]] = -1;
			return;
		}

		int cC = currC(solution, facility);
		if(checkBandwidth(facility, solution)+instance.bandwidthOf(cList[pos]) <= instance.maxBandwidth && 
			instance.maxCustomersFor(facility)>cC){
			solution[cList[pos]] = facility;
			cost = value(solution,shortest,true)+ instance.calcObjectiveValue(solution);

			if(cost >= upperBound){
				solution[cList[pos]] = -1;
				return;
			}
			// System.out.println("lower: "+cost + " - upper: "+ upperBound);
			branch(facility, solution, ++pos);
			full[facility] = false;
			if(pos < numC)
				solution[cList[pos]] = -1;
		return;

		}
		if(cC >= instance.maxCustomersFor(facility))
			full[facility] = true;
	}

	// private int nearest(int[] a, int n){
	// 	int[] k = instance.distances[n];
	// 	int p = Integer.MAX_VALUE;
	// 	int h = 0;
	// 	boolean flag = false;

	// 	while(!flag){
	// 	for(int i=h;i< numC;i++){
	// 		if(k[i] <p){
	// 			h = i;
	// 		}
	// 	}
	// 	if(a[h] == -1){
	// 		flag = true;	
	// 	}
	// }
	// 	return h;
	// }

	// private boolean bandOK(int[] a){
	// 	for(int i=0;i<numF;i++){

	// 		if(checkBandwidth(i, a) > instance.maxBandwidth){
	// 			return false;
	// 		}
	// 	}
	// 	return true;
	// }

	// private boolean notTooMany(int[] a){
	// 	int[] f = new int[numF];
	// 	Arrays.fill(f,0);
	// 	for(int i:a){
	// 		if(i >= 0)
	// 		f[i]++;
	// 	}
	// 	for(int i=0;i<numF;i++){
	// 		if(f[i] > instance.maxCustomersFor(i)){
	// 			return false;
	// 		}
	// 	}
	// 	return true;
	// }

	private int currC(int[] a,int f){
		int cnt = 0;
		for(int i:a){
			if(i == f){
				cnt++;
			}
		}
		return cnt;
	}

	private int value(int[] a, int[] b, boolean flag){
		int result = 0;
		for(int i=0;i<a.length;i++){
			if(a[i] == -1){
				result += instance.distanceCosts*b[i];
			} else if(!flag && a[i] != -1){
				result += instance.distanceCosts*b[i];
			}
		}	
		return result;
	}
	// private int getbList(int i){
	// 	for(int[] k: bList){
	// 		if(k[0] == i){
	// 			return k[2];
	// 		}
	// 	}
	// 	return 0;
	// }

	private int checkBandwidth(int facility, int[] a){
		int cnt = 0;
		for(int i=0;i<a.length;i++){
			if(a[i] == facility){
				cnt += instance.bandwidthOf(i);
			}
		}
		return cnt;
	}

	private boolean cont(int k, int[] a){
		for(int i:a){
			if(i == k){
				return true;
			}
		}
		return false;
	}
}
