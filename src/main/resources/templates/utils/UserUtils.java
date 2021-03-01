package ${packagePath}.utils;

import ${packagePath}.entity.User;
import ${packagePath}.enums.DeleteState;
import com.google.common.base.Objects;

import javax.security.auth.login.AccountException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/25 14:51
 * description:
 */
public class UserUtils {

    public static void checkUserState(User user) throws AccountException {
        if (Objects.equal(user.getDeleteState(), DeleteState.DELETE.getCode())) {
            throw new AccountException("账户已被删除");
        }
    }

    /**
     * 获取加密密码
     *
     * @param account  账户
     * @param password 未加密密码
     * @param salt     盐
     * @return String
     */
    public static String getEncryptPassword(String account, String password, String salt) {
        byte[] hashPassword = Digests.sha1((account + password).getBytes(), Encodes.decodeHex(salt), Digests.HASH_INTERACTIONS);
        return Encodes.encodeHex(hashPassword);
    }


}
