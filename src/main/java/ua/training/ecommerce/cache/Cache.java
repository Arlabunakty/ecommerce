package ua.training.ecommerce.cache;

import java.util.Collection;

public interface Cache {
    <T> T getItem(String key, Class<T> type);

    <T> void setItem(String key, T item);

    void removeItem(String key);

    <T> Collection<T> getList(String key, Class<T> type);

    <T> Collection<T> addItemToList(String key, T item, Class<T> type);

    <T> Collection<T> removeItemFromList(String key, T item, Class<T> type);
}
