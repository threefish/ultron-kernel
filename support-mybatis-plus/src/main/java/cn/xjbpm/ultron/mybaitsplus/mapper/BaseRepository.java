package cn.xjbpm.ultron.mybaitsplus.mapper;

import cn.xjbpm.ultron.mybaitsplus.expand.util.MyBatisQueryUtil;
import cn.xjbpm.ultron.mybaitsplus.mapper.functional.LambdQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author 黄川 huchuc@vip.qq.com date: 2021/6/20
 */
public class BaseRepository<Mapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<DO>, DO>
		extends ServiceImpl<Mapper, DO> implements IService<DO> {

	/**
	 * 查询单个对象
	 * <p>
	 * 例子：等价查询 final Optional<AccountDO> optionalAccount = accountRepository.findOne(query
	 * -> query.eq(AccountDO::getUserName, userName)); final Optional<AccountDO>
	 * optionalAccount = accountRepository.lambdaQuery().eq(AccountDO::getUserName,
	 * userName).oneOpt();
	 * @param lambdQuery
	 * @return
	 */
	public Optional<DO> findOne(LambdQueryWrapper<DO> lambdQuery) {
		return lambdQuery.lambdaQueryChainWrapper(this.lambdaQuery()).oneOpt();
	}

	/**
	 * 查询一个集合
	 * @param lambdQuery
	 * @return
	 */
	public List<DO> list(LambdQueryWrapper<DO> lambdQuery) {
		return lambdQuery.lambdaQueryChainWrapper(this.lambdaQuery()).list();
	}

	/**
	 * 获取一个对象
	 * @param id
	 * @return
	 */
	public Optional<DO> findById(Serializable id) {
		return Optional.ofNullable(super.getById(id));
	}

	/**
	 * 根据对象进行查询
	 * @param beanParam
	 * @return
	 */
	public Page<DO> findAllOfBean(Object beanParam) {
		return new PageImpl<DO>(MyBatisQueryUtil.toQueryChainWrapper(this.query(), beanParam).list());
	}

	@Override
	protected Class<DO> currentMapperClass() {
		return (Class<DO>) this.getResolvableType().as(BaseRepository.class).getGeneric(0).getType();
	}

	@Override
	protected Class<DO> currentModelClass() {
		return (Class<DO>) this.getResolvableType().as(BaseRepository.class).getGeneric(1).getType();
	}

}
