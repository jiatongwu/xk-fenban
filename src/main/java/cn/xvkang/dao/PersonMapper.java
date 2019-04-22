package cn.xvkang.dao;

import cn.xvkang.entity.Person;
import cn.xvkang.entity.PersonExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PersonMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table person
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    long countByExample(PersonExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table person
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    int deleteByExample(PersonExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table person
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table person
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    int insert(Person record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table person
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    int insertSelective(Person record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table person
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    List<Person> selectByExample(PersonExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table person
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    Person selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table person
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    int updateByExampleSelective(@Param("record") Person record, @Param("example") PersonExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table person
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    int updateByExample(@Param("record") Person record, @Param("example") PersonExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table person
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    int updateByPrimaryKeySelective(Person record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table person
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    int updateByPrimaryKey(Person record);
}