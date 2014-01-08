package design.nestedset;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.testng.annotations.Test;

import design.nestedset.NestedSet.Builder;

@Test
public class NestedSetTest {

	private final static NestedSetElementTest ROOT = new NestedSetElementTest(1, 18);

	private final static NestedSetElementTest E_1 = new NestedSetElementTest(2, 5);

	private final static NestedSetElementTest E_1_2 = new NestedSetElementTest(3, 4);

	private final static NestedSetElementTest E_2 = new NestedSetElementTest(6, 17);

	private final static NestedSetElementTest E_2_1 = new NestedSetElementTest(7, 8);

	private final static NestedSetElementTest E_2_2 = new NestedSetElementTest(9, 14);

	private final static NestedSetElementTest E_2_2_1 = new NestedSetElementTest(10, 11);

	private final static NestedSetElementTest E_2_2_2 = new NestedSetElementTest(12, 13);

	private final static NestedSetElementTest E_2_3 = new NestedSetElementTest(15, 16);

	private Builder<NestedSetElementTest> nestedSetBuilder;

	private NestedSet<NestedSetElementTest> nestedSet;

	public void shouldCalculateRoot() {
		given();

		NestedSetElementTest root = when().getRoot();

		assertThat(root).isEqualTo(ROOT);
	}

	public void shouldCalculateParentOfE22() {
		given();

		NestedSetElementTest parent = when().getParentOf(E_2_2);

		assertThat(parent).isEqualTo(E_2);
	}

	public void shouldCalculateAncestorsOfE22() {
		given();

		List<NestedSetElementTest> ancestors = when().getAncestorsOf(E_2_2);

		assertThat(ancestors).containsExactly(ROOT, E_2);
	}

	public void shouldCalculateChildrenOfE2() {
		given();

		List<NestedSetElementTest> children = when().getChildrenOf(E_2);

		assertThat(children).containsExactly(E_2_1, E_2_2, E_2_3);
	}

	public void shouldCalculateDescendantsOfE2() {
		given();

		List<NestedSetElementTest> descendants = when().getDescendantsOf(E_2);

		assertThat(descendants).containsExactly(E_2_1, E_2_2, E_2_2_1, E_2_2_2, E_2_3);
	}

	public void shouldCalculateParentOfRoot() {
		given();

		NestedSetElementTest parent = when().getParentOf(ROOT);

		assertThat(parent).isNull();
	}

	public void shouldCalculateChildrenOfLeaf() {
		given();

		List<NestedSetElementTest> children = when().getChildrenOf(E_1_2);

		assertThat(children).isEmpty();
	}

	public void shouldAddChild() {
		NestedSetElementTest newChild = new NestedSetElementTest();

		given();

		when().addChild(E_1, newChild);

		then().hasParent(newChild, E_1);
	}

	public void shouldRemoveChildE1() {
		given();

		when().removeChild(E_1);

		then().hasChildren(ROOT, E_2);
	}

	public void shouldRemoveChildE2() {
		given();

		when().removeChild(E_2);

		then().hasChildren(ROOT, E_1);
	}

	private Builder<NestedSetElementTest> given() {
		this.nestedSetBuilder = new Builder<NestedSetElementTest>().withElements(ROOT, E_1, E_1_2, E_2, E_2_1, E_2_2,
				E_2_2_1, E_2_2_2, E_2_3);
		return this.nestedSetBuilder;
	}

	private NestedSet<NestedSetElementTest> when() {
		this.nestedSet = nestedSetBuilder.build();
		return this.nestedSet;
	}

	private NestedSetAssert<NestedSetElementTest> then() {
		return new NestedSetAssert<NestedSetElementTest>(this.nestedSet);
	}

	static class NestedSetElementTest implements NestedSetElement {

		private NestedSetBound bound;

		NestedSetElementTest() {
			this(new NestedSetBound());
		}

		NestedSetElementTest(int left, int rigth) {
			this(new NestedSetBound(left, rigth));
		}

		NestedSetElementTest(NestedSetBound bound) {
			this.bound = bound;
		}

		@Override
		public NestedSetBound getBound() {
			return bound;
		}

		@Override
		public void setBound(NestedSetBound bound) {
			this.bound = bound;
		}

		@Override
		public String toString() {
			return bound.toString();
		}

	}

}
