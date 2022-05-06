package org.unclesniper.test;

public class RunAllTests {

	private static final Testable<Void> ALL_TESTS = TestBuilders.testsuite("RunAllTests", RunAllTests::new,
		TestBuilders.testcase("subtypes", RunAllTests::testSubtypes),
		TestBuilders.testcase("failure", RunAllTests::testFailure),
		TestBuilders.testcase("skip", RunAllTests::testSkip),
		TestBuilders.testcase("failWithStderr", RunAllTests::testFailWithStderr),
		TestBuilders.testcase("skipWithStdout", RunAllTests::testSkipWithStdout),
		TestBuilders.testcase("assertInlineEqualYes", RunAllTests::testAssertInlineEqualYes),
		TestBuilders.testcase("assertInlineEqualNo", RunAllTests::testAssertInlineEqualNo),
		TestBuilders.testcase("assertFlowEqualYes", RunAllTests::testAssertFlowEqualYes),
		TestBuilders.testcase("assertFlowEqualNo", RunAllTests::testAssertFlowEqualNo)
	);

	public static void main(String[] args) throws Exception {
		PrintStreamTestResultSink sink = new PrintStreamTestResultSink(System.out);
		sink.setShowSkipped(true);
		sink.setShowSkippedDetails(true);
		sink.setShowPassed(true);
		sink.setInternalFramePredicate((frame, qname, pname, uqname, method) ->
				qname.equals("org.unclesniper.test.Assume") || qname.equals("org.unclesniper.test.Assert"));
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

	private void testAssertInlineEqualYes() {
		Assert.assertThat(3, Assert.equalTo(3));
	}

	private void testAssertInlineEqualNo() {
		Assert.assertThat(3, Assert.equalTo(4));
	}

	private void testAssertFlowEqualYes() {
		Assert.assertThat(3).is(Assert.equalTo(3));
	}

	private void testAssertFlowEqualNo() {
		Assert.assertThat(3).is(Assert.equalTo(4));
	}

}
