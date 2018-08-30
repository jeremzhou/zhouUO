/**
 * created on 2018年8月24日 下午4:23:03
 */
package com.utstar.uapollo.custom.view;

/**
 * 
 * @author UTSC1021
 * @date 2018年8月24日	
 *
 */
public class NodeVO {
	
	private Long id;
	
	private String name;
	
	private Long createTime;
	
	private Long modifyTime;
	 
	private String projectName;
	
	public NodeVO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public Long getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Long modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public String toString() {
		return "NodeDTO{" +
	            "id=" + getId() +
	            ", name='" + getName() + "'" +
	            ", projectName=" + getProjectName() + 
	            "}";
	}
	 

}
