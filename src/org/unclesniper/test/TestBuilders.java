package org.unclesniper.test;

import java.util.Objects;
import java.util.function.Predicate;

public class TestBuilders {

	private TestBuilders() {}

	private static void enforceExpectedException(Throwable thrown,
			Predicate<? super Throwable> expectedException, String expectedExceptionName) {
		if(thrown == null) {
			if(expectedException == null)
				return;
			throw new ExpectedExceptionNotThrownError(expectedException, expectedExceptionName);
		}
		if(thrown instanceof AssertionFailureError)
			throw (AssertionFailureError)thrown;
		if(thrown instanceof AssumptionFailureError)
			throw (AssumptionFailureError)thrown;
		if(expectedException == null)
			throw new UnexpectedExceptionThrownError(thrown);
		if(!expectedException.test(thrown))
			throw new WrongExceptionThrownError(expectedException, expectedExceptionName, thrown);
	}

	public static <BaseT> Testcase<BaseT> discard(StaticTestcase testcase) {
		if(testcase == null)
			throw new IllegalArgumentException("Testcase must not be null");
		return (base, context) -> testcase.performTest(context);
	}

	public static <BaseT> Testcase<BaseT> discard(ContextlessTestcase<? super BaseT> testcase) {
		if(testcase == null)
			throw new IllegalArgumentException("Testcase must not be null");
		return (base, context) -> testcase.performTest(base);
	}

	public static <BaseT> Testcase<BaseT> discard(StaticContextlessTestcase testcase) {
		if(testcase == null)
			throw new IllegalArgumentException("Testcase must not be null");
		return (base, context) -> testcase.performTest();
	}

	public static <BaseT> ContextInitializer<BaseT> discard(ContextlessInitializer<? super BaseT> initializer) {
		if(initializer == null)
			throw new IllegalArgumentException("Initializer must not be null");
		return (context, base) -> initializer.initializeBase(base);
	}

	public static <ExceptionT extends Throwable> Predicate<ExceptionT> exception(Class<? extends ExceptionT> type,
			Predicate<String> message) {
		return thrown -> {
			if(thrown == null)
				throw new IllegalArgumentException("Exception cannot be null");
			if(type != null && !type.isInstance(thrown))
				return false;
			if(message != null && !message.test(thrown.getMessage()))
				return false;
			return true;
		};
	}

	public static <ExceptionT extends Throwable> Predicate<ExceptionT> exception(Class<? extends ExceptionT> type,
			String message) {
		return TestBuilders.exception(type, msg -> Objects.equals(msg, message));
	}

	public static <ExceptionT extends Throwable> Predicate<ExceptionT> exception(Class<? extends ExceptionT> type) {
		return TestBuilders.exception(type, (Predicate<String>)null);
	}

	public static <ExceptionT extends Throwable> Predicate<ExceptionT> exception(Predicate<String> message) {
		return TestBuilders.exception(null, message);
	}

	public static <ExceptionT extends Throwable> Predicate<ExceptionT> exception(String message) {
		return TestBuilders.exception(null, msg -> Objects.equals(msg, message));
	}

	public static Predicate<String> startsWith(String prefix) {
		if(prefix == null)
			throw new IllegalArgumentException("Prefix must not be null");
		return subject -> subject != null && subject.startsWith(prefix);
	}

	// ===== begin expect()

	// begin (testcase, expectedException, expectedExceptionName)

	public static <BaseT> Testcase<BaseT> expect(Testcase<? super BaseT> testcase,
			Predicate<? super Throwable> expectedException, String expectedExceptionName) {
		if(testcase == null)
			throw new IllegalArgumentException("Testcase must not be null");
		return (base, context) -> {
			Throwable thrown;
			try {
				testcase.performTest(base, context);
				thrown = null;
			}
			catch(Throwable t) {
				thrown = t;
			}
			TestBuilders.enforceExpectedException(thrown, expectedException, expectedExceptionName);
		};
	}

	public static StaticTestcase expect(StaticTestcase testcase,
			Predicate<? super Throwable> expectedException, String expectedExceptionName) {
		if(testcase == null)
			throw new IllegalArgumentException("Testcase must not be null");
		return context -> {
			Throwable thrown;
			try {
				testcase.performTest(context);
				thrown = null;
			}
			catch(Throwable t) {
				thrown = t;
			}
			TestBuilders.enforceExpectedException(thrown, expectedException, expectedExceptionName);
		};
	}

	public static <BaseT> ContextlessTestcase<BaseT> expect(ContextlessTestcase<? super BaseT> testcase,
			Predicate<? super Throwable> expectedException, String expectedExceptionName) {
		if(testcase == null)
			throw new IllegalArgumentException("Testcase must not be null");
		return base -> {
			Throwable thrown;
			try {
				testcase.performTest(base);
				thrown = null;
			}
			catch(Throwable t) {
				thrown = t;
			}
			TestBuilders.enforceExpectedException(thrown, expectedException, expectedExceptionName);
		};
	}

	public static StaticContextlessTestcase expect(StaticContextlessTestcase testcase,
			Predicate<? super Throwable> expectedException, String expectedExceptionName) {
		if(testcase == null)
			throw new IllegalArgumentException("Testcase must not be null");
		return () -> {
			Throwable thrown;
			try {
				testcase.performTest();
				thrown = null;
			}
			catch(Throwable t) {
				thrown = t;
			}
			TestBuilders.enforceExpectedException(thrown, expectedException, expectedExceptionName);
		};
	}

	// end (testcase, expectedException, expectedExceptionName)

	// begin (testcase, exceptionType, messagePredicate)

	public static <BaseT> Testcase<BaseT> expect(Testcase<? super BaseT> testcase,
			Class<? extends Throwable> exceptionType, Predicate<String> messagePredicate) {
		return TestBuilders.expect(testcase, TestBuilders.exception(exceptionType, messagePredicate),
				(exceptionType == null ? "exception" : exceptionType.getName())
				+ (messagePredicate == null ? "" : " matching predicate"));
	}

	public static StaticTestcase expect(StaticTestcase testcase,
			Class<? extends Throwable> exceptionType, Predicate<String> messagePredicate) {
		return TestBuilders.expect(testcase, TestBuilders.exception(exceptionType, messagePredicate),
				(exceptionType == null ? "exception" : exceptionType.getName())
				+ (messagePredicate == null ? "" : " matching predicate"));
	}

	public static <BaseT> ContextlessTestcase<BaseT> expect(ContextlessTestcase<? super BaseT> testcase,
			Class<? extends Throwable> exceptionType, Predicate<String> messagePredicate) {
		return TestBuilders.expect(testcase, TestBuilders.exception(exceptionType, messagePredicate),
				(exceptionType == null ? "exception" : exceptionType.getName())
				+ (messagePredicate == null ? "" : " matching predicate"));
	}

	public static StaticContextlessTestcase expect(StaticContextlessTestcase testcase,
			Class<? extends Throwable> exceptionType, Predicate<String> messagePredicate) {
		return TestBuilders.expect(testcase, TestBuilders.exception(exceptionType, messagePredicate),
				(exceptionType == null ? "exception" : exceptionType.getName())
				+ (messagePredicate == null ? "" : " matching predicate"));
	}

	// end (testcase, exceptionType, messagePredicate)

	// begin (testcase, exceptionType, message)

	public static <BaseT> Testcase<BaseT> expect(Testcase<? super BaseT> testcase,
			Class<? extends Throwable> exceptionType, String message) {
		return TestBuilders.expect(testcase, TestBuilders.exception(exceptionType, message),
				(exceptionType == null ? "exception" : exceptionType.getName())
				+ (message == null ? " with null message" : " with message \"" + message + '"'));
	}

	public static StaticTestcase expect(StaticTestcase testcase,
			Class<? extends Throwable> exceptionType, String message) {
		return TestBuilders.expect(testcase, TestBuilders.exception(exceptionType, message),
				(exceptionType == null ? "exception" : exceptionType.getName())
				+ (message == null ? " with null message" : " with message \"" + message + '"'));
	}

	public static <BaseT> ContextlessTestcase<BaseT> expect(ContextlessTestcase<? super BaseT> testcase,
			Class<? extends Throwable> exceptionType, String message) {
		return TestBuilders.expect(testcase, TestBuilders.exception(exceptionType, message),
				(exceptionType == null ? "exception" : exceptionType.getName())
				+ (message == null ? " with null message" : " with message \"" + message + '"'));
	}

	public static StaticContextlessTestcase expect(StaticContextlessTestcase testcase,
			Class<? extends Throwable> exceptionType, String message) {
		return TestBuilders.expect(testcase, TestBuilders.exception(exceptionType, message),
				(exceptionType == null ? "exception" : exceptionType.getName())
				+ (message == null ? " with null message" : " with message \"" + message + '"'));
	}

	// end (testcase, exceptionType, message)

	// begin (testcase, exceptionType)

	public static <BaseT> Testcase<BaseT> expect(Testcase<? super BaseT> testcase,
			Class<? extends Throwable> exceptionType) {
		return TestBuilders.expect(testcase, TestBuilders.exception(exceptionType),
				(exceptionType == null ? "exception" : exceptionType.getName()));
	}

	public static StaticTestcase expect(StaticTestcase testcase,
			Class<? extends Throwable> exceptionType) {
		return TestBuilders.expect(testcase, TestBuilders.exception(exceptionType),
				(exceptionType == null ? "exception" : exceptionType.getName()));
	}

	public static <BaseT> ContextlessTestcase<BaseT> expect(ContextlessTestcase<? super BaseT> testcase,
			Class<? extends Throwable> exceptionType) {
		return TestBuilders.expect(testcase, TestBuilders.exception(exceptionType),
				(exceptionType == null ? "exception" : exceptionType.getName()));
	}

	public static StaticContextlessTestcase expect(StaticContextlessTestcase testcase,
			Class<? extends Throwable> exceptionType) {
		return TestBuilders.expect(testcase, TestBuilders.exception(exceptionType),
				(exceptionType == null ? "exception" : exceptionType.getName()));
	}

	// end (testcase, exceptionType)

	// begin (testcase, messagePredicate)

	public static <BaseT> Testcase<BaseT> expect(Testcase<? super BaseT> testcase,
			Predicate<String> messagePredicate) {
		return TestBuilders.expect(testcase, TestBuilders.exception(messagePredicate),
				messagePredicate == null ? "exception" : "exception matching predicate");
	}

	public static StaticTestcase expect(StaticTestcase testcase,
			Predicate<String> messagePredicate) {
		return TestBuilders.expect(testcase, TestBuilders.exception(messagePredicate),
				messagePredicate == null ? "exception" : "exception matching predicate");
	}

	public static <BaseT> ContextlessTestcase<BaseT> expect(ContextlessTestcase<? super BaseT> testcase,
			Predicate<String> messagePredicate) {
		return TestBuilders.expect(testcase, TestBuilders.exception(messagePredicate),
				(messagePredicate == null ? "exception" : "exception matching predicate"));
	}

	public static StaticContextlessTestcase expect(StaticContextlessTestcase testcase,
			Predicate<String> messagePredicate) {
		return TestBuilders.expect(testcase, TestBuilders.exception(messagePredicate),
				messagePredicate == null ? "exception" : "exception matching predicate");
	}

	// end (testcase, messagePredicate)

	// begin (testcase, message)

	public static <BaseT> Testcase<BaseT> expect(Testcase<? super BaseT> testcase,
			String message) {
		return TestBuilders.expect(testcase, TestBuilders.exception(message),
				message == null ? "exception with null message" : "exception with message \"" + message + '"');
	}

	public static StaticTestcase expect(StaticTestcase testcase,
			String message) {
		return TestBuilders.expect(testcase, TestBuilders.exception(message),
				message == null ? "exception with null message" : "exception with message \"" + message + '"');
	}

	public static <BaseT> ContextlessTestcase<BaseT> expect(ContextlessTestcase<? super BaseT> testcase,
			String message) {
		return TestBuilders.expect(testcase, TestBuilders.exception(message),
				message == null ? "exception with null message" : "exception with message \"" + message + '"');
	}

	public static StaticContextlessTestcase expect(StaticContextlessTestcase testcase,
			String message) {
		return TestBuilders.expect(testcase, TestBuilders.exception(message),
				message == null ? "exception with null message" : "exception with message \"" + message + '"');
	}

	// end (testcase, message)

	// ===== end expect()

	public static <BaseT> ContextInitializer<BaseT> discardInit(ContextlessInitializer<? super BaseT> initializer) {
		if(initializer == null)
			throw new IllegalArgumentException("Initializer must not be null");
		return (context, base) -> initializer.initializeBase(base);
	}

	public static <BaseT> ContextInitializer<BaseT> discardInit(StaticContextInitializer initializer) {
		if(initializer == null)
			throw new IllegalArgumentException("Initializer must not be null");
		return (context, base) -> initializer.initializeEnvironment(context);
	}

	public static <BaseT> ContextInitializer<BaseT> discardInit(StaticContextlessInitializer initializer) {
		if(initializer == null)
			throw new IllegalArgumentException("Initializer must not be null");
		return (context, base) -> initializer.initializeEnvironment();
	}

	// ==== begin initializer()/finalizer()

	public static <BaseT> Initializer<BaseT> initializer(Initializer<? super BaseT> initializer, boolean required) {
		return new DelegatingInitializer<BaseT>(initializer, required);
	}

	public static <BaseT> Initializer<BaseT> finalizer(Initializer<? super BaseT> finalizer, boolean required) {
		return TestBuilders.initializer(finalizer, required);
	}

	// begin ContextInitializer

	public static <BaseT> Initializer<BaseT> initializer(ContextInitializer<? super BaseT> initializer,
			boolean required) {
		return new SimpleInitializer<BaseT>(initializer, required);
	}

	public static <BaseT> Initializer<BaseT> finalizer(ContextInitializer<? super BaseT> finalizer,
			boolean required) {
		return TestBuilders.initializer(finalizer, required);
	}

	public static <BaseT> Initializer<BaseT> initializer(ContextInitializer<? super BaseT> initializer) {
		return TestBuilders.initializer(initializer, true);
	}

	public static <BaseT> Initializer<BaseT> finalizer(ContextInitializer<? super BaseT> finalizer) {
		return TestBuilders.initializer(finalizer, true);
	}

	// end ContextInitializer

	// begin ContextlessInitializer

	public static <BaseT> Initializer<BaseT> initializer(ContextlessInitializer<? super BaseT> initializer,
			boolean required) {
		return TestBuilders.initializer(TestBuilders.discardInit(initializer), required);
	}

	public static <BaseT> Initializer<BaseT> finalizer(ContextlessInitializer<? super BaseT> finalizer,
			boolean required) {
		return TestBuilders.initializer(TestBuilders.discardInit(finalizer), required);
	}

	public static <BaseT> Initializer<BaseT> initializer(ContextlessInitializer<? super BaseT> initializer) {
		return TestBuilders.initializer(initializer, true);
	}

	public static <BaseT> Initializer<BaseT> finalizer(ContextlessInitializer<? super BaseT> finalizer) {
		return TestBuilders.initializer(finalizer, true);
	}

	// end ContextlessInitializer

	// begin StaticContextInitializer

	public static <BaseT> Initializer<BaseT> initializer(StaticContextInitializer initializer,
			boolean required) {
		return TestBuilders.initializer(TestBuilders.discardInit(initializer), required);
	}

	public static <BaseT> Initializer<BaseT> finalizer(StaticContextInitializer finalizer,
			boolean required) {
		return TestBuilders.initializer(TestBuilders.discardInit(finalizer), required);
	}

	public static <BaseT> Initializer<BaseT> initializer(StaticContextInitializer initializer) {
		return TestBuilders.initializer(initializer, true);
	}

	public static <BaseT> Initializer<BaseT> finalizer(StaticContextInitializer finalizer) {
		return TestBuilders.initializer(finalizer, true);
	}

	// end StaticContextInitializer

	// begin StaticContextlessInitializer

	public static <BaseT> Initializer<BaseT> initializer(StaticContextlessInitializer initializer,
			boolean required) {
		return TestBuilders.initializer(TestBuilders.discardInit(initializer), required);
	}

	public static <BaseT> Initializer<BaseT> finalizer(StaticContextlessInitializer finalizer,
			boolean required) {
		return TestBuilders.initializer(TestBuilders.discardInit(finalizer), required);
	}

	public static <BaseT> Initializer<BaseT> initializer(StaticContextlessInitializer initializer) {
		return TestBuilders.initializer(initializer, true);
	}

	public static <BaseT> Initializer<BaseT> finalizer(StaticContextlessInitializer finalizer) {
		return TestBuilders.initializer(finalizer, true);
	}

	// end StaticContextlessInitializer

	// ==== end initializer()/finalizer()

	@SafeVarargs
	public static <BaseT> Initializer<BaseT> initializers(Initializer<? super BaseT>... initializers) {
		MultiInitializer<BaseT> multi = new MultiInitializer<BaseT>();
		if(initializers != null) {
			for(Initializer<? super BaseT> initializer : initializers)
				multi.addInitializer(initializer);
		}
		return multi;
	}

	@SafeVarargs
	public static <BaseT> Initializer<BaseT> finalizers(Initializer<? super BaseT>... finalizers) {
		return TestBuilders.initializers(finalizers);
	}

	@SafeVarargs
	public static <BaseT> Initializer<BaseT> abort(Initializer<? super BaseT>... initializers) {
		return new KeepGoingInitializer<BaseT>(TestBuilders.initializers(initializers), false);
	}

	@SafeVarargs
	public static <BaseT> Initializer<BaseT> keepGoing(Initializer<? super BaseT>... initializers) {
		return new KeepGoingInitializer<BaseT>(TestBuilders.initializers(initializers), true);
	}

	// ==== begin testsuite()

	@SafeVarargs
	public static <BaseT> SimpleTestsuite<BaseT> testsuite(String name, Testable<? super BaseT>... tests) {
		SimpleTestsuite<BaseT> suite = new SimpleTestsuite<BaseT>(name);
		if(tests != null) {
			for(Testable<? super BaseT> test : tests)
				suite.addTest(test);
		}
		return suite;
	}

	@SafeVarargs
	public static <BaseT> SimpleTestsuite<BaseT> testsuite(Testable<? super BaseT>... tests) {
		return TestBuilders.testsuite((String)null, tests);
	}

	@SafeVarargs
	public static <BaseT> InstantiatingTestsuite<BaseT> testsuite(
		String name,
		ObjectFactory<? extends BaseT, ? extends Throwable> constructor,
		Initializer<? super BaseT> initializer,
		Initializer<? super BaseT> finalizer,
		Testable<? super BaseT>... tests
	) {
		InstantiatingTestsuite<BaseT> suite = new InstantiatingTestsuite<BaseT>(name, constructor);
		suite.addInitializer(initializer);
		suite.addFinalizer(finalizer);
		if(tests != null) {
			for(Testable<? super BaseT> test : tests)
				suite.addTest(test);
		}
		return suite;
	}

	@SafeVarargs
	public static <BaseT> InstantiatingTestsuite<BaseT> testsuite(
		String name,
		ObjectFactory<? extends BaseT, ? extends Throwable> constructor,
		Initializer<? super BaseT> initializer,
		Testable<? super BaseT>... tests
	) {
		return TestBuilders.testsuite(name, constructor, initializer, (Initializer<? super BaseT>)null, tests);
	}

	@SafeVarargs
	public static <BaseT> InstantiatingTestsuite<BaseT> testsuite(
		String name,
		ObjectFactory<? extends BaseT, ? extends Throwable> constructor,
		Testable<? super BaseT>... tests
	) {
		return TestBuilders.testsuite(name, constructor, (Initializer<? super BaseT>)null,
				(Initializer<? super BaseT>)null, tests);
	}

	@SafeVarargs
	public static <BaseT> InstantiatingTestsuite<BaseT> testsuite(
		ObjectFactory<? extends BaseT, ? extends Throwable> constructor,
		Initializer<? super BaseT> initializer,
		Initializer<? super BaseT> finalizer,
		Testable<? super BaseT>... tests
	) {
		return TestBuilders.testsuite((String)null, constructor, initializer, finalizer, tests);
	}

	@SafeVarargs
	public static <BaseT> InstantiatingTestsuite<BaseT> testsuite(
		ObjectFactory<? extends BaseT, ? extends Throwable> constructor,
		Initializer<? super BaseT> initializer,
		Testable<? super BaseT>... tests
	) {
		return TestBuilders.testsuite((String)null, constructor, initializer,
				(Initializer<? super BaseT>)null, tests);
	}

	@SafeVarargs
	public static <BaseT> InstantiatingTestsuite<BaseT> testsuite(
		ObjectFactory<? extends BaseT, ? extends Throwable> constructor,
		Testable<? super BaseT>... tests
	) {
		return TestBuilders.testsuite((String)null, constructor, (Initializer<? super BaseT>)null,
				(Initializer<? super BaseT>)null, tests);
	}

	// ==== end testsuite()

	// ==== begin testcase()

	// begin Testcase

	public static <BaseT> Testable<BaseT> testcase(String name, Testcase<? super BaseT> testcase,
			Predicate<? super Throwable> expectedException, String expectedExceptionName) {
		return new DelegatingTestcase<BaseT>(name, testcase, expectedException, expectedExceptionName);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, Testcase<? super BaseT> testcase,
			Class<? extends Throwable> exceptionType, Predicate<String> messagePredicate) {
		return new DelegatingTestcase<BaseT>(name, testcase, TestBuilders.exception(exceptionType,
				messagePredicate), (exceptionType == null ? "exception" : exceptionType.getName())
				+ (messagePredicate == null ? "" : " matching predicate"));
	}

	public static <BaseT> Testable<BaseT> testcase(String name, Testcase<? super BaseT> testcase,
			Class<? extends Throwable> exceptionType, String message) {
		return new DelegatingTestcase<BaseT>(name, testcase, TestBuilders.exception(exceptionType, message),
				(exceptionType == null ? "exception" : exceptionType.getName())
				+ (message == null ? " with null message" : " with message \"" + message + '"'));
	}

	public static <BaseT> Testable<BaseT> testcase(String name, Testcase<? super BaseT> testcase,
			Class<? extends Throwable> exceptionType) {
		return new DelegatingTestcase<BaseT>(name, testcase, TestBuilders.exception(exceptionType),
				(exceptionType == null ? "exception" : exceptionType.getName()));
	}

	public static <BaseT> Testable<BaseT> testcase(String name, Testcase<? super BaseT> testcase,
			Predicate<String> messagePredicate) {
		return new DelegatingTestcase<BaseT>(name, testcase, TestBuilders.exception(messagePredicate),
				messagePredicate == null ? "exception" : "exception matching predicate");
	}

	public static <BaseT> Testable<BaseT> testcase(String name, Testcase<? super BaseT> testcase,
			String message) {
		return new DelegatingTestcase<BaseT>(name, testcase, TestBuilders.exception(message),
				message == null ? "exception with null message" : "exception with message \"" + message + '"');
	}

	public static <BaseT> Testable<BaseT> testcase(String name, Testcase<? super BaseT> testcase) {
		return new DelegatingTestcase<BaseT>(name, testcase);
	}

	// end Testcase

	// begin ContextlessTestcase

	public static <BaseT> Testable<BaseT> testcase(String name, ContextlessTestcase<? super BaseT> testcase,
			Predicate<? super Throwable> expectedException, String expectedExceptionName) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), expectedException, expectedExceptionName);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, ContextlessTestcase<? super BaseT> testcase,
			Class<? extends Throwable> exceptionType, Predicate<String> messagePredicate) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), exceptionType, messagePredicate);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, ContextlessTestcase<? super BaseT> testcase,
			Class<? extends Throwable> exceptionType, String message) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), exceptionType, message);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, ContextlessTestcase<? super BaseT> testcase,
			Class<? extends Throwable> exceptionType) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), exceptionType);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, ContextlessTestcase<? super BaseT> testcase,
			Predicate<String> messagePredicate) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), messagePredicate);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, ContextlessTestcase<? super BaseT> testcase,
			String message) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), message);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, ContextlessTestcase<? super BaseT> testcase) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase));
	}

	// end ContextlessTestcase

	// begin StaticTestcase

	public static <BaseT> Testable<BaseT> testcase(String name, StaticTestcase testcase,
			Predicate<? super Throwable> expectedException, String expectedExceptionName) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), expectedException, expectedExceptionName);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, StaticTestcase testcase,
			Class<? extends Throwable> exceptionType, Predicate<String> messagePredicate) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), exceptionType, messagePredicate);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, StaticTestcase testcase,
			Class<? extends Throwable> exceptionType, String message) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), exceptionType, message);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, StaticTestcase testcase,
			Class<? extends Throwable> exceptionType) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), exceptionType);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, StaticTestcase testcase,
			Predicate<String> messagePredicate) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), messagePredicate);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, StaticTestcase testcase,
			String message) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), message);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, StaticTestcase testcase) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase));
	}

	// end StaticTestcase

	// begin StaticContextlessTestcase

	public static <BaseT> Testable<BaseT> testcase(String name, StaticContextlessTestcase testcase,
			Predicate<? super Throwable> expectedException, String expectedExceptionName) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), expectedException, expectedExceptionName);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, StaticContextlessTestcase testcase,
			Class<? extends Throwable> exceptionType, Predicate<String> messagePredicate) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), exceptionType, messagePredicate);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, StaticContextlessTestcase testcase,
			Class<? extends Throwable> exceptionType, String message) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), exceptionType, message);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, StaticContextlessTestcase testcase,
			Class<? extends Throwable> exceptionType) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), exceptionType);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, StaticContextlessTestcase testcase,
			Predicate<String> messagePredicate) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), messagePredicate);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, StaticContextlessTestcase testcase,
			String message) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase), message);
	}

	public static <BaseT> Testable<BaseT> testcase(String name, StaticContextlessTestcase testcase) {
		return TestBuilders.testcase(name, TestBuilders.discard(testcase));
	}

	// end StaticContextlessTestcase

	// ==== end testcase()

}
