package cn.xvkang.entity;

import java.io.Serializable;

public class Kemu implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column kemu.id
     *
     * @mbg.generated Wed Apr 24 10:17:12 CST 2019
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column kemu.name
     *
     * @mbg.generated Wed Apr 24 10:17:12 CST 2019
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column kemu.shortname
     *
     * @mbg.generated Wed Apr 24 10:17:12 CST 2019
     */
    private String shortname;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table kemu
     *
     * @mbg.generated Wed Apr 24 10:17:12 CST 2019
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column kemu.id
     *
     * @return the value of kemu.id
     *
     * @mbg.generated Wed Apr 24 10:17:12 CST 2019
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column kemu.id
     *
     * @param id the value for kemu.id
     *
     * @mbg.generated Wed Apr 24 10:17:12 CST 2019
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column kemu.name
     *
     * @return the value of kemu.name
     *
     * @mbg.generated Wed Apr 24 10:17:12 CST 2019
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column kemu.name
     *
     * @param name the value for kemu.name
     *
     * @mbg.generated Wed Apr 24 10:17:12 CST 2019
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column kemu.shortname
     *
     * @return the value of kemu.shortname
     *
     * @mbg.generated Wed Apr 24 10:17:12 CST 2019
     */
    public String getShortname() {
        return shortname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column kemu.shortname
     *
     * @param shortname the value for kemu.shortname
     *
     * @mbg.generated Wed Apr 24 10:17:12 CST 2019
     */
    public void setShortname(String shortname) {
        this.shortname = shortname == null ? null : shortname.trim();
    }
}