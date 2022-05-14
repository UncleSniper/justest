package org.unclesniper.test;

import java.util.Comparator;
import java.util.function.ToIntBiFunction;
import org.unclesniper.test.matcher.Matcher;
import org.unclesniper.test.matcher.TypeMatcher;
import org.unclesniper.test.matcher.EqualMatcher;
import org.unclesniper.test.matcher.ThrowsMatcher;
import org.unclesniper.test.matcher.CompareMatcher;
import org.unclesniper.test.matcher.SubtypeMatcher;
import org.unclesniper.test.matcher.OrderConstraint;
import org.unclesniper.test.matcher.ExceptionMatcher;
import org.unclesniper.test.deepeq.DeepCompareConfig;
import org.unclesniper.test.matcher.DeepEqualMatcher;
import org.unclesniper.test.matcher.OrExceptionMatcher;
import org.unclesniper.test.matcher.AndExceptionMatcher;
import org.unclesniper.test.matcher.DeepEqualConfigurer;
import org.unclesniper.test.matcher.TypeExceptionMatcher;
import org.unclesniper.test.matcher.CauseExceptionMatcher;
import org.unclesniper.test.matcher.MessageExceptionMatcher;
import org.unclesniper.test.matcher.ThrownByExceptionMatcher;

import static org.unclesniper.test.TestUtils.notNull;

public class Matchers {

	public static final Matcher<Boolean, Boolean> yay = new EqualMatcher<Boolean>(Boolean.TRUE);

	public static final Matcher<Boolean, Boolean> nay = new EqualMatcher<Boolean>(Boolean.FALSE);

	public static final Matcher<Boolean, Boolean> yes = Matchers.yay;

	public static final Matcher<Boolean, Boolean> no = Matchers.nay;

	public static final Matcher<Boolean, Boolean> trew = Matchers.yay;

	public static final Matcher<Boolean, Boolean> phalse = Matchers.nay;

	private Matchers() {}

	public static <InT, OutT> Matcher<InT, OutT> is(Matcher<InT, OutT> matcher) {
		return notNull(matcher, "Matcher");
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
		return new CompareMatcher<SubjectT, SubjectT>(notNull(comparator, "Comparator")::compare, expected,
				OrderConstraint.EQUAL);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> orderEqualTo(BoundT expected) {
		return new CompareMatcher<SubjectT, BoundT>(Matchers.comparatorFromComparable(), expected,
				OrderConstraint.EQUAL);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> orderUnequalTo(SubjectT expected,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(notNull(comparator, "Comparator")::compare, expected,
				OrderConstraint.UNEQUAL);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> orderUnequalTo(BoundT expected) {
		return new CompareMatcher<SubjectT, BoundT>(Matchers.comparatorFromComparable(), expected,
				OrderConstraint.UNEQUAL);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> lessThan(SubjectT bound,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(notNull(comparator, "Comparator")::compare, bound,
				OrderConstraint.LESS);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> lessThan(BoundT bound) {
		return new CompareMatcher<SubjectT, BoundT>(Matchers.comparatorFromComparable(), bound,
				OrderConstraint.LESS);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> lessOrEqual(SubjectT bound,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(notNull(comparator, "Comparator")::compare, bound,
				OrderConstraint.LESS_EQUAL);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> lessOrEqual(BoundT bound) {
		return new CompareMatcher<SubjectT, BoundT>(Matchers.comparatorFromComparable(), bound,
				OrderConstraint.LESS_EQUAL);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> greaterThan(SubjectT bound,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(notNull(comparator, "Comparator")::compare, bound,
				OrderConstraint.GREATER);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> greaterThan(BoundT bound) {
		return new CompareMatcher<SubjectT, BoundT>(Matchers.comparatorFromComparable(), bound,
				OrderConstraint.GREATER);
	}

	public static <SubjectT> Matcher<SubjectT, SubjectT> greaterOrEqual(SubjectT bound,
			Comparator<? super SubjectT> comparator) {
		return new CompareMatcher<SubjectT, SubjectT>(notNull(comparator, "Comparator")::compare, bound,
				OrderConstraint.GREATER_EQUAL);
	}

	public static <BoundT, SubjectT extends Comparable<? super BoundT>>
	Matcher<SubjectT, SubjectT> greaterOrEqual(BoundT bound) {
		return new CompareMatcher<SubjectT, BoundT>(Matchers.comparatorFromComparable(), bound,
				OrderConstraint.GREATER_EQUAL);
	}

	public static Matcher<Executable, Executable> doesNotThrow() {
		return new ThrowsMatcher(null);
	}

	public static Matcher<Executable, Executable> willThrow(ExceptionMatcher... matchers) {
		AndExceptionMatcher all = new AndExceptionMatcher();
		if(matchers != null) {
			for(ExceptionMatcher matcher : matchers)
				all.addMatcher(matcher);
		}
		return new ThrowsMatcher(all.isEmpty() ? null : all);
	}

	public static ExceptionMatcher all(ExceptionMatcher... matchers) {
		AndExceptionMatcher all = new AndExceptionMatcher();
		if(matchers != null) {
			for(ExceptionMatcher matcher : matchers)
				all.addMatcher(matcher);
		}
		if(all.isEmpty())
			throw new IllegalArgumentException("Cannot form conjunction of zero exception matchers");
		return all;
	}

	public static ExceptionMatcher any(ExceptionMatcher... matchers) {
		OrExceptionMatcher any = new OrExceptionMatcher();
		if(matchers != null) {
			for(ExceptionMatcher matcher : matchers)
				any.addMatcher(matcher);
		}
		if(any.isEmpty())
			throw new IllegalArgumentException("Cannot form disjunction of zero exception matchers");
		return any;
	}

	public static ExceptionMatcher exceptionOfType(Class<? extends Throwable> exceptionClass) {
		return new TypeExceptionMatcher(exceptionClass);
	}

	public static ExceptionMatcher exceptionWithMessage(String message) {
		return new MessageExceptionMatcher(message);
	}

	public static ExceptionMatcher withoutCause() {
		return new CauseExceptionMatcher(null);
	}

	public static ExceptionMatcher causedBy(ExceptionMatcher... matchers) {
		AndExceptionMatcher all = new AndExceptionMatcher();
		if(matchers != null) {
			for(ExceptionMatcher matcher : matchers)
				all.addMatcher(matcher);
		}
		return new CauseExceptionMatcher(all.isEmpty() ? null : all);
	}

	public static ExceptionMatcher exception(Class<? extends Throwable> exceptionClass,
			ExceptionMatcher... matchers) {
		AndExceptionMatcher all = new AndExceptionMatcher();
		all.addMatcher(Matchers.exceptionOfType(exceptionClass));
		if(matchers != null) {
			for(ExceptionMatcher matcher : matchers)
				all.addMatcher(matcher);
		}
		return all;
	}

	public static ExceptionMatcher exception(String message, ExceptionMatcher... matchers) {
		AndExceptionMatcher all = new AndExceptionMatcher();
		all.addMatcher(Matchers.exceptionWithMessage(message));
		if(matchers != null) {
			for(ExceptionMatcher matcher : matchers)
				all.addMatcher(matcher);
		}
		return all;
	}

	public static ExceptionMatcher exception(Class<? extends Throwable> exceptionClass, String message,
			ExceptionMatcher... matchers) {
		AndExceptionMatcher all = new AndExceptionMatcher();
		all.addMatcher(Matchers.exceptionOfType(exceptionClass));
		all.addMatcher(Matchers.exceptionWithMessage(message));
		if(matchers != null) {
			for(ExceptionMatcher matcher : matchers)
				all.addMatcher(matcher);
		}
		return all;
	}

	public static ExceptionMatcher causedBy(Class<? extends Throwable> causeClass, ExceptionMatcher... matchers) {
		return new CauseExceptionMatcher(Matchers.exception(causeClass, matchers));
	}

	public static ExceptionMatcher causedBy(String causeMessage, ExceptionMatcher... matchers) {
		return new CauseExceptionMatcher(Matchers.exception(causeMessage, matchers));
	}

	public static ExceptionMatcher causedBy(Class<? extends Throwable> causeClass, String causeMessage,
			ExceptionMatcher... matchers) {
		return new CauseExceptionMatcher(Matchers.exception(causeClass, causeMessage, matchers));
	}

	public static ExceptionMatcher thrownBy(String className) {
		return new ThrownByExceptionMatcher(className);
	}

	public static ExceptionMatcher thrownWithin(String className) {
		return new ThrownByExceptionMatcher(className, true);
	}

	public static ExceptionMatcher thrownBy(String className, String methodName) {
		return new ThrownByExceptionMatcher(className, methodName);
	}

	public static ExceptionMatcher thrownWithin(String className, String methodName) {
		return new ThrownByExceptionMatcher(className, methodName, true);
	}

	public static ExceptionMatcher thrownBy(Class<?> clazz) {
		return new ThrownByExceptionMatcher(clazz);
	}

	public static ExceptionMatcher thrownWithin(Class<?> clazz) {
		return new ThrownByExceptionMatcher(clazz, true);
	}

	public static ExceptionMatcher thrownBy(Class<?> clazz, String methodName) {
		return new ThrownByExceptionMatcher(clazz, methodName);
	}

	public static ExceptionMatcher thrownWithin(Class<?> clazz, String methodName) {
		return new ThrownByExceptionMatcher(clazz, methodName, true);
	}

	public static ExceptionMatcher thrownBy(String className, boolean inward) {
		return new ThrownByExceptionMatcher(className, inward);
	}

	public static ExceptionMatcher thrownBy(String className, String methodName, boolean inward) {
		return new ThrownByExceptionMatcher(className, methodName, inward);
	}

	public static ExceptionMatcher thrownBy(Class<?> clazz, boolean inward) {
		return new ThrownByExceptionMatcher(clazz, inward);
	}

	public static ExceptionMatcher thrownBy(Class<?> clazz, String methodName, boolean inward) {
		return new ThrownByExceptionMatcher(clazz, methodName, inward);
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
		return new TypeMatcher<SubjectT>(expectedType, false);
	}

	public static <InT, OutT> Matcher<InT, OutT> ofSubtype(Class<? extends OutT> expectedType) {
		return new SubtypeMatcher<InT, OutT>(expectedType);
	}

}
