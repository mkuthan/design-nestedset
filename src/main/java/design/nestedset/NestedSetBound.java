package design.nestedset;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

public class NestedSetBound {

	private final static int DEFAULT_LEFT = 1;
	private final static int DEFAULT_RIGHT = 2;

	private int left;

	private int right;

	public NestedSetBound() {
		this(DEFAULT_LEFT, DEFAULT_RIGHT);
	}

	public NestedSetBound(int left, int right) {
		if (left <= 0 || right <= 0) {
			throw new IllegalArgumentException("Bounds must be positive [left=" + left + ",right=" + right);
		}

		if (left >= right) {
			throw new IllegalArgumentException("Right bound must be greater than left [left=" + left + ",right="
					+ right);
		}

		this.left = left;
		this.right = right;
	}

	public int getDistance() {
		return right - left;
	}

	public boolean isDescendantOf(NestedSetBound ancestor) {
		requireNonNull(ancestor);
		return right < ancestor.right && left > ancestor.left;
	}

	public boolean isAncestorOf(NestedSetBound descendant) {
		requireNonNull(descendant);
		return right > descendant.right && left < descendant.left;
	}

	public boolean isSelf(NestedSetBound that) {
		requireNonNull(that);
		return equals(that);
	}

	public boolean hasParent() {
		return left > 1;
	}

	public boolean hasChildren() {
		return right > left + 1;
	}

	public NestedSetBound moveRight(int newRight) {
		return new NestedSetBound(left, newRight);
	}

	public NestedSetBound moveLeft(int newLeft) {
		return new NestedSetBound(newLeft, right);
	}

	public NestedSetBound move(int distance) {
		return new NestedSetBound(left + distance, right + distance);
	}

	@Override
	public int hashCode() {
		return Objects.hash(left, right);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		NestedSetBound that = (NestedSetBound) obj;
		return Objects.equals(this.left, this.left) && Objects.equals(this.right, that.right);
	}

	public String toString() {
		return "NestedSetBound[" + left + "," + right + "]";
	}

	int getLeft() {
		return left;
	}

	int getRight() {
		return right;
	}

}
