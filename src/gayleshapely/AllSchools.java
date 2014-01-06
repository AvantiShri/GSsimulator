package gayleshapely;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A class to specify the full set of schools relevant for a particular Gayle-Shapely run
 * @author avantis
 */
public class AllSchools implements Iterable<School> {
	Set<School> schoolSet = new HashSet<School>();	
	
	public void addSchool(School aSchool) {
		schoolSet.add(aSchool);
	}
	
	public boolean contains(School aSchool) {
		return schoolSet.contains(aSchool);
	}
	
	public int size() {
		return schoolSet.size();
	}
	
	@Override
	public Iterator<School> iterator() {
		return schoolSet.iterator();
	}
	
}
