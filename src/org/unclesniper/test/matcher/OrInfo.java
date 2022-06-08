package org.unclesniper.test.matcher;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.util.stream.Stream;
import org.unclesniper.test.TestUtils;
import org.unclesniper.test.IndentSink;
import org.unclesniper.test.Describeable;
import org.unclesniper.test.BulletIndentSink;

public class OrInfo extends AbstractInfo {

	private final Object subject;

	private final List<Describeable> failures = new LinkedList<Describeable>();

	public OrInfo(Object subject) {
		this.subject = subject;
	}

	public Object getSubject() {
		return subject;
	}

	public void addFailure(Describeable failure) {
		if(failure != null)
			failures.add(failure);
	}

	public List<Describeable> getFailures() {
		return Collections.unmodifiableList(failures);
	}

	@Override
	protected void make(IndentSink sink) {
		sink.append("Expected", false);
		sink.append(TestUtils.describeObject(subject), true);
		sink.append("to satisfy at least one of the sub-matchers, but didn't satisfy any:", false);
		BulletIndentSink bullet = new BulletIndentSink(sink, false);
		for(Describeable failure : failures) {
			Stream<String> lines = failure.describe();
			if(lines == null)
				continue;
			bullet.reset();
			lines.forEach(line -> {
				if(line != null)
					bullet.append(line, false);
			});
		}
	}

}
