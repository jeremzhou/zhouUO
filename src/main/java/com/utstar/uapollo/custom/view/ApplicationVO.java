/**
 * created on 2018年8月25日 上午10:07:01
 */
package com.utstar.uapollo.custom.view;

import javax.validation.constraints.NotNull;

/**
 * 
 * @author UTSC1021
 * @date 2018年8月25日	
 *
 */
public class ApplicationVO {

	private Long id;
	
	@NotNull
	private Long createTime;

	@NotNull
	private Long modifyTime;
	
	private String applicationMetaName;
	
	private String serverIp;
	
	private String nodeName;
	
	private String projectName;
	
	public ApplicationVO () {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getApplicationMetaName() {
		return applicationMetaName;
	}

	public void setApplicationMetaName(String applicationMetaName) {
		this.applicationMetaName = applicationMetaName;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public String toString() {
		return "ApplicationVO{ " +
				" id=" + id + 
				", createTime=" + createTime + 
				", modifyTime=" + modifyTime + 
				", applicationMetaName=" + applicationMetaName + 
				", serverIp=" + serverIp + 
				", nodeName=" + nodeName + 
				", projectName=" + projectName + 
				"}";
	}
	
	
	
}
