/*
 * Copyright © 2021  黄川 Rights Reserved.
 * 版权声明：黄川保留所有权利。
 * 免责声明：本规范是初步的，随时可能更改，恕不另行通知。黄川对此处包含的任何错误不承担任何责任。
 * 最后修改时间：2021/04/04 23:12:04
 *
 */

package cn.xjbpm.ultron.mybaitsplus.expand.util;

import cn.xjbpm.ultron.common.vo.PageReqVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@UtilityClass
public class PageUtil {

	public static <T> IPage<T> getPage(PageReqVO reqVO) {
		return new Page<>(reqVO.getCurrent(), reqVO.getPageSize());
	}

}
