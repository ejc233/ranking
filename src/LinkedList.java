/**
 * Basic implementation of a linked list.
 * 
 * @author echan
 */
public class LinkedList {

	/**
	 * Inner node class.
	 */
	private class Node {

		private String data;
		Node next, prev;

		public Node(String data) {
			this.data = data;
		}
	}

	private Node head, last;
	private int size;

	/**
	 * Constructor.
	 */
	public LinkedList() {
		this.size = 0;
	}

	/**
	 * Adds a string to the beginning of the linked list.
	 * 
	 * @param data
	 */
	public void addToBeginning(String data) {
		Node temp = new Node(data);
		if (head == null) {
			head = temp;
			last = head;
		} else {
			temp.next = head;
			head.prev = temp;
			head = temp;
		}
		size++;
	}
	
	/**
	 * Add a string to the end of the linked list.
	 * 
	 * @param data
	 */

	public void add(String data) {
		Node temp = new Node(data);
		if (head == null) {
			head = temp;
			last = head;
		} else {
			last.next = temp;
			temp.prev = last;
			last = temp;
		}
		size++;
	}

	public void add(String data, int index) {
		Node temp = new Node(data);
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		} else if (index == 0) {
			addToBeginning(data);
		} else if (index == size) {
			add(data);
		} else {
			Node current = head;
			for (int i = 0; i < index; i++) {
				current = current.next;
			}
			Node previous = current.prev;
			previous.next = temp;
			temp.prev = previous;
			temp.next = current;
			current.prev = temp;
			size++;
		}
	}

	public String get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		} else {
			Node current = head;
			for (int i = 0; i < index; i++) {
				current = current.next;
			}
			return current.data;
		}
	}

	public void delete(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		} else if (index == 0) {
			head = head.next;
			head.prev = null;
			size--;
		} else if (index == size - 1) {
			last = last.prev;
			last.next = null;
			size--;
		} else {
			Node current = head;
			for (int i = 0; i < index; i++) {
				current = current.next;
			}
			Node previous = current.prev;
			Node next = current.next;
			previous.next = current.next;
			next.prev = previous;
			size--;
		}
	}

	public int getSize() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}
}