package ads2.ss15.cflp;

/**
 * Klasse zum Berechnen der L&ouml;sung mittels Branch-and-Bound.
 * Hier sollen Sie Ihre L&ouml;sung implementieren.
 */
public class CFLP extends AbstractCFLP {

	private CFLPInstance instance;
/*	private Facility[] facilities;
	public final int threshold;
	public final int distanceCosts;
	public int[] distances;*/

	private int[][] subSolution;	


	public CFLP(CFLPInstance instance) {
		// TODO: Hier ist der richtige Platz fuer Initialisierungen
		this.instance = instance;
		subSolution = new int[instance.getNumFacilities()][];
		for(int i=0;i<subSolution.length;i++){
				subSolution[i] = initArray(instance.getNumCustomers());
		}
/*		this.threshold = instance.getThreshold();
		this.distanceCosts = instance.distanceCosts;
		facilities = new Facility[instance.getNumFacilities()];
		for(Facility f: facilities){
			f = new Facility(instance.)
		}*/
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
	public void run() {
		// TODO: Diese Methode ist von Ihnen zu implementieren
		int[] check = branch(initArray(instance.getNumCustomers()));


	}

	private int[] branch(int[] check){
		int idx = -1, bwidth;
		for(int i=0;i<instance.getNumFacilities();i++){
			bwidth = -1;
			if(arraySum(subSolution) >= instance.maxBandwith || customerSum(subSolution) >= instance.maxCustomers[i]){
				return check;
			}
			for(int j=0;j<instance.getNumCustomers();j++){
				if(instance.bandwithOf(j) > bwidth && check[j] < 1){
					bwidth = instance.bandwithOf(j);
					idx = j;
				}
			}
			check[idx] = 1;
			subSolution[i][idx] = bwidth;
		}
		return branch(check);
	}

	private int customerSum(int[] array){
		int cnt = 0;
		for(int i=0;i<array.length;i++){
			if(array[i] > -1){
				cnt++;
			}
		}
		return cnt;
	}

	private int arraySum(int[] array){
		int cnt;
		for(int i=0;i<array.length;i++){
			if(array[i] > -1){
				cnt += array[i];
			}
		}
		return cnt;
	}

	private int[] initArray(int size){
		int[] newArray = new int[size];
		for(int i=0;i<size;i++){
			newArray[i] = -1;
		}
		return newArray;
	}
/*
	public class Facility {

		public final int maxBandwith;
		public final int maxCustomers;
		public final int openingCosts;

		public Facility(int maxBandwith, int maxCustomers, int openingCosts){
			this.maxBandwith = maxBandwith;
			this.maxCustomers = maxCustomers;
			this.openingCosts = openingCosts;
		}
	}

	public class Customer {

		private static int id;
		public int bandwith;

		public Customer(int bandwith){
			this.id++;
			this.bandwith = bandwith;
			this.distances = distances;
		}

		public getId(){
			return this.id;
		}
	}*/

	
	public class Facility {

		private int id;
		public final int maxCustomers;
		public final int openingCosts;
		private int usedBandwidth;
		private ArrayList<Customer> c;
		private int[] distance;

		public Facility(int maxCustomers, int openingCosts, int id){
			this.id = id;
			this.maxCustomers = maxCustomers;
			this.openingCosts = openingCosts;
			this.usedBandwidth = 0;
			c = new ArrayList<Customer>();
		}

		public boolean full(){
			return maxCustomers >= c.size() ? true : false;
		}

		public int getId(){
			return id;
		}

		public void setCustomer(Customer customer){
				// if(c.size() < maxCustomers && usedBandwidth+customer.getBandwidth() <= maxBandwith){
					c.add(customer);
					customer.setTo(id)	;
					usedBandwidth += customer.getBandwidth();
					// return true;
			// 	}
			// return false;
		}

		public Customer getCustomer(int i){
			return c.get(i);
		}
	}

	public class Customer {

		private int id;
		private int bandwith;
		/*private int[] distances;*/
		private  int fId;

		public Customer(int bandwith, int id){
			this.id = id;
			this.bandwith = bandwith;
			/*this.distances = distances*/;
			fId = -1;
		}

		public int getId(){
			return this.id;
		}

		public int getBandwidth(){
			return bandwith;
		}

		public void setTo(int id){
			fId = id;
		}

		public boolean isSet(){
			return fId > -1 ? true : false;
		}
}
	}
