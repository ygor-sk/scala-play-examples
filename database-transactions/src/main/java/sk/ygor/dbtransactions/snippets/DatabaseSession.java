package sk.ygor.dbtransactions.snippets;

import java.util.List;

public interface DatabaseSession {

    <T> void insertObject(T object);

    <T> void updateObject(T object);

    <T> void deleteObject(T object);

    <T> List<T> listObjects(Criteria criteria);
}
