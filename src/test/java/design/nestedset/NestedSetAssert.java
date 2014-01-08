package design.nestedset;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.api.AbstractAssert;

public class NestedSetAssert<T extends NestedSetElement> extends AbstractAssert<NestedSetAssert<T>, NestedSet<T>> {

	protected NestedSetAssert(NestedSet<T> actual) {
		super(actual, NestedSetAssert.class);
	}

	public final NestedSetAssert<T> hasRoot(T expected) {
		assertThat(actual.getRoot()).isEqualTo(expected);
		return this;
	}

	@SafeVarargs
	public final NestedSetAssert<T> hasAncestors(T element, T... expected) {
		List<T> ancestors = actual.getAncestorsOf(element);
		assertThat(ancestors).containsExactly(expected);
		return this;
	}

	@SafeVarargs
	public final NestedSetAssert<T> hasDescendants(T element, T... expected) {
		List<T> descendants = actual.getDescendantsOf(element);
		assertThat(descendants).containsExactly(expected);
		return this;
	}

	public final NestedSetAssert<T> hasParent(T element, T expected) {
		T parent = actual.getParentOf(element);
		assertThat(parent).isEqualTo(expected);
		return this;
	}

	@SafeVarargs
	public final NestedSetAssert<T> hasChildren(T element, T... expected) {
		List<T> children = actual.getChildrenOf(element);
		assertThat(children).containsExactly(expected);
		return this;
	}

	public final NestedSetAssert<T> isParentNull(T element) {
		T parent = actual.getParentOf(element);
		assertThat(parent).isNull();
		return this;
	}

	public final NestedSetAssert<T> isChildrenEmpty(T element) {
		List<T> children = actual.getChildrenOf(element);
		assertThat(children).isEmpty();
		return this;
	}

}
