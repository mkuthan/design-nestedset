# Nested Set Design Pattern

[![Build Status](https://travis-ci.org/mkuthan/design-nestedset.png)](https://travis-ci.org/mkuthan/design-nestedset) [![Coverage Status](https://coveralls.io/repos/mkuthan/design-nestedset/badge.png)](https://coveralls.io/r/mkuthan/design-nestedset)

[Nested Set](http://en.wikipedia.org/wiki/Nested_set_model) is a design pattern for representing hierarchies in relational databases. 

The primary goal of this project is to provide reusable implementation of this pattern. 

## Example

Below you can find example JPA mapping using nested set implementation. Enjoy!

	@Entity
	public class Tree {

		@OneToMany
		private Set<Node> nodes = new HashSet<>();

		public NestedSet<Node> asNestedSet() {
			return new NestedSet<Node>(nodes);
		}

		public Node getRootComponent() {
			return asNestedSet().getRoot();
		}
	}

	@Entity
	public class Node implements NestedSetElement {
		@ManyToOne
		@JoinColumn
		private Tree tree;

		@Embedded
		private NestedSetBound bound = new NestedSetBound();

		@Override
		public NestedSetBound getBound() {
			return bound;
		}

		@Override
		public void setBound(NestedSetBound bound) {
			this.bound = bound;
		}

		public MetadataComponent getParent() {
			return tree.asNestedSet().getParentOf(this);
		}

		public final List<MetadataComponent> getChildren() {
			return tree.asNestedSet().getChildrenOf(this);
		}
	}
