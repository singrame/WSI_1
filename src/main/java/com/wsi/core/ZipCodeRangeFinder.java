package com.wsi.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wsi.data.ZipCodeRange;
import com.wsi.data.ZipCodeRangeComparator;
import com.wsi.exception.InvalidInputException;

/**
 * @author rames
 *
 *         It finds unique zip code ranges from the given input set of zip code
 *         ranges It used a TreeSet to store the ranges in ascending order of
 *         their start range. The method findMinumRanges has the implementation
 *         for the same. See the documentation for the method for more details
 */
public class ZipCodeRangeFinder {

	private static final Logger logger = LogManager.getLogger(ZipCodeRangeFinder.class);

	private ZipCodeRangeComparator comparator = new ZipCodeRangeComparator();
	
	private static final Pattern ZIP_CODE_PATTERN = Pattern.compile("(\\[)(\\d{5})([ ]*,[ ]*)(\\d{5})(\\])");

	/**
	 * Returns set of zipcode non overlapping ranges from given set of zipcode
	 * ranges e.g. for input [94133,94133] [94200,94299] [94226,94399] returns
	 * [94133,94133] [94200,94399]
	 * 
	 * Throws InvalidInputException if unable to parse the input String. Valid
	 * format of input string is [94133,94133] [94200,94299] [94226,94399]
	 * 
	 * @param input
	 *            : set of zipcode ranges
	 * @return Set<ZipCodeRange> : set of non overlapping ranges
	 * @throws InvalidInputException
	 */
	public Set<ZipCodeRange> findMiniumRanges(String input) throws InvalidInputException {
		logger.info("Received Input :{}", input);
		if (input == null || input.trim().equals("")) {

			throw new InvalidInputException("Input String is empty");
		}
		input = input.replaceAll("\\][ ]\\[", "]\t[");

		Set<ZipCodeRange> existingRanges = new TreeSet<ZipCodeRange>(comparator);
		String[] rangeArray = input.split("\t");
		for (String elem : rangeArray) {
			if (elem == null || elem.trim().equals(""))
				throw new InvalidInputException("Input has multiple consecitive spaces");
			Matcher zipCodePatternMatcher = ZIP_CODE_PATTERN.matcher(elem);
			if (zipCodePatternMatcher.find()) {
				ZipCodeRange range = new ZipCodeRange(Integer.parseInt(zipCodePatternMatcher.group(2)),
						Integer.parseInt(zipCodePatternMatcher.group(4)));
				ZipCodeRange clonedRange = new ZipCodeRange(range.getZipCodeStart(), range.getZipCodeEnd());
				ZipCodeRange updatedRange = processZipCodeRange(clonedRange, existingRanges);
				if (updatedRange != null) {
					existingRanges.add(updatedRange);
				}
				logger.info("After processing Range {} Merged Ranges : {}", range, existingRanges);
			} else {
				throw new InvalidInputException("Input has invalid range : " + elem);
			}
		}
		return existingRanges;

	}

	/**
	 * This is the helper method, to adjust the ranges compiled so for with the
	 * next range from the input. 
	 * 
	 * @param currentRange		: ZipCode range processing 
	 * @param existingRanges	: Ordered Set of ZipCode Ranges (in ascending order of the start of the range)
	 * @return					: updated ZipCode Range based on the values in existingRanges set 
	 */
	private ZipCodeRange processZipCodeRange(ZipCodeRange currentRange, Set<ZipCodeRange> existingRanges) {
		List<ZipCodeRange> duplicateRanges = new ArrayList<>();

		for (ZipCodeRange existingRange : existingRanges) {
			if (currentRange.getZipCodeEnd() < existingRange.getZipCodeStart()
					|| currentRange.getZipCodeStart() > existingRange.getZipCodeEnd()) {
				logger.debug("Continue checking with the next exising Range as range {} is outside of {}", currentRange,
						existingRange);
				continue;
			}
			if (currentRange.getZipCodeStart() >= existingRange.getZipCodeStart()
					&& currentRange.getZipCodeEnd() <= existingRange.getZipCodeEnd()) {
				logger.debug("Ignoring current Range {} as covered in existing range {}", currentRange, existingRange);

				return null;
			}
			if (currentRange.getZipCodeStart() <= existingRange.getZipCodeStart()
					&& currentRange.getZipCodeEnd() >= existingRange.getZipCodeEnd()) {
				duplicateRanges.add(existingRange);
				logger.debug("Continue checking next elements of existing range with merged range {}", currentRange);
				continue;
			}
			if (currentRange.getZipCodeStart() < existingRange.getZipCodeStart()
					&& currentRange.getZipCodeEnd() < existingRange.getZipCodeEnd()) {
				duplicateRanges.add(existingRange);
				currentRange.setZipCodeEnd(existingRange.getZipCodeEnd());
				logger.debug("Continue checking next elements of existing range with merged range {}", currentRange);

				continue;
			}

			if (currentRange.getZipCodeStart() > existingRange.getZipCodeStart()
					&& currentRange.getZipCodeEnd() > existingRange.getZipCodeEnd()) {
				duplicateRanges.add(existingRange);
				currentRange.setZipCodeStart(existingRange.getZipCodeStart());
				logger.debug("Continue checking next elements of existing range with merged range {}", currentRange);
				continue;
			}

		}
		existingRanges.removeAll(duplicateRanges);
		return currentRange;
	}
}
