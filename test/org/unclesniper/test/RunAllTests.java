package org.unclesniper.test;

public class RunAllTests {

	private static final Testable<Void> ALL_TESTS = TestBuilders.tests(
		TestBuilders.testsuite("RunAllTests", RunAllTests::new
			//TODO
		),
		LineSplitterTests.TESTSUITE
	);

	public static void main(String[] args) throws Exception {
		PrintStreamTestResultSink sink = new PrintStreamTestResultSink(System.out);
		sink.setShowSkipped(true);
		sink.setShowSkippedDetails(true);
		sink.setShowPassed(true);
		sink.setInternalFramePredicate((frame, qname, pname, uqname, method) ->
				qname.equals("org.unclesniper.test.Assume")
				|| qname.equals("org.unclesniper.test.Assert")
				|| qname.equals("org.unclesniper.test.matcher.Subject")
				|| qname.startsWith("org.unclesniper.test.matcher."));
		Testable.run(null, sink, RunAllTests.ALL_TESTS);
		if(sink.getFailedCases() > 0)
			System.exit(1);
	}

}
