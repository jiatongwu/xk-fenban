package cn.xvkang.entity;

public class UserRole {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_role.id
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_role.user_id
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    private String userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_role.role_id
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    private String roleId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_role.id
     *
     * @return the value of user_role.id
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_role.id
     *
     * @param id the value for user_role.id
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_role.user_id
     *
     * @return the value of user_role.user_id
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_role.user_id
     *
     * @param userId the value for user_role.user_id
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_role.role_id
     *
     * @return the value of user_role.role_id
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_role.role_id
     *
     * @param roleId the value for user_role.role_id
     *
     * @mbg.generated Sun Apr 21 15:59:23 CST 2019
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }
}