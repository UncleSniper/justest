package org.unclesniper.test;

import java.util.Comparator;
import java.util.function.ToIntBiFunction;
import org.unclesniper.test.matcher.Matcher;
import org.unclesniper.test.matcher.TypeMatcher;
import org.unclesniper.test.matcher.NullMatcher;
import org.unclesniper.test.matcher.SameMatcher;
import org.unclesniper.test.matcher.EqualMatcher;
import org.unclesniper.test.matcher.ThrowsMatcher;
import org.unclesniper.test.matcher.CompareMatcher;
import org.unclesniper.test.matcher.SubtypeMatcher;
import org.unclesniper.test.matcher.OrderConstraint;
import org.unclesniper.test.matcher.ExceptionMatcher;
import org.unclesniper.test.deepeq.DeepCompareConfig;
import org.unclesniper.test.matcher.DeepEqualMatcher;
import org.unclesniper.test.matcher.OrExceptionMatcher;
import org.unclesniper.test.matcher.ThrowsYieldMatcher;
import org.unclesniper.test.matcher.AndExceptionMatcher;
import org.unclesniper.test.matcher.DeepEqualConfigurer;
import org.unclesniper.test.matcher.TypeExceptionMatcher;
import org.unclesniper.test.matcher.CauseExceptionMatcher;
import org.unclesniper.test.matcher.MessageExceptionMatcher;
import org.unclesniper.test.matcher.NumericallyCloseMatcher;
import org.unclesniper.test.matcher.ThrownByExceptionMatcher;

public class Matchers {

	public static final Matcher<Boolean, Boolean> yay = new EqualMatcher<Boolean>(Boolean.TRUE);

	public static final Matcher<Boolean, Boolean> nay = new EqualMatcher<Boolean>(Boolean.FALSE);

	public static final Matcher<Boolean, Boolean> yes = Matchers.yay;

	public static final Matcher<Boolean, Boolean> no = Matchers.nay;

	public static final Matcher<Boolean, Boolean> trew = Matchers.yay;

	public static final Matcher<Boolean, Boolean> phalse = Matchers.nay;

	private Matchers() {}

	public static <InT, OutT> Matcher<InT, OutT> is(Matcher<InT, OutT> matcher) {
		return TestUtils.notNull(matcher, "Matcher");
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> equalTo(SubjectT expected) {
		return new EqualMatcher<SubjectT>(expected);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> notEqualTo(SubjectT unexpected) {
		return new EqualMatcher<SubjectT>(unexpected, true);
	}

	public static <SubjectT> DeepEqualConfigurer<SubjectT> deepEqualTo(SubjectT expected) {
		return new DeepEqualMatcher<SubjectT>(expected, false);
	}

	public static <SubjectT> DeepEqualConfigurer<SubjectT> deepEqualTo(SubjectT expected,
			DeepCompareConfig config) {
		return new DeepEqualMatcher<SubjectT>(expected, false, config);
	}

	public static <SubjectT> DeepEqualConfigurer<SubjectT> notDeepEqualTo(SubjectT expected) {
		return new DeepEqualMatcher<SubjectT>(expected, true);
	}

	public static <SubjectT> DeepEqualConfigurer<SubjectT> notDeepEqualTo(SubjectT expected,
			DeepCompareConfig config) {
		return new DeepEqualMatcher<SubjectT>(expected, true, config);
	}

	private static <BoundT, SubjectT extends Comparable<? super BoundT>>
	ToIntBiFunction<? super SubjectT, ? super BoundT> comparatorFromComparable() {
		return (a, b) -> {
			if(a == null)
				throw new CompareMatcher.ComparisonNullPointerException("Attempted to call "
						+ "Comparable.compareTo() on null");
			return a.compareTo(b);
		};
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> orderEqualTo(SubjectT expected,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(TestUtils.notNull(comparator, "Comparator")::compare,
				expected, OrderConstraint.EQUAL);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> orderEqualTo(BoundT expected) {
		return new CompareMatcher<SubjectT, BoundT>(Matchers.comparatorFromComparable(), expected,
				OrderConstraint.EQUAL);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> orderUnequalTo(SubjectT expected,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(TestUtils.notNull(comparator, "Comparator")::compare,
				expected, OrderConstraint.UNEQUAL);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> orderUnequalTo(BoundT expected) {
		return new CompareMatcher<SubjectT, BoundT>(Matchers.comparatorFromComparable(), expected,
				OrderConstraint.UNEQUAL);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> lessThan(SubjectT bound,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(TestUtils.notNull(comparator, "Comparator")::compare,
				bound, OrderConstraint.LESS);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> lessThan(BoundT bound) {
		return new CompareMatcher<SubjectT, BoundT>(Matchers.comparatorFromComparable(), bound,
				OrderConstraint.LESS);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> lessOrEqual(SubjectT bound,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(TestUtils.notNull(comparator, "Comparator")::compare,
				bound, OrderConstraint.LESS_EQUAL);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> lessOrEqual(BoundT bound) {
		return new CompareMatcher<SubjectT, BoundT>(Matchers.comparatorFromComparable(), bound,
				OrderConstraint.LESS_EQUAL);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> greaterThan(SubjectT bound,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(TestUtils.notNull(comparator, "Comparator")::compare,
				bound, OrderConstraint.GREATER);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> greaterThan(BoundT bound) {
		return new CompareMatcher<SubjectT, BoundT>(Matchers.comparatorFromComparable(), bound,
				OrderConstraint.GREATER);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> greaterOrEqual(SubjectT bound,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(TestUtils.notNull(comparator, "Comparator")::compare,
				bound, OrderConstraint.GREATER_EQUAL);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> greaterOrEqual(BoundT bound) {
		return new CompareMatcher<SubjectT, BoundT>(Matchers.comparatorFromComparable(), bound,
				OrderConstraint.GREATER_EQUAL);
	}

	public static Matcher<Executable, Executable> doesNotThrow() {
		return new ThrowsMatcher(null);
	}

	@SafeVarargs
	public static Matcher<Executable, Executable>
	willThrow(ExceptionMatcher<? extends Throwable, ? extends Throwable>... matchers) {
		AndExceptionMatcher<Throwable> all = new AndExceptionMatcher<Throwable>();
		if(matchers != null) {
			for(ExceptionMatcher<? extends Throwable, ? extends Throwable> matcher : matchers)
				all.addMatcher(matcher);
		}
		return new ThrowsMatcher(all.isEmpty() ? null : all);
	}

	public static <ExceptionT extends Throwable> Matcher<Executable, ExceptionT>
	willThrowExceptionThat(ExceptionMatcher<? extends Throwable, ? extends ExceptionT> matcher) {
		return new ThrowsYieldMatcher<ExceptionT>(TestUtils.notNull(matcher, "Exception matcher"));
	}

	@SafeVarargs
	public static <ExceptionT extends Throwable> ExceptionMatcher<ExceptionT, ExceptionT>
	all(ExceptionMatcher<? extends Throwable, ? extends Throwable>... matchers) {
		AndExceptionMatcher<ExceptionT> all = new AndExceptionMatcher<ExceptionT>();
		if(matchers != null) {
			for(ExceptionMatcher<? extends Throwable, ? extends Throwable> matcher : matchers)
				all.addMatcher(matcher);
		}
		if(all.isEmpty())
			throw new IllegalArgumentException("Cannot form conjunction of zero exception matchers");
		return all;
	}

	@SafeVarargs
	public static <ExceptionT extends Throwable> ExceptionMatcher<ExceptionT, ExceptionT>
	any(ExceptionMatcher<? extends Throwable, ? extends Throwable>... matchers) {
		OrExceptionMatcher<ExceptionT> any = new OrExceptionMatcher<ExceptionT>();
		if(matchers != null) {
			for(ExceptionMatcher<? extends Throwable, ? extends Throwable> matcher : matchers)
				any.addMatcher(matcher);
		}
		if(any.isEmpty())
			throw new IllegalArgumentException("Cannot form disjunction of zero exception matchers");
		return any;
	}

	public static <InT extends Throwable, ExceptionT extends Throwable> ExceptionMatcher<InT, ExceptionT>
	exceptionOfType(Class<ExceptionT> exceptionClass) {
		return new TypeExceptionMatcher<InT, ExceptionT>(exceptionClass);
	}

	public static <ExceptionT extends Throwable> ExceptionMatcher<ExceptionT, ExceptionT>
	exceptionWithMessage(String message) {
		return new MessageExceptionMatcher<ExceptionT>(message);
	}

	public static <ExceptionT extends Throwable> ExceptionMatcher<ExceptionT, ExceptionT> withoutCause() {
		return new CauseExceptionMatcher<ExceptionT>(null);
	}

	@SafeVarargs
	public static <ExceptionT extends Throwable> ExceptionMatcher<ExceptionT, ExceptionT>
	causedBy(ExceptionMatcher<? extends Throwable, ? extends Throwable>... matchers) {
		AndExceptionMatcher<Throwable> all = new AndExceptionMatcher<Throwable>();
		if(matchers != null) {
			for(ExceptionMatcher<? extends Throwable, ? extends Throwable> matcher : matchers)
				all.addMatcher(matcher);
		}
		return new CauseExceptionMatcher<ExceptionT>(all.isEmpty() ? null : all);
	}

	@SafeVarargs
	@SuppressWarnings("unchecked")
	public static <InT extends Throwable, ExceptionT extends Throwable>
	ExceptionMatcher<InT, ExceptionT> exception(
		Class<? extends Throwable> exceptionClass,
		ExceptionMatcher<? extends Throwable, ? extends Throwable>... matchers
	) {
		AndExceptionMatcher<ExceptionT> all = new AndExceptionMatcher<ExceptionT>();
		all.addMatcher(Matchers.exceptionOfType(exceptionClass));
		if(matchers != null) {
			for(ExceptionMatcher<? extends Throwable, ? extends Throwable> matcher : matchers)
				all.addMatcher(matcher);
		}
		return (ExceptionMatcher)all;
	}

	@SafeVarargs
	public static <ExceptionT extends Throwable> ExceptionMatcher<ExceptionT, ExceptionT>
	exception(String message, ExceptionMatcher<? extends Throwable, ? extends Throwable>... matchers) {
		AndExceptionMatcher<ExceptionT> all = new AndExceptionMatcher<ExceptionT>();
		all.addMatcher(Matchers.exceptionWithMessage(message));
		if(matchers != null) {
			for(ExceptionMatcher<? extends Throwable, ? extends Throwable> matcher : matchers)
				all.addMatcher(matcher);
		}
		return all;
	}

	@SafeVarargs
	@SuppressWarnings("unchecked")
	public static <InT extends Throwable, ExceptionT extends Throwable>
	ExceptionMatcher<InT, ExceptionT> exception(
		Class<ExceptionT> exceptionClass,
		String message,
		ExceptionMatcher<? extends Throwable, ? extends Throwable>... matchers
	) {
		AndExceptionMatcher<ExceptionT> all = new AndExceptionMatcher<ExceptionT>();
		all.addMatcher(Matchers.exceptionOfType(exceptionClass));
		all.addMatcher(Matchers.exceptionWithMessage(message));
		if(matchers != null) {
			for(ExceptionMatcher<? extends Throwable, ? extends Throwable> matcher : matchers)
				all.addMatcher(matcher);
		}
		return (ExceptionMatcher)all;
	}

	@SafeVarargs
	public static <ExceptionT extends Throwable>
	ExceptionMatcher<ExceptionT, ExceptionT> causedBy(
		Class<? extends Throwable> causeClass,
		ExceptionMatcher<? extends Throwable, ? extends Throwable>... matchers
	) {
		return new CauseExceptionMatcher<ExceptionT>(Matchers.exception(causeClass, matchers));
	}

	@SafeVarargs
	public static <ExceptionT extends Throwable> ExceptionMatcher<ExceptionT, ExceptionT>
	causedBy(String causeMessage, ExceptionMatcher<? extends Throwable, ? extends Throwable>... matchers) {
		return new CauseExceptionMatcher<ExceptionT>(Matchers.exception(causeMessage, matchers));
	}

	@SafeVarargs
	public static <ExceptionT extends Throwable>
	ExceptionMatcher<ExceptionT, ExceptionT> causedBy(
		Class<? extends Throwable> causeClass,
		String causeMessage,
		ExceptionMatcher<? extends Throwable, ? extends Throwable>... matchers
	) {
		return new CauseExceptionMatcher<ExceptionT>(Matchers.exception(causeClass, causeMessage, matchers));
	}

	public static <ExceptionT extends Throwable>
	ExceptionMatcher<ExceptionT, ExceptionT> thrownBy(String className) {
		return new ThrownByExceptionMatcher<ExceptionT>(className);
	}

	public static <ExceptionT extends Throwable>
	ExceptionMatcher<ExceptionT, ExceptionT> thrownWithin(String className) {
		return new ThrownByExceptionMatcher<ExceptionT>(className, true);
	}

	public static <ExceptionT extends Throwable>
	ExceptionMatcher<ExceptionT, ExceptionT> thrownBy(String className, String methodName) {
		return new ThrownByExceptionMatcher<ExceptionT>(className, methodName);
	}

	public static <ExceptionT extends Throwable>
	ExceptionMatcher<ExceptionT, ExceptionT> thrownWithin(String className, String methodName) {
		return new ThrownByExceptionMatcher<ExceptionT>(className, methodName, true);
	}

	public static <ExceptionT extends Throwable> ExceptionMatcher<ExceptionT, ExceptionT> thrownBy(Class<?> clazz) {
		return new ThrownByExceptionMatcher<ExceptionT>(clazz);
	}

	public static <ExceptionT extends Throwable>
	ExceptionMatcher<ExceptionT, ExceptionT> thrownWithin(Class<?> clazz) {
		return new ThrownByExceptionMatcher<ExceptionT>(clazz, true);
	}

	public static <ExceptionT extends Throwable>
	ExceptionMatcher<ExceptionT, ExceptionT> thrownBy(Class<?> clazz, String methodName) {
		return new ThrownByExceptionMatcher<ExceptionT>(clazz, methodName);
	}

	public static <ExceptionT extends Throwable>
	ExceptionMatcher<ExceptionT, ExceptionT> thrownWithin(Class<?> clazz, String methodName) {
		return new ThrownByExceptionMatcher<ExceptionT>(clazz, methodName, true);
	}

	public static <ExceptionT extends Throwable>
	ExceptionMatcher<ExceptionT, ExceptionT> thrownBy(String className, boolean inward) {
		return new ThrownByExceptionMatcher<ExceptionT>(className, inward);
	}

	public static <ExceptionT extends Throwable>
	ExceptionMatcher<ExceptionT, ExceptionT> thrownBy(String className, String methodName, boolean inward) {
		return new ThrownByExceptionMatcher<ExceptionT>(className, methodName, inward);
	}

	public static <ExceptionT extends Throwable>
	ExceptionMatcher<ExceptionT, ExceptionT> thrownBy(Class<?> clazz, boolean inward) {
		return new ThrownByExceptionMatcher<ExceptionT>(clazz, inward);
	}

	public static <ExceptionT extends Throwable>
	ExceptionMatcher<ExceptionT, ExceptionT> thrownBy(Class<?> clazz, String methodName, boolean inward) {
		return new ThrownByExceptionMatcher<ExceptionT>(clazz, methodName, inward);
	}

	public static Matcher<Boolean, Boolean> yay() {
		return Matchers.yay;
	}

	public static Matcher<Boolean, Boolean> nay() {
		return Matchers.nay;
	}

	public static Matcher<Boolean, Boolean> yes() {
		return Matchers.yay;
	}

	public static Matcher<Boolean, Boolean> no() {
		return Matchers.nay;
	}

	public static Matcher<Boolean, Boolean> trew() {
		return Matchers.yay;
	}

	public static Matcher<Boolean, Boolean> phalse() {
		return Matchers.nay;
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> ofType(Class<?> expectedType) {
		return new TypeMatcher<SubjectT>(expectedType);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> notOfType(Class<?> expectedType) {
		return new TypeMatcher<SubjectT>(expectedType, true);
	}

	public static <InT, OutT> Matcher<InT, OutT> ofSubtype(Class<? extends OutT> expectedType) {
		return new SubtypeMatcher<InT, OutT>(expectedType);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> nil() {
		return new NullMatcher<SubjectT>(false);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> notNull() {
		return new NullMatcher<SubjectT>(true);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> sameAs(SubjectT expected) {
		return new SameMatcher<SubjectT>(expected);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> notSameAs(SubjectT expected) {
		return new SameMatcher<SubjectT>(expected, true);
	}

	public static <SubjectT extends Number> Matcher<SubjectT, SubjectT> closeTo(Number expected, double epsilon) {
		return new NumericallyCloseMatcher<SubjectT>(expected, epsilon);
	}

	public static <SubjectT extends Number> Matcher<SubjectT, SubjectT>
	notCloseTo(Number expected, double epsilon) {
		return new NumericallyCloseMatcher<SubjectT>(expected, epsilon, true);
	}

}
