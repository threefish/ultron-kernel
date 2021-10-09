/*
 * Copyright © 2021  黄川 Rights Reserved.
 * 版权声明：黄川保留所有权利。
 * 免责声明：本规范是初步的，随时可能更改，恕不另行通知。黄川对此处包含的任何错误不承担任何责任。
 * 最后修改时间：2021/04/04 23:12:04
 *
 */

package cn.xjbpm.ultron.mybaitsplus.component.generator;

import cn.xjbpm.ultron.id.generator.util.GlobalIdServiceUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import lombok.RequiredArgsConstructor;

/**
 * @author 黄川 huchuc@vip.qq.com 2020/12/4 mybaits全局实体类ID生成器
 * @TableId(type = IdType.ASSIGN_ID)
 */
@RequiredArgsConstructor
public class GlobalAssignIdentifierGenerator implements IdentifierGenerator {
    /**
     * 生产一个Number类型的全局ID
     *
     * @param entity
     * @return
     */
    @Override
    public Number nextId(Object entity) {
        return GlobalIdServiceUtil.nextLongId();
    }

    /**
     * 生产一个字符串的全局ID
     *
     * @param entity
     * @return
     */
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @Override
    public String nextUUID(Object entity) {
        return GlobalIdServiceUtil.nextStringId();
    }

}
