package ${baseServicePackageName};

import com.example.awards.custom.EnabledState;
import com.example.awards.enums.DeleteState;

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

    /**
     * 开启
     */
    Integer ON = EnabledState.ON.getCode();
    /**
     * 停用
     */
    Integer OFF = EnabledState.OFF.getCode();
}
