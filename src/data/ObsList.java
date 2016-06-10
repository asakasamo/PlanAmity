package data;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Al-John
 */
public class ObsList<E> extends AbstractListModel {

    private static final long serialVersionUID = 1L;

    private List<E> delegate = new ArrayList<>();

    @Override
    public int getSize() {
        return delegate.size();
    }

    @Override
    public Object getElementAt(int index) {
        return delegate.get(index);
    }

    public List<E> getList() {
        return delegate;
    }

    @SafeVarargs
    public final void add(E... e){
        int index = delegate.size();
        Collections.addAll(delegate, e);
        fireIntervalAdded(this, index, index + e.length);
    }

    public void add(int idx, E e) {
        delegate.add(idx, e);
        fireIntervalAdded(this, idx, idx);
    }

    public void remove(int idx) {
        delegate.remove(idx);
        fireIntervalRemoved(delegate, idx, idx);
    }

    public void remove(E e){
        int index = delegate.indexOf(e);
        delegate.remove(e);
        fireIntervalRemoved(delegate, index, index);
    }

    public void set(int idx, E e) {
        delegate.set(idx, e);
        fireContentsChanged(delegate, idx, idx);
    }

    public E get(int idx) {
        return delegate.get(idx);
    }

    public int size() {
        return delegate.size();
    }

    public int indexOf(E e) {
        return delegate.indexOf(e);
    }
}