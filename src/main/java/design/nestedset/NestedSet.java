package design.nestedset;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NestedSet<T extends NestedSetElement> {

	private Set<T> elements;

	NestedSet(Builder<T> builder) {
		this.elements = requireNonNull(builder.elements);
	}

	public Set<T> getElements() {
		return Collections.unmodifiableSet(elements);
	}

	public T getRoot() {
		T rootElement = null;

		if (!elements.isEmpty()) {
			List<T> result = new ArrayList<T>(elements);
			sortByLeftBound(result);
			rootElement = result.get(0);
		}

		return rootElement;
	}

	public List<T> getChildrenOf(T parent) {
		List<T> descendants = getDescendantsOf(parent);

		List<T> children = new ArrayList<T>();
		for (T descendant : descendants) {
			if (parent.equals(getParentOf(descendant))) {
				children.add(descendant);
			}
		}
		return children;
	}

	public T getParentOf(T child) {
		T parent = null;
		List<T> ancestors = getAncestorsOf(child);
		if (!ancestors.isEmpty()) {
			sortByDistance(ancestors);
			parent = ancestors.get(0);
		}

		return parent;
	}

	public List<T> getDescendantsOf(T parent) {
		List<T> descendants = new ArrayList<T>();

		List<T> sortedElements = new ArrayList<T>(elements);
		sortByLeftBound(sortedElements);

		for (T element : sortedElements) {
			if (element.getBound().isDescendantOf(parent.getBound())) {
				descendants.add(element);
			}
		}

		return descendants;
	}

	public List<T> getAncestorsOf(T child) {
		List<T> ancestors = new ArrayList<T>();

		List<T> sortedElements = new ArrayList<T>(elements);
		sortByLeftBound(sortedElements);

		for (T element : sortedElements) {
			if (element.getBound().isAncestorOf(child.getBound())) {
				ancestors.add(element);
			}
		}

		return ancestors;
	}

	public void removeChild(T child) {
		NestedSetBound childBound = child.getBound();
		int distanceShift = -2;

		Set<T> subSet = subSet(child.getBound());
		elements.removeAll(subSet);

		move(distanceShift, childBound);
	}

	public void addRoot(T root) {
		int distanceShift = 1;
		for (NestedSetElement element : elements) {
			NestedSetBound elementBound = element.getBound();
			element.setBound(elementBound.move(distanceShift));
		}

		elements.add(root);

		NestedSetBound rootBound = new NestedSetBound(1, elements.size() * 2);
		root.setBound(rootBound);
	}

	public void addChild(T parent, T child) {
		NestedSetBound parentBound = parent.getBound();

		int distanceShift = 2;
		move(distanceShift, parentBound);

		int parentRight = parentBound.getRight();

		NestedSetBound newChildBound = new NestedSetBound(parentRight++, parentRight++);
		NestedSetBound newParentBound = parentBound.moveRight(parentRight++);

		child.setBound(newChildBound);
		parent.setBound(newParentBound);

		elements.add(child);
	}

	private void move(int distanceShift, NestedSetBound bound) {
		for (T element : elements) {
			NestedSetBound elementBound = element.getBound();

			int newLeft = elementBound.getLeft();
			int newRight = elementBound.getRight();

			if (elementBound.getLeft() > bound.getRight()) {
				newLeft = elementBound.getLeft() + distanceShift;
			}

			if (elementBound.getRight() > bound.getRight()) {
				newRight = elementBound.getRight() + distanceShift;
			}

			NestedSetBound newBound = new NestedSetBound(newLeft, newRight);
			element.setBound(newBound);
		}
	}

	private Set<T> subSet(NestedSetBound bound) {
		Set<T> result = new HashSet<T>();
		for (T element : elements) {

			if (element.getBound().isSelf(bound)) {
				result.add(element);
			}

			if (element.getBound().isDescendantOf(bound)) {
				result.add(element);
			}
		}
		return result;
	}

	private void sortByDistance(List<T> elements) {
		Collections.sort(elements, new Comparator<T>() {

			@Override
			public int compare(NestedSetElement e1, NestedSetElement e2) {
				NestedSetBound b1 = e1.getBound();
				NestedSetBound b2 = e2.getBound();

				Integer distance = b1.getDistance();
				Integer otherDistance = b2.getDistance();

				return distance.compareTo(otherDistance);
			}

		});
	};

	private void sortByLeftBound(List<T> elements) {
		Collections.sort(elements, new Comparator<T>() {

			@Override
			public int compare(NestedSetElement e1, NestedSetElement e2) {
				NestedSetBound b1 = e1.getBound();
				NestedSetBound b2 = e2.getBound();

				Integer left = b1.getLeft();
				Integer otherLeft = b2.getLeft();

				return left.compareTo(otherLeft);
			}

		});
	};

	public static class Builder<T extends NestedSetElement> {

		private Set<T> elements = new HashSet<>();

		@SafeVarargs
		public final Builder<T> withElements(T... elements) {
			for (T element : elements) {
				this.elements.add(element);
			}
			return this;
		}

		public NestedSet<T> build() {
			return new NestedSet<T>(this);
		}

	}

}
