package repository;

import java.util.List;

public interface AbstractFilter<T> {
    List<T> filter(List<T> entities);
}