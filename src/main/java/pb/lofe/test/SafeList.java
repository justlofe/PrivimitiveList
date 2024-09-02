package pb.lofe.test;

import java.util.function.Consumer;

/**
 * SafeList is a zero-import list which creates a list, where null values can not be saved.
 * <p>
 * Example:
 * <pre>
 * {@code
 * SafeList<String> list = new SafeList()<>;
 * list.add("foo");
 * list.add("bar");
 *
 * list.remove("foo");
 * list.get(0) // Will return "bar", because of "foo" value deleted from the list.
 * }
 * </pre>
 *
 * @param <T> Type of list will be created
 */
@SuppressWarnings("unchecked")
public final class SafeList<T> {

    private Object[] array;
    private int length;

    public SafeList() {
        init(8, null);
    }

    public SafeList(int capacity) {
        init(capacity, null);
    }

    /**
     * @param base Array of values to be added in list
     * @throws RuntimeException If base array is null
     */
    public SafeList(T[] base) {
        Checks.check(base != null, "Initial array cannot be null!");
        int baseLength = base.length;
        if(baseLength == 0) {
            init(8, null);
            return;
        }

        Object[] temp = new Object[baseLength];
        int lastWrittenIndex = -1;
        for (T t : base) {
            if (t == null) continue;
            lastWrittenIndex++;
            temp[lastWrittenIndex] = t;
        }
        int resLength = lastWrittenIndex + 1;
        Object[] result = new Object[resLength];
        System.arraycopy(temp, 0, result, 0, resLength);
        init(resLength, result);
    }

    /**
     * @param capacity Will be the same as the values array length if array presented
     * @param values Values that will be copied to list
     */
    private void init(int capacity, Object[] values) {
        Checks.check(capacity > 0, "Initial capacity can't be less or equals zero.");
        Checks.check(values == null || values.length == capacity, "Input array length can't be different with input capacity.");
        array = new Object[capacity];
        length = capacity;
        if(values != null) System.arraycopy(values, 0, array, 0, capacity);
    }

    /**
     * @return Count of stored values.
     */
    public int size() {
        if(array == null) return 0;
        return getNearestEmptyIndex();
    }

    /**
     * Returns a value index in the list.
     * @param element Value to check
     * @return Value index, or -1 if value is not stored in the list
     */
    public int indexOf(T element) {
        for (int i = 0; i < array.length; i++) {
            if(array[i].equals(element)) return i;
        }
        return -1;
    }

    /**
     * @param index Index of value
     * @return Assigned value, or null if value ot of bounds
     */
    public T get(int index) {
        if(size() < index || index < 0) return null;
        return (T) array[index];
    }

    /**
     * Store's value in list
     * @return True if value is added, false if value is null
     */
    public boolean add(T value) {
        if(value == null) return false;
        if(getRemainingSize() < 1) expandArray(9);
        array[getNearestEmptyIndex()] = value;
        return true;
    }

    /**
     * Store's values in list
     * @return Does something changed in a list
     */
    @SafeVarargs public final boolean addAll(T element, T... elements) {
        SafeList<T> list = new SafeList<>();
        list.add(element);
        list.addAll(new SafeList<>(elements));
        return addAll(list);
    }

    /**
     * @return Does something changed in a list
     */
    public boolean addAll(SafeList<T> elements) {
        Checks.check(elements != null, "Input values cannot be null");
        if(elements.length == 0) return false;

        int remainingSize = getRemainingSize();
        if(remainingSize < elements.length) {
            int requiredSize = elements.length - getRemainingSize();
            expandArray(requiredSize + 8);
        }

        System.arraycopy(elements.array, 0, array, getNearestEmptyIndex(), elements.length);
        return true;
    }

    /**
     * Clears the entirely list
     */
    public void clear() {
        init(8, null);
    }


    /**
     * Runs a function to every element in the list.
     * @param function Function to run
     */
    public void forEach(Consumer<T> function) {
        for (Object element: array) {
            if(element == null) continue;
            function.accept((T) element);
        }
    }

    /**
     * @param element Value to be deleted
     * @return Deleted value, or null if value does not found in the list.
     */
    public T remove(T element) {
        int index = indexOf(element);
        if(index == -1) return null;
        return remove(index);
    }

    /**
     * @param index Index of value to be deleted
     * @return Deleted value, or null if value does not found in the list.
     */
    public T remove(int index) {
        Checks.check(index < length, "Index out of bounds.");
        Object element = array[index];
        if(element == null) return null;

        int elementsToCopy = 0;
        for (int i = index + 1; i < length; i++) {
            if(array[i] == null) break;
            elementsToCopy++;
        }


        length--;
        Object[] temp = new Object[length - 1];
        System.arraycopy(array, 0, temp, 0, elementsToCopy);
        if(index < array.length - 1) System.arraycopy(array, index + 1, temp, index, elementsToCopy);
        array = temp;
        return (T) element;
    }

    private int getNearestEmptyIndex() {
        for (int i = 0; i < length; i++) {
            if(array[i] != null) continue;
            return i;
        }
        return -1;
    }

    /**
     * Expands an array where elements are stored.
     * @param addSize The size by which array will be expanded
     */
    private void expandArray(int addSize) {
        int prevLength = length;
        length += addSize;
        Object[] newArray = new Object[length];
        System.arraycopy(array, 0, newArray, 0, prevLength);
        array = newArray;
    }

    public int getRemainingSize() {
        for (int i = length - 1; i > 0; i--) {
            if(array[i] != null) return length - i;
        }
        return length;
    }

    public java.util.List<T> toJavaUtilList() {
        java.util.List<T> arrayList = new java.util.ArrayList<>(length);
        for (Object o: array) {
            if(o != null) arrayList.add((T) o);
        }
        return arrayList;
    }

    @Override
    public String toString() {
        return String.format("PrimitiveList{size=%s}", size());
    }

}