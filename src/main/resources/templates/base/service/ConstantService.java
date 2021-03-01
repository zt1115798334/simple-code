package ${packagePath}.base.service;

import ${packagePath}.enums.DeleteState;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 11:40
 * description:
 */
public interface ConstantService {

    /**
     * 未删除
     */
    Integer UN_DELETED = DeleteState.UN_DELETED.getCode();

    /**
     * 删除
     */
    Integer DELETED = DeleteState.DELETE.getCode();
}
