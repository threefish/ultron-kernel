/*
 * Copyright © 2021  黄川 Rights Reserved.
 * 版权声明：黄川保留所有权利。
 * 免责声明：本规范是初步的，随时可能更改，恕不另行通知。黄川对此处包含的任何错误不承担任何责任。
 * 最后修改时间：2021/04/04 23:12:04
 *
 */

package cn.xjbpm.ultron.mybaitsplus.component.handler;

import cn.xjbpm.common.util.LoginUserUtil;
import cn.xjbpm.common.vo.OperatorUserVO;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * @author 黄川 huchuc@vip.qq.com 2020/12/2
 */
@RequiredArgsConstructor
public class BaseMetaObjectHandler implements MetaObjectHandler {

	private static final String FIELD_DELETED = "deleted";

	private static final String FIELD_CREATE_DATE = "createDate";

	private static final String FIELD_CREATED_BY = "createdBy";

	private static final String FIELD_LAST_MODIFIED_DATE = "lastModifiedDate";

	private static final String FIELD_LAST_MODIFIED_BY = "lastModifiedBy";

	/**
	 * 如果数据有值就不填充
	 * @param metaObject
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		Optional<OperatorUserVO> optionalOperator = LoginUserUtil.getOptionalOperator();
		if (fieldHasGetterAndSetter(metaObject, FIELD_DELETED)) {
			if (Objects.isNull(metaObject.getValue(FIELD_DELETED))) {
				metaObject.setValue(FIELD_DELETED, 0);
			}
		}
		if (fieldHasGetterAndSetter(metaObject, FIELD_CREATE_DATE)) {
			if (Objects.isNull(metaObject.getValue(FIELD_CREATE_DATE))) {
				metaObject.setValue(FIELD_CREATE_DATE, LocalDateTime.now());
			}
		}
		if (fieldHasGetterAndSetter(metaObject, FIELD_CREATED_BY)) {
			if (Objects.isNull(metaObject.getValue(FIELD_CREATED_BY))) {
				optionalOperator.ifPresent(operator -> metaObject.setValue(FIELD_CREATED_BY, operator.getUserName()));
			}
		}
	}

	/**
	 * 无论数据是否有值都进行填充
	 * @param metaObject
	 */
	@Override
	public void updateFill(MetaObject metaObject) {
		Optional<OperatorUserVO> optionalOperator = LoginUserUtil.getOptionalOperator();
		if (fieldHasGetterAndSetter(metaObject, FIELD_LAST_MODIFIED_DATE)) {
			metaObject.setValue(FIELD_LAST_MODIFIED_DATE, LocalDateTime.now());
		}
		if (fieldHasGetterAndSetter(metaObject, FIELD_LAST_MODIFIED_BY)) {
			optionalOperator.ifPresent(operator -> metaObject.setValue(FIELD_LAST_MODIFIED_BY, operator.getUserName()));
		}
	}

	private boolean fieldHasGetterAndSetter(MetaObject metaObject, String fieldName) {
		return metaObject.hasGetter(fieldName) && metaObject.hasSetter(fieldName);
	}

}
