package org.unclesniper.test;

import java.util.List;
import java.util.LinkedList;

import static org.unclesniper.test.Matchers.is;
import static org.unclesniper.test.Matchers.equalTo;
import static org.unclesniper.test.Assert.assertThat;
import static org.unclesniper.test.Matchers.lessOrEqual;
import static org.unclesniper.test.TestBuilders.testcase;
import static org.unclesniper.test.TestBuilders.testsuite;
import static org.unclesniper.test.Matchers.greaterOrEqual;

public class LineSplitterTests {

	private interface InputMethod {

		int split(LineSplitter splitter, String input, int start, int end);

	}

	private static class NamedInputMethod implements InputMethod {

		private final String name;

		private final InputMethod method;

		public NamedInputMethod(String name, InputMethod method) {
			this.name = name;
			this.method = method;
		}

		@Override
		public int split(LineSplitter splitter, String input, int start, int end) {
			return method.split(splitter, input, start, end);
		}

		@Override
		public String toString() {
			return name;
		}

	}

	private static class Piece {

		final int offset;

		final int length;

		public Piece(int offset, int length) {
			this.offset = offset;
			this.length = length;
		}

	}

	private static class Case {

		final String input;

		final Piece[] pieces;

		final String[] expected;

		public Case(String input, Piece[] pieces, String[] expected) {
			this.input = input;
			this.pieces = pieces;
			this.expected = expected;
		}

		public void performTest() {
			for(InputMethod method : new InputMethod[] {
				(splitter, str, start, end) -> splitter.split(str.substring(0, end), start),
				(splitter, str, start, end) -> splitter.split(str.toCharArray(), start, end - start)
			}) {
				int prescribedStart = -1;
				LineSplitter splitter = new LineSplitter();
				List<String> gotPieces = new LinkedList<String>();
				for(Piece piece : pieces) {
					if(prescribedStart >= 0) {
						assertThat(piece.offset).is(equalTo(prescribedStart));
						assertThat(prescribedStart).is(lessOrEqual(input.length()));
					}
					int addCount = method.split(splitter, input, piece.offset, piece.offset + piece.length);
					int skip = splitter.getSkip();
					if(addCount < 0) {
						addCount = piece.length;
						skip = 0;
					}
					else
						assertThat(addCount).is(lessOrEqual(piece.length));
					assertThat(skip).is(greaterOrEqual(0));
					assertThat(piece.offset + addCount).is(lessOrEqual(input.length()));
					gotPieces.add(input.substring(piece.offset, piece.offset + addCount));
					prescribedStart = piece.offset + addCount + skip;
				}
				assertThat(prescribedStart).is(equalTo(input.length()));
				assertThat(gotPieces.size()).is(equalTo(expected.length));
				for(int i = 0; i < expected.length; ++i)
					assertThat(gotPieces.get(i)).is(equalTo(expected[i]));
			}
		}

	}

	public static final Testable<Void> TESTSUITE = testsuite("LineSplitter", LineSplitterTests::new,
		testcase("FIXME: all tests in one", LineSplitterTests::fixMeTestEverything)
	);

	private void fixMeTestEverything() {
		for(Case tcase : tcases(
			tcase("foo\rbar", pieces(
				piece(0, 7),
				piece(4, 3)
			), expect("foo", "bar")),
			tcase("foobar\nbaz", pieces(
				piece(3, 7),
				piece(7, 3)
			), expect("bar", "baz")),
			tcase("foo\r\nbar", pieces(
				piece(0, 8),
				piece(5, 3)
			), expect("foo", "bar")),
			tcase("foo\n\rbar", pieces(
				piece(0, 8),
				piece(4, 4),
				piece(5, 3)
			), expect("foo", "", "bar")),
			tcase("\nfoo", pieces(
				piece(0, 4),
				piece(1, 3)
			), expect("", "foo")),
			tcase("foo\n", pieces(
				piece(0, 4),
				piece(4, 0)
			), expect("foo", ""))
		))
			tcase.performTest();
	}

	private static Piece piece(int offset, int length) {
		return new Piece(offset, length);
	}

	private static Piece[] pieces(Piece... pieces) {
		return pieces;
	}

	private static Case tcase(String input, Piece[] pieces, String[] expected) {
		return new Case(input, pieces, expected);
	}

	private static Case[] tcases(Case... cases) {
		return cases;
	}

	private static String[] expect(String... expected) {
		return expected;
	}

}
