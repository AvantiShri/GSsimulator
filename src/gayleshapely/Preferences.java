package gayleshapely;

import java.util.ArrayList;
import java.util.HashMap;

public class Preferences<E>{
	
	ArrayList<ArrayList<E>> ranksArray;
	HashMap<E,Integer> rankOfHashMap = new HashMap<E, Integer>();
	
	public Preferences(ArrayList<ArrayList<E>> ranksArray) {
		if (ranksArray == null) {
			throw new RuntimeException("ranksArray should not be null");
		}
		if (ranksArray.size() == 0) {
			throw new RuntimeException("ranksArray has size 0");
		}
		
		for (int i = 0; i <= ranksArray.size(); i++) {
			for (E item : ranksArray.get(i)) {
				rankOfHashMap.put(item,i);
			}
		}
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
		for (int i = 0; i <= ranksArray.size(); i++) {
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
	

	
}
