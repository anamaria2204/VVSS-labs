package drinkshop.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public abstract class AbstractRepository<ID, T>
        implements Repository<ID, T> {

    protected Map<ID, T> entities = new HashMap<>();

    @Override
    public T findOne(ID id) {
        return entities.get(id);
    }

    @Override
    public List<T> findAll() {
        return StreamSupport.stream(entities.values().spliterator(), false).toList();
    }

    @Override
    public T save(T entity) {
        entities.put(getId(entity), entity);
        return entity;
    }

    @Override
    public T delete(ID id) {
        return entities.remove(id);
    }

    @Override
    public T update(T entity) {
        return save(entity);
    }

    protected abstract ID getId(T entity);
}
