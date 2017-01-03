public class MyTreeMap<K, V> {
	private transient Entry<K, V> root = null;
	private transient int size = 0;
	private transient int modCount = 0;

	public MyTreeMap() {
	}

	public int size() {
		return size;
	}

	public V get(Object key) {
		Entry<K, V> p = getEntry(key);
		return (p == null ? null : p.value);
	}

	public Entry<K, V> getEntry(Object key) {
		if (key == null)
			throw new NullPointerException();
		Comparable<? super K> k = (Comparable<? super K>) key;
		Entry<K, V> p = root;
		while (p != null) {
			int cmp = k.compareTo(p.key);
			if (cmp < 0)
				p = p.left;
			else if (cmp > 0)
				p = p.right;
			else
				return p;
		}
		return null;
	}

	public K getCeiling(K key) {
		Entry<K, V> entry = getCeilingEntry(key);
		if (entry != null) {
			return entry.key;
		}
		return null;
	}

	final Entry<K, V> getCeilingEntry(K key) {
		Entry<K, V> p = root;
		while (p != null) {
			int cmp = compare(key, p.key);
			if (cmp < 0) {
				if (p.left != null)
					p = p.left;
				else
					return p;
			} else if (cmp > 0) {
				if (p.right != null) {
					p = p.right;
				} else {
					Entry<K, V> parent = p.parent;
					Entry<K, V> ch = p;
					while (parent != null && ch == parent.right) {
						ch = parent;
						parent = parent.parent;
					}
					return parent;
				}
			} else
				return p;
		}
		return null;
	}

	public int getRank(K key) {
		Entry<K, V> entry = getEntry(key);
		int r = 1;
		if (leftOf(entry) != null) {
			r += leftOf(entry).getSize();
		}
		Entry<K, V> y = entry;
		while (compare(y.key, root.key) != 0) {
			if (rightOf(parentOf(y)) != null) {
				if (compare(y.key, rightOf(parentOf(y)).key) == 0) {
					if (leftOf(parentOf(y)) != null) {
						r = r + leftOf(parentOf(y)).getSize() + 1;
					} else {
						r++;
					}
				}
			}
			y = parentOf(y);
		}
		return r;
	}

	public V put(K key, V value) {
		Entry<K, V> t = root;
		if (t == null) {
			compare(key, key); // type (and possibly null) check

			root = new Entry<K, V>(key, value, null);
			size = 1;
			root.setSize(1);
			modCount++;
			return null;
		}
		int cmp;
		Entry<K, V> parent;
		if (key == null)
			throw new NullPointerException();
		Comparable<? super K> k = (Comparable<? super K>) key;
		do {
			parent = t;
			parent.setSize(parent.getSize() + 1);
			cmp = k.compareTo(t.key);
			if (cmp < 0)
				t = t.left;
			else if (cmp > 0)
				t = t.right;
			else
				return t.setValue(value);
		} while (t != null);

		Entry<K, V> e = new Entry<K, V>(key, value, parent);
		if (cmp < 0)
			parent.left = e;
		else
			parent.right = e;
		e.setSize(1);
		fixAfterInsertion(e);
		size++;
		modCount++;
		return null;
	}

	public V remove(Object key) {
		Entry<K, V> p = getEntry(key);
		if (p == null)
			return null;

		V oldValue = p.value;
		deleteEntry(p);
		return oldValue;
	}

	class Values {
		public int size() {
			return MyTreeMap.this.size();
		}

		public boolean remove(Object o) {
			for (Entry<K, V> e = getFirstEntry(); e != null; e = successor(e)) {
				if (valEquals(e.getValue(), o)) {
					deleteEntry(e);
					return true;
				}
			}
			return false;
		}
	}

	final int compare(Object k1, Object k2) {
		return ((Comparable<? super K>) k1).compareTo((K) k2);
	}

	final boolean valEquals(Object o1, Object o2) {
		return (o1 == null ? o2 == null : o1.equals(o2));
	}

	private static final boolean RED = false;
	private static final boolean BLACK = true;

	final class Entry<K, V> {
		K key;
		V value;
		Entry<K, V> left = null;
		Entry<K, V> right = null;
		int size = 1;
		Entry<K, V> parent;
		boolean color = BLACK;

		Entry(K key, V value, Entry<K, V> parent) {
			this.key = key;
			this.value = value;
			this.parent = parent;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		public V setValue(V value) {
			V oldValue = this.value;
			this.value = value;
			return oldValue;
		}

		public boolean equals(Object o) {
			if (!(o instanceof Entry))
				return false;
			Entry<?, ?> e = (Entry<?, ?>) o;

			return valEquals(key, e.getKey()) && valEquals(value, e.getValue());
		}

		public int hashCode() {
			int keyHash = (key == null ? 0 : key.hashCode());
			int valueHash = (value == null ? 0 : value.hashCode());
			return keyHash ^ valueHash;
		}

		public String toString() {
			return key + "=" + value;
		}
	}

	final Entry<K, V> getFirstEntry() {
		Entry<K, V> p = root;
		if (p != null)
			while (p.left != null)
				p = p.left;
		return p;
	}

	final Entry<K, V> getLastEntry() {
		Entry<K, V> p = root;
		if (p != null)
			while (p.right != null)
				p = p.right;
		return p;
	}

	MyTreeMap<K, V>.Entry<K, V> successor(Entry<K, V> t) {
		if (t == null)
			return null;
		else if (t.right != null) {
			Entry<K, V> p = t.right;
			while (p.left != null)
				p = p.left;
			return p;
		} else {
			Entry<K, V> p = t.parent;
			Entry<K, V> ch = t;
			while (p != null && ch == p.right) {
				ch = p;
				p = p.parent;
			}
			return p;
		}
	}

	<K, V> Entry<K, V> predecessor(Entry<K, V> t) {
		if (t == null)
			return null;
		else if (t.left != null) {
			Entry<K, V> p = t.left;
			while (p.right != null)
				p = p.right;
			return p;
		} else {
			Entry<K, V> p = t.parent;
			Entry<K, V> ch = t;
			while (p != null && ch == p.left) {
				ch = p;
				p = p.parent;
			}
			return p;
		}
	}

	private <K, V> boolean colorOf(Entry<K, V> p) {
		return (p == null ? BLACK : p.color);
	}

	private <K, V> Entry<K, V> parentOf(Entry<K, V> p) {
		return (p == null ? null : p.parent);
	}

	private <K, V> void setColor(Entry<K, V> p, boolean c) {
		if (p != null)
			p.color = c;
	}

	private <K, V> Entry<K, V> leftOf(Entry<K, V> p) {
		return (p == null) ? null : p.left;
	}

	private <K, V> Entry<K, V> rightOf(Entry<K, V> p) {
		return (p == null) ? null : p.right;
	}

	/** From CLR */
	private void rotateLeft(Entry<K, V> p) {
		if (p != null) {
			Entry<K, V> r = p.right;
			p.right = r.left;
			if (r.left != null)
				r.left.parent = p;
			r.parent = p.parent;
			if (p.parent == null)
				root = r;
			else if (p.parent.left == p)
				p.parent.left = r;
			else
				p.parent.right = r;
			r.left = p;
			p.parent = r;
			p.setSize(1);
			r.setSize(1);
			if (p.left != null) {
				p.setSize(p.left.getSize() + p.getSize());
			}
			if (p.right != null) {
				p.setSize(p.right.getSize() + p.getSize());
			}
			if (r.left != null) {
				r.setSize(r.left.getSize() + r.getSize());
			}
			if (r.right != null) {
				r.setSize(r.right.getSize() + r.getSize());
			}
		}
	}

	/** From CLR */
	private void rotateRight(Entry<K, V> p) {
		if (p != null) {
			Entry<K, V> l = p.left;
			p.left = l.right;
			if (l.right != null)
				l.right.parent = p;
			l.parent = p.parent;
			if (p.parent == null)
				root = l;
			else if (p.parent.right == p)
				p.parent.right = l;
			else
				p.parent.left = l;
			l.right = p;
			p.parent = l;
			p.setSize(1);
			l.setSize(1);
			if (p.left != null) {
				p.setSize(p.left.getSize() + p.getSize());
			}
			if (p.right != null) {
				p.setSize(p.right.getSize() + p.getSize());
			}
			if (l.left != null) {
				l.setSize(l.left.getSize() + l.getSize());
			}
			if (l.right != null) {
				l.setSize(l.right.getSize() + l.getSize());
			}
		}
	}

	/** From CLR */
	private void fixAfterInsertion(Entry<K, V> x) {
		x.color = RED;

		while (x != null && x != root && x.parent.color == RED) {
			if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
				Entry<K, V> y = rightOf(parentOf(parentOf(x)));
				if (colorOf(y) == RED) {
					setColor(parentOf(x), BLACK);
					setColor(y, BLACK);
					setColor(parentOf(parentOf(x)), RED);
					x = parentOf(parentOf(x));
				} else {
					if (x == rightOf(parentOf(x))) {
						x = parentOf(x);
						rotateLeft(x);
					}
					setColor(parentOf(x), BLACK);
					setColor(parentOf(parentOf(x)), RED);
					rotateRight(parentOf(parentOf(x)));
				}
			} else {
				Entry<K, V> y = leftOf(parentOf(parentOf(x)));
				if (colorOf(y) == RED) {
					setColor(parentOf(x), BLACK);
					setColor(y, BLACK);
					setColor(parentOf(parentOf(x)), RED);
					x = parentOf(parentOf(x));
				} else {
					if (x == leftOf(parentOf(x))) {
						x = parentOf(x);
						rotateRight(x);
					}
					setColor(parentOf(x), BLACK);
					setColor(parentOf(parentOf(x)), RED);
					rotateLeft(parentOf(parentOf(x)));
				}
			}
		}
		root.color = BLACK;
	}

	/**
	 * Delete node p, and then rebalance the tree.
	 */
	private void deleteEntry(Entry<K, V> p) {
		modCount++;
		size--;

		// If strictly internal, copy successor's element to p and then make
		// p
		// point to successor.
		if (p.left != null && p.right != null) {
			Entry<K, V> s = successor(p);
			p.key = s.key;
			p.value = s.value;
			p = s;
		} // p has 2 children

		Entry<K, V> copy = p;
		while (copy != null) {
			copy.setSize(copy.getSize() - 1);
			copy = parentOf(copy);
		}
		// Start fixup at replacement node, if it exists.
		Entry<K, V> replacement = (p.left != null ? p.left : p.right);

		if (replacement != null) {
			// Link replacement to parent
			replacement.parent = p.parent;
			if (p.parent == null)
				root = replacement;
			else if (p == p.parent.left)
				p.parent.left = replacement;
			else
				p.parent.right = replacement;

			// Null out links so they are OK to use by fixAfterDeletion.
			p.left = p.right = p.parent = null;

			// Fix replacement
			if (p.color == BLACK)
				fixAfterDeletion(replacement);
		} else if (p.parent == null) { // return if we are the only node.
			root = null;
		} else { // No children. Use self as phantom replacement and unlink.
			if (p.color == BLACK)
				fixAfterDeletion(p);

			if (p.parent != null) {
				if (p == p.parent.left)
					p.parent.left = null;
				else if (p == p.parent.right)
					p.parent.right = null;
				p.parent = null;
			}
		}
	}

	/** From CLR */
	private void fixAfterDeletion(Entry<K, V> x) {
		while (x != root && colorOf(x) == BLACK) {
			if (x == leftOf(parentOf(x))) {
				Entry<K, V> sib = rightOf(parentOf(x));

				if (colorOf(sib) == RED) {
					setColor(sib, BLACK);
					setColor(parentOf(x), RED);
					rotateLeft(parentOf(x));
					sib = rightOf(parentOf(x));
				}

				if (colorOf(leftOf(sib)) == BLACK && colorOf(rightOf(sib)) == BLACK) {
					setColor(sib, RED);
					x = parentOf(x);
				} else {
					if (colorOf(rightOf(sib)) == BLACK) {
						setColor(leftOf(sib), BLACK);
						setColor(sib, RED);
						rotateRight(sib);
						sib = rightOf(parentOf(x));
					}
					setColor(sib, colorOf(parentOf(x)));
					setColor(parentOf(x), BLACK);
					setColor(rightOf(sib), BLACK);
					rotateLeft(parentOf(x));
					x = root;
				}
			} else { // symmetric
				Entry<K, V> sib = leftOf(parentOf(x));

				if (colorOf(sib) == RED) {
					setColor(sib, BLACK);
					setColor(parentOf(x), RED);
					rotateRight(parentOf(x));
					sib = leftOf(parentOf(x));
				}

				if (colorOf(rightOf(sib)) == BLACK && colorOf(leftOf(sib)) == BLACK) {
					setColor(sib, RED);
					x = parentOf(x);
				} else {
					if (colorOf(leftOf(sib)) == BLACK) {
						setColor(rightOf(sib), BLACK);
						setColor(sib, RED);
						rotateLeft(sib);
						sib = leftOf(parentOf(x));
					}
					setColor(sib, colorOf(parentOf(x)));
					setColor(parentOf(x), BLACK);
					setColor(leftOf(sib), BLACK);
					rotateRight(parentOf(x));
					x = root;
				}
			}
		}

		setColor(x, BLACK);
	}

}
