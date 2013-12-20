package gayleshapely;

import java.util.ArrayList;

/**
 * Utility functions, for when it's too much effort to object orient...
 * @author avantis
 */
public class Utility {
	
	/**
	 * returns a new array which has the order of inputArr jumbled up
	 * @param inputArr
	 * @return
	 */
	public static <E> ArrayList<E> randomise(ArrayList<E> inputArr) {
		return sampleWithoutReplacement(inputArr, inputArr.size());
	}
	
	/**
	 * Efficient sampling without replacement
	 * Think of it as working by making repeated swaps; each randomly selected item is swapped to the front of the array,
	 * and the subsequent selection are made from the portion of the array containing items that have not
	 * been picked yet...
	 * Does NOT modify the input array
	 * @param inputArr - array containing elements to sample without replacement from
	 * @param numToSample
	 * @return
	 */
	public static <E> ArrayList<E> sampleWithoutReplacement(ArrayList<E> inputArr, int numToSample) {
		
		ArrayList<E> inputArrCopy = new ArrayList<E>(inputArr); //don't want to permute the input
		if (numToSample > inputArr.size()) {
			throw new RuntimeException("Can't sample "+numToSample+" from "+inputArr.size()+" items without replacement");
		}
		ArrayList<E> outputArray = new ArrayList<E>();
		for (int i = 0; i < numToSample; i++) {
			int selectedIndex = randomIntBetween(i,inputArrCopy.size());
			outputArray.add(inputArrCopy.get(selectedIndex));
			inputArrCopy.add(selectedIndex, inputArrCopy.get(i));			
		}		
		return outputArray;
	}
	
	/**
	 * Returns a random integer in the range [min,max)
	 * @param min - inclusive
	 * @param max - not inclusive
	 * @return
	 */
	static int randomIntBetween(int min, int max) {
		return (int) Math.floor(Math.random()*(max-min)+min);
	}
	
}
