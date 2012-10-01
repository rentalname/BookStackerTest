package name.hash.bookstacker.test;

import junit.framework.Test;
import android.test.suitebuilder.TestSuiteBuilder;

public class AllInstrumentationTests {

	public static Test suite() {
		return new TestSuiteBuilder(AllInstrumentationTests.class).includeAllPackagesUnderHere().build();
	}
}
