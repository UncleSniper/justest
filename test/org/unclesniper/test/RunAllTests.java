package org.unclesniper.test;

public class RunAllTests {

	private static final Testable<Void> ALL_TESTS = TestBuilders.testsuite("RunAllTests", RunAllTests::new,
		TestBuilders.testcase("subtypes", RunAllTests::testSubtypes),
		TestBuilders.testcase("failure", RunAllTests::testFailure),
		TestBuilders.testcase("skip", RunAllTests::testSkip),
		TestBuilders.testcase("failWithStderr", RunAllTests::testFailWithStderr),
		TestBuilders.testcase("skipWithStdout", RunAllTests::testSkipWithStdout)
	);

	public static void main(String[] args) throws Exception {
		PrintStreamTestResultSink sink = new PrintStreamTestResultSink(System.out);
		sink.setShowSkipped(true);
		sink.setShowSkippedDetails(true);
		sink.setShowPassed(true);
		Testable.run(null, sink, RunAllTests.ALL_TESTS);
		if(sink.getFailedCases() > 0)
			System.exit(1);
	}

	private void testSubtypes(TestContext context) {
		Assert.<SimpleTestsuite<RunAllTests>, Testable<RunAllTests>>assertSubtype();
	}

	private void testFailure() {
		Assert.fail("failing statically");
	}

	private void testSkip() {
		Assume.skip("skipping statically");
	}

	private void testFailWithStderr() {
		System.err.println("Failing now!");
		Assert.fail("dead");
	}

	private void testSkipWithStdout() {
		System.out.println("Bailing now!");
		Assume.skip("sad");
	}

}
