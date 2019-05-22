package com.wsi.data;

import java.util.Comparator;

/**
 * @author rames
 *
 *Order the ZipCodeRange in ascending order of zipCodeStart
 *Null is treated as the smallest element in ordering
 */
public class ZipCodeRangeComparator implements Comparator<ZipCodeRange> {

	@Override
	public int compare(ZipCodeRange o1, ZipCodeRange o2) {
		int o1Start = o1!= null ? o1.getZipCodeStart() : -1;
		int o2Start = o2!= null ? o2.getZipCodeStart() : -1;
		
		if (o1Start > o2Start)
			return 1;
		if (o1Start < o2Start)
			return -1;

		return 0;
	}

}
