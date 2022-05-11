package org.unclesniper.test.deepeq;

import java.util.Collection;
import java.util.function.Predicate;

public class CollectionDeepCompareFamily implements DeepCompareFamily {

	private Predicate<Collection<?>> orderedPredicate;

	private boolean symmetricEquality;

	public CollectionDeepCompareFamily() {
		symmetricEquality = true;
	}

	public CollectionDeepCompareFamily(Predicate<Collection<?>> orderedPredicate, boolean symmetricEquality) {
		this.orderedPredicate = orderedPredicate;
		this.symmetricEquality = symmetricEquality;
	}

	public Predicate<Collection<?>> getOrderedPredicate() {
		return orderedPredicate;
	}

	public void setOrderedPredicate(Predicate<Collection<?>> orderedPredicate) {
		this.orderedPredicate = orderedPredicate;
	}

	public boolean isSymmetricEquality() {
		return symmetricEquality;
	}

	public void setSymmetricEquality(boolean symmetricEquality) {
		this.symmetricEquality = symmetricEquality;
	}

	@Override
	public DeepComparer tryDeepEquals(Object a, Object b) {
		if(!(a instanceof Collection) || !(b instanceof Collection))
			return null;
		Collection<?> aCollection = (Collection)a;
		Collection<?> bCollection = (Collection)b;
		boolean ordered;
		if(orderedPredicate == null)
			ordered = DeepEquals.isOrderedCollection(aCollection) && DeepEquals.isOrderedCollection(bCollection);
		else
			ordered = orderedPredicate.test(aCollection) && orderedPredicate.test(bCollection);
		if(ordered)
			return DeepEquals.ORDERED_COLLECTION_DEEP_COMPARER;
		else
			return (a2, b2, seen, config) -> DeepEquals.deepEqualsUnorderedCollection((Collection)a2,
					(Collection)b2, seen,config, symmetricEquality);
	}

}
