package test;

import junit.framework.Test;
import junit.framework.TestSuite;
import wordcount.WordCountsForDocsMapperTest;
import wordcount.WordCountsForDocsReducerTest;
import wordfreq.WordFreqMapperTest;
import wordfreq.WordFreqReducerTest;

public final class AllTests {

	  private AllTests() { }
	  
	  public static Test suite() {
	    TestSuite suite = new TestSuite("Tests for the TF-IDF algorithm");
	 
	    suite.addTestSuite(WordFreqMapperTest.class);
	    suite.addTestSuite(WordFreqReducerTest.class);
	    suite.addTestSuite(WordCountsForDocsMapperTest.class);
	    suite.addTestSuite(WordCountsForDocsReducerTest.class);
	 
	    return suite;
	  }
}
