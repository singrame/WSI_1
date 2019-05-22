package com.wsi.core;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.Test;

import com.wsi.data.ZipCodeRangeComparator;
import com.wsi.data.ZipCodeRange;

public class ZipCodeRangeFinderTest {
	private static final Logger logger = LogManager.getLogger(ZipCodeRangeFinderTest.class);
	private ZipCodeRangeComparator comparator = new ZipCodeRangeComparator();
	static {
		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
		File file = new File("src/test/resources/log4j2.xml");
		context.setConfigLocation(file.toURI());
	}

	@Test
	public void testCase1() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering testCase1()");
		}
		String input = "[94133,94133] [94200,94299] [94600,94699]";
		ZipCodeRangeFinder codeRageFinder = new ZipCodeRangeFinder();
		Set<ZipCodeRange> actualOutput = codeRageFinder.findMiniumRanges(input);
		int actualOutputCount = actualOutput.size();
		int expectedOutputCount = 3;
		assertEquals(String.format("Total Expected Count : %d, Total Actual Count : %d", expectedOutputCount, actualOutputCount)
				,expectedOutputCount, actualOutputCount);
		String expectedOutputString = "[94133, 94133] [94200, 94299] [94600, 94699]";
		String actualOutputString = actualOutput.stream().map(x -> x.toString()).collect(Collectors.joining(" "));
		
		assertEquals(String.format("Expected Output: %s, Actual Output: %s", expectedOutputString, actualOutputString)
				,expectedOutputString, actualOutputString);
		if (logger.isDebugEnabled()) {
			logger.debug("exiting testCase1()");
		}
	}

	@Test
	public void testCase3() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering testCase3()");
		}
		String input = "[49679, 52015] [49800, 50000] [51500, 53479] [45012, 46937] [54012, 59607] [45500, 45590] [45999, 47900] [44000, 45000] [43012, 45950]";
		ZipCodeRangeFinder codeRageFinder = new ZipCodeRangeFinder();
		Set<ZipCodeRange> actualOutput = codeRageFinder.findMiniumRanges(input);
		int actualOutputCount = actualOutput.size();
		int expectedOutputCount = 3;
		assertEquals(String.format("Total Expected Count : %d, Total Actual Count : %d", expectedOutputCount, actualOutputCount)
				,expectedOutputCount, actualOutputCount);
		String expectedOutputString = "[43012, 47900] [49679, 53479] [54012, 59607]";
		String actualOutputString = actualOutput.stream().map(x -> x.toString()).collect(Collectors.joining(" "));
		
		assertEquals(String.format("Expected Output: %s, Actual Output: %s", expectedOutputString, actualOutputString)
				,expectedOutputString, actualOutputString);
		if (logger.isDebugEnabled()) {
			logger.debug("exiting testCase3()");
		}
	}

	@Test
	public void testCase2() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("entering testCase2()");
		}
		String input = "[94133,94133] [94200,94299] [94226,94399]";
		ZipCodeRangeFinder codeRageFinder = new ZipCodeRangeFinder();
		Set<ZipCodeRange> actualOutput = codeRageFinder.findMiniumRanges(input);
		int actualOutputCount = actualOutput.size();
		int expectedOutputCount = 2;
		assertEquals(String.format("Total Expected Count : %d, Total Actual Count : %d", expectedOutputCount, actualOutputCount)
				,expectedOutputCount, actualOutputCount);
		String expectedOutputString = "[94133, 94133] [94200, 94399]";
		String actualOutputString = actualOutput.stream().map(x -> x.toString()).collect(Collectors.joining(" "));
		
		assertEquals(String.format("Expected Output: %s, Actual Output: %s", expectedOutputString, actualOutputString)
				,expectedOutputString, actualOutputString);
		if (logger.isDebugEnabled()) {
			logger.debug("exiting testCase2()");
		}
	}
}
