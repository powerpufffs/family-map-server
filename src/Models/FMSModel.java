package Models;

import java.util.List;

public abstract class FMSModel {
    public abstract boolean requiredFieldsAreNotNull();
    protected <T>boolean allAreNotNull(List<T> items) {
        for(T item : items) {
            if(item == null) return false;
        }
        return true;
    }
}
