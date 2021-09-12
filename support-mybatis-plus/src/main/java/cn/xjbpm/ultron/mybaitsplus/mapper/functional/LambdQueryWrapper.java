package cn.xjbpm.ultron.mybaitsplus.mapper.functional;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/20
 */
@FunctionalInterface
public interface LambdQueryWrapper<DO> {

	LambdaQueryChainWrapper<DO> lambdaQueryChainWrapper(LambdaQueryChainWrapper<DO> lambdaQueryChainWrapper);

}
