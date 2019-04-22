package cn.xvkang.dao;

import cn.xvkang.entity.TXms;
import cn.xvkang.entity.TXmsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TXmsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_xms
     *
     * @mbg.generated Fri Apr 12 21:17:04 CST 2019
     */
    long countByExample(TXmsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_xms
     *
     * @mbg.generated Fri Apr 12 21:17:04 CST 2019
     */
    int deleteByExample(TXmsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_xms
     *
     * @mbg.generated Fri Apr 12 21:17:04 CST 2019
     */
    int deleteByPrimaryKey(String xmid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_xms
     *
     * @mbg.generated Fri Apr 12 21:17:04 CST 2019
     */
    int insert(TXms record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_xms
     *
     * @mbg.generated Fri Apr 12 21:17:04 CST 2019
     */
    int insertSelective(TXms record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_xms
     *
     * @mbg.generated Fri Apr 12 21:17:04 CST 2019
     */
    List<TXms> selectByExample(TXmsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_xms
     *
     * @mbg.generated Fri Apr 12 21:17:04 CST 2019
     */
    TXms selectByPrimaryKey(String xmid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_xms
     *
     * @mbg.generated Fri Apr 12 21:17:04 CST 2019
     */
    int updateByExampleSelective(@Param("record") TXms record, @Param("example") TXmsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_xms
     *
     * @mbg.generated Fri Apr 12 21:17:04 CST 2019
     */
    int updateByExample(@Param("record") TXms record, @Param("example") TXmsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_xms
     *
     * @mbg.generated Fri Apr 12 21:17:04 CST 2019
     */
    int updateByPrimaryKeySelective(TXms record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_xms
     *
     * @mbg.generated Fri Apr 12 21:17:04 CST 2019
     */
    int updateByPrimaryKey(TXms record);
}