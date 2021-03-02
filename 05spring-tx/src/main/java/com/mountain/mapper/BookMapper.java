package com.mountain.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BookMapper {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * Transactional:
     * 四大特性：原子性，一致型，隔离性，持久性
     * <p>
     *  事务隔离性
     *  - 并发问题：
     *    - 脏读 -> 事务1修改了value，事务2独取了value，事务1回滚，这时候事务2独取到的值是脏数据
     *    - 幻读 -> 事务1读取了value，事务2修改了value，事务1再次独取value时两次独取不一致
     *    - 不可重复读 -> 两次独取到的数量不一致
     *  - READ_UNCOMMITTED：读未提交，允许事务1读取事务2未提交的修改
     *  - READ_COMMITTED：读已提交，要求事务1只能读取事务2已提交的修改
     *  - REPEATABLE_READ：可重复读，确保事务1可以多次从一个字段中独取到相同的值，即事务1在执行期间巾期间对这个字段进行更新
     *  - SERIALIZABLE：串行化
     * </p>
     *
     * <p>
     * 事务的传播性
     * - propagation：事务的传播行为
     * - Propagation.REQUIRES_NEW 另起一个事务，该事务与主事务无关，
     * - Propagation.REQUIRED 和主事务绑定，若主事务执行异常则一同失败
     * </p>
     * @param id
     */

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.DEFAULT)
    public void updateLeftAmount(int id) {
        jdbcTemplate.update("update bookAmount set amount = amount-5 where id=?", id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateLeftNum(int id) {
        jdbcTemplate.update("update bookNum set num=num-1 where id=?", id);
    }

    // 两个事务都是读，设置只读，可以加快事务处理的速度
    @Transactional(readOnly = true, timeout = 30,
            noRollbackFor = {ClassCastException.class}, noRollbackForClassName = {"java.lang.ArithmeticException"})
    public Double getAveragePrice(int id) {
        Double amount = jdbcTemplate.queryForObject("select amount from bookAmount where id = ?", Double.class);
        Integer num = jdbcTemplate.queryForObject("select num from bookNum where id = ?", Integer.class);
        return amount / num;
    }
}
