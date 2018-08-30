/**
 * created on 2018年8月25日 上午9:57:50
 */
package com.utstar.uapollo.custom.view;

import javax.validation.constraints.NotNull;


/**
 * 
 * @author UTSC1021
 * @date 2018年8月25日	
 *
 */
public class ServerVO {

	private Long id ;
	
	@NotNull
	private String ip;
	
	@NotNull
	private Long createTime;

	@NotNull
	private Long modifyTime;
	
	private String nodeName;
	
	private String projectName;
	
	public ServerVO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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
		return "ServerVO{"+ 
				"id=" + id + 
				", ip=" + ip +
				", createTime=" + createTime +
				", modifyTime="+ modifyTime + 
				", nodeName=" + nodeName + 
				", projectName=" + projectName + 
				"}";
	}
	
	
	
}
