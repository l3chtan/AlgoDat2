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
	private int[] longest;
	private int cost;
	private int counter;
	private int[] cList;


	public CFLP(CFLPInstance instance) {
		this.instance = instance;
		numC = instance.getNumCustomers();
		numF = instance.getNumFacilities();
		// this.upperBound = Integer.MAX_VALUE;
		setSolution(Integer.MAX_VALUE, initArray(instance.getNumCustomers()));
		sol =  initArray(instance.getNumCustomers());

		counter = 0;
		shortest = new int[numC];
		longest = new int[numC];
		int len = 0, maximum = 0;
		int f = 0;
		for(int i=0;i<numC;i++){
			len = Integer.MAX_VALUE;
			maximum = Integer.MIN_VALUE;
			for(int j=0;j<numF;j++){
				if(instance.distance(j,i) < len){
					len = instance.distance(j,i);
				} else  if(instance.distance(j,i) > maximum){
					maximum = instance.distance(j,i);
					f = j;
				}
			}	
			longest[i] = f;
			shortest[i] = len;
		}

		upperBound = instance.calcObjectiveValue(longest);

		cost = value(sol,shortest,true);

		cList = new int[numC];
		int[] tempo = new int[numC];
		for(int i=0;i<numC;i++){
			tempo[i] = instance.bandwidthOf(i);
			cList[i] = i;
		}
		System.out.println(numC/numF);
		if(numC/numF <4){
			int max = 0;
			int idx = 0;
			for(int i=0;i<numC;i++){
				max = Integer.MIN_VALUE;
				for(int j=0;j<numC;j++){
					if(tempo[j] != -1 && max < tempo[j]){
						max = tempo[j]; 
						idx = j;
					}
				}
				cList[i] = idx;
				tempo[idx] = -1;
			}
		}


		/* sort in random order */

		// int ratio = numC/numF;
		// cList = new int[numC];
		// List<Integer> al = new ArrayList<Integer>();
		// for(int i=0;i<numC;i++){
		// 	al.add(i);
		// 	cList[i] = i;
		// }
		// if(ratio < 3){
		// Collections.shuffle(al);
		// for(int i=0;i<numC;i++){
		// 	cList[i] = al.get(i);
		// 	System.out.print(cList[i]);
		// }
		// System.out.println();
		// }


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
		System.out.println(counter);
	}


	private int[] initArray(int length){
		int[] array = new int[length];
		Arrays.fill(array,-1);
		return array;
	}

	
	private void branch(int facility, int[] solution, int idx){

		for(int i=0;i<numF;i++){
					if(idx < numC)
						bound(i,solution,idx);
					else
						return;
		}
	}

	private void bound(int facility, int[] solution, int pos){
		counter++;
		

		if(checkBandwidth(facility, solution)+instance.bandwidthOf(cList[pos]) <= instance.maxBandwidth && 
			instance.maxCustomersFor(facility)>currC(solution, facility)){
			solution[cList[pos]] = facility;
			cost = value(solution,shortest,true)+ instance.calcObjectiveValue(solution);

			if(cost >= upperBound){
				solution[cList[pos]] = -1;
				return;
			}
			branch(facility, solution, ++pos);
			if(pos < numC)
				solution[cList[pos]] = -1;
		}

		if(!cont(-1,solution)){
			if(setSolution(cost, solution.clone())){
				upperBound = cost; 
			}
		}
	}

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
