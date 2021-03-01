package ${packagePath}.base.service;

import ${packagePath}.exception.OperationException;
import com.google.common.base.Objects;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 11:40
 * description:
 */
public interface BaseService<T, S, ID, U> extends ConstantService {

    default T save(T t) {
        return null;
    }

    default Iterable<T> saveAll(Iterable<T> t) {
        return t;
    }

    default void deleteById(ID id) {
    }

    default void deleteById(ID id, U userId) {
    }

    default void delete(T t){

    }

    default void deleteById(List<ID> id) {
    }

    default void deleteById(List<ID> id, U userId) {
    }

    default void deleteAll(Iterable<T> t) {
    }

    default Optional<T> findById(ID id) {
        return Optional.empty();
    }

    default List<T> findByIds(List<ID> id) {
        return null;
    }

    default List<T> findByIdsOrder(List<ID> id) {
        return null;
    }

    default Optional<T> findByIdNotDelete(ID id) {
        return Optional.empty();
    }

    default Optional<T> findByIdNotDelete(ID id, U userId) {
        return Optional.empty();
    }

    default List<T> findByIdsNotDelete(List<ID> id) {
        return null;
    }

    default List<T> findByIdsNotDelete(List<ID> id, U userId) {
        return null;
    }

    default List<T> findAll() {
        return null;
    }

    default List<T> findListByEntity(S s) {
        return null;
    }

    default Page<T> findPageByEntity(S s) {
        return null;
    }

    default void verificationNameRepeat(Long id, Long dbId, String msg) {
        if (id != null && id != 0) {
            if (!Objects.equal(dbId, id)) {
                throw new OperationException(msg);
            }
        } else {
            throw new OperationException(msg);
        }
    }

}
