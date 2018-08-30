/**
 * created on 2018年4月19日 下午2:24:33
 */
package com.utstar.uapollo.custom.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utstar.uapollo.custom.repositry.NodeCoustomRepository;
import com.utstar.uapollo.custom.service.NodeCustomService;
import com.utstar.uapollo.custom.view.NodeVO;
import com.utstar.uapollo.domain.Node;
import com.utstar.uapollo.service.dto.NodeDTO;
import com.utstar.uapollo.service.mapper.NodeMapper;

/**
 * @author UTSC0167
 * @date 2018年4月19日
 *
 */
@Service
@Transactional
public class NodeCustomServiceImpl implements NodeCustomService {

    private final Logger log = LoggerFactory.getLogger(NodeCustomServiceImpl.class);

    private final NodeCoustomRepository nodeCoustomRepository;

    private final NodeMapper nodeMapper;

    public NodeCustomServiceImpl(NodeCoustomRepository nodeCoustomRepository,
            NodeMapper nodeMapper) {
        this.nodeCoustomRepository = nodeCoustomRepository;
        this.nodeMapper = nodeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NodeDTO> findByProjectId(Long projectId) {
        log.debug("Request to get all nodes by projectId: {}", projectId);
        return nodeCoustomRepository.findByProjectId(projectId).stream().map(nodeMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NodeDTO> findByApplicationMetaId(Long applicationMetaId) {

        log.debug("Request to get all nodes by applicationMetaId: {}", applicationMetaId);
        return nodeCoustomRepository.findByApplicationMetaId(applicationMetaId).stream()
                .map(nodeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NodeDTO> findByApplicationId(Long applicationId) {

        log.debug("Request to get the Node by applicationId: {}", applicationId);
        Optional<Node> existingNode = nodeCoustomRepository.findByApplicationId(applicationId);
        return existingNode.map((value) -> nodeMapper.toDto(value));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NodeDTO> findByProjectIdAndName(Long projectId, String name) {

        log.debug("Request to get the Node by projectId : {} and name: {}", projectId, name);
        Optional<Node> existingNode = nodeCoustomRepository
                .findByProjectIdAndNameIgnoreCase(projectId, name);
        return existingNode.map((value) -> nodeMapper.toDto(value));
    }

	@Override
	public List<NodeVO> findAllInfomation() {
		
		log.debug("Request to get the about node All infomation");
		
		List<NodeVO> nodelist = new ArrayList<NodeVO>();
		List<Object> result = nodeCoustomRepository.findAllInformation();
		for(Object obj : result) {
			
			Object[] rowArray = (Object[]) obj;
			NodeVO nodeVo = new NodeVO();
			nodeVo.setId(Long.valueOf(rowArray[0].toString()));
			nodeVo.setName((String) rowArray[1]);
			nodeVo.setCreateTime(Long.valueOf(rowArray[2].toString()));
			nodeVo.setModifyTime(Long.valueOf(rowArray[3].toString()));
			nodeVo.setProjectName((String) rowArray[4]);
			nodelist.add(nodeVo);
		}
		return nodelist;
		
	}
}
