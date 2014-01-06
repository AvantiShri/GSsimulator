package gayleshapely;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is used to specify school and student preferences.
 * @author avantis
 * @param <E> - E is the item that we are indicating preferences of - schools or students in our case.
 */
public class Preferences<E>{
	
	ArrayList<ArrayList<E>> ranksArray;
	HashMap<E,Integer> rankOfHashMap = new HashMap<E, Integer>();
	
	/**
	 * An array of sub-arrays, containing the items in order
	 * of preference. Tied items are part of the same sub-array.
	 * @param ranksArray
	 */
	public Preferences(ArrayList<ArrayList<E>> ranksArray) {
		this.ranksArray = ranksArray;
		if (ranksArray == null) {
			throw new RuntimeException("ranksArray should not be null");
		}
		if (ranksArray.size() == 0) {
			throw new RuntimeException("ranksArray has size 0");
		}
		
		for (int i = 0; i < ranksArray.size(); i++) {
			for (E item : ranksArray.get(i)) {
				rankOfHashMap.put(item,i);
			}
		}
	}
	
	/**
	 * This class can also be initialized by passing in an ItemRawRanks class,
	 * which can be used to extract the array of subarrays needed for initialization.
	 * @param itemRawRanks
	 */
	public Preferences(ItemRawRanks<E> itemRawRanks) {
		this(itemRawRanks.getRanksArray());
	}
		
	/**
	 * This function is 0 indexed
	 * @param item - the item to find the rank of
	 * @return the rank of the item, 0 indexed
	 */
	public Integer rankOf(E item) {
		if (rankOfHashMap.containsKey(item)) {
			return rankOfHashMap.get(item);
		} else {
			throw new RuntimeException("No such item "+item+" in preferences. Preferences are: "+this);
		}
	}
	
	/**
	 * 0 indexed ranks
	 * @param index - the index into the ranks array
	 * @return list of all items at that rank
	 */
	public ArrayList<E> atRank(int index) {
		if (index >= this.size()) {
			throw new RuntimeException("There are only "+this.size()+" items in the preferences; you asked for rank "+index+" - remember we are 0 indexed");
		} else {
			return this.ranksArray.get(index);
		}
	}
	
	/**
	 * Returns the items one rank lower than the input item. Returns null if input is least preferred item.
	 * @param item - the item that you want to know the next best alternative to.
	 * @return the next preferred item. Null if at bottom of list.
	 */
	public ArrayList<E> nextPreferred(E item) {
		Integer thisItemRank = this.rankOf(item);
		Integer nextItemRank = thisItemRank + 1;
		if (nextItemRank == this.size()) {
			return null;
		} else {
			return this.atRank(nextItemRank);
		}		
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ranksArray.size(); i++) {
			if (i > 0) {
				sb.append("; ");
			}
			sb.append(i+": "+ranksArray.get(i));
		}		
		return sb.toString();
	}
	
	public int size() {
		return this.ranksArray.size();
	}
	
	/**
	 * A class to represent the raw rank of an item as may be read in from an inputfile
	 * @author avantis
	 * @param <E> - the item type, in our case a school or a student.
	 */
	public static class ItemRawRank<E> {
		Integer rawRankNumber;
		E item;
		public ItemRawRank(E item, Integer rawRankNumber) {
			this.item = item;
			this.rawRankNumber = rawRankNumber;
		}
	}
	
	/**
	 * A class to represent the full set of raw item ranks as might be read in from an input file.
	 * Once the raw ranks are read in, this class can produce the array of subarrays needed to intialize Preferences.
	 * @author avantis
	 * @param <E> - the item type, in our case a school or student
	 */
	public static class ItemRawRanks<E> {
		Set<Integer> rawRankNumbers = new HashSet<Integer>();
		HashMap<Integer,Set<E>> itemsAtRawRank = new HashMap<Integer,Set<E>>();
		
		/**
		 * Add in the raw rank for a particular school or student, as read in from a text file.
		 * @param itemRawRank
		 */
		public void addItemRawRank(ItemRawRank<E> itemRawRank) {
			rawRankNumbers.add(itemRawRank.rawRankNumber);
			if (itemsAtRawRank.containsKey(itemRawRank.rawRankNumber) == false) {
				itemsAtRawRank.put(itemRawRank.rawRankNumber, new HashSet<E>());
			}
			itemsAtRawRank.get(itemRawRank.rawRankNumber).add(itemRawRank.item);
		}
		
		/**
		 * Once all the raw ranks are read in, figure out the order of items
		 * and any ties, and produce the array of arrays needed to initialize
		 * the Preferences class.
		 * @return
		 */
		public ArrayList<ArrayList<E>> getRanksArray() {
			List<Integer> rawRanksList = new ArrayList<Integer>();
			rawRanksList.addAll(rawRankNumbers);
			Collections.sort(rawRanksList);
			
			ArrayList<ArrayList<E>> arrToReturn = new ArrayList<ArrayList<E>>();			
			for (Integer rawRank : rawRanksList) {
				ArrayList<E> itemsAtThisRank = new ArrayList<E>();
				itemsAtThisRank.addAll(itemsAtRawRank.get(rawRank));
				arrToReturn.add(itemsAtThisRank);
			}
			
			return arrToReturn;
		}
	}
	
}
