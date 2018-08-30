/**
 * created on 2018年4月23日 上午10:45:24
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

import com.utstar.uapollo.custom.repositry.ServerCustomRepository;
import com.utstar.uapollo.custom.service.ServerCustomService;
import com.utstar.uapollo.custom.view.ServerVO;
import com.utstar.uapollo.domain.Server;
import com.utstar.uapollo.service.dto.ServerDTO;
import com.utstar.uapollo.service.mapper.ServerMapper;

/**
 * @author UTSC0167
 * @date 2018年4月23日
 *
 */
@Service
@Transactional
public class ServerCustomServiceImpl implements ServerCustomService {

    private final Logger log = LoggerFactory.getLogger(ServerCustomServiceImpl.class);

    private final ServerCustomRepository serverCustomRepository;

    private final ServerMapper serverMapper;

    public ServerCustomServiceImpl(ServerCustomRepository serverCustomRepository,
            ServerMapper serverMapper) {
        this.serverCustomRepository = serverCustomRepository;
        this.serverMapper = serverMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServerDTO> findByProjectId(Long projectId) {

        log.debug("Request to get all Servers by projectId: {}", projectId);
        return serverCustomRepository.findByProjectId(projectId).stream().map(serverMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }
    
    @Override
    @Transactional(readOnly = true)
	public List<ServerDTO> findByNodeId(Long nodeId) {
		
    	log.debug("Request to get all Servers by nodeId:{} ", nodeId);
		return serverCustomRepository.findByNodeId(nodeId).stream().map(serverMapper::toDto)
				.collect(Collectors.toCollection(LinkedList::new));
	}

    @Override
    @Transactional(readOnly = true)
    public List<ServerDTO> findByApplicationMetaIdAndNodeId(Long applicationMetaId, Long nodeId) {

        log.debug("Request to get all Servers by applicationMetaId: {} and nodeId: {}",
                applicationMetaId, nodeId);
        return serverCustomRepository.findByApplicationMetaIdAndNodeId(applicationMetaId, nodeId)
                .stream().map(serverMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServerDTO> findByApplicationId(Long applicationId) {

        log.debug("Request to get the Server by applicationId : {}", applicationId);
        Optional<Server> existingServer = serverCustomRepository.findByApplicationId(applicationId);
        return existingServer.map((value) -> serverMapper.toDto(value));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServerDTO> findByNodeIdAndIp(Long nodeId, String ip) {

        log.debug("Request to get the Server by nodeId : {} and ip: {}", nodeId, ip);
        Optional<Server> existingServer = serverCustomRepository.findByNodeIdAndIp(nodeId, ip);
        return existingServer.map((value) -> serverMapper.toDto(value));
    }

	@Override
	@Transactional(readOnly = true)
	public Optional<ServerDTO> findByIp(String ip) {
		
		log.debug("Request to get the Server by ip :{}", ip);
		Optional<Server> existingServer = serverCustomRepository.findByIp(ip);
		return existingServer.map((value) -> serverMapper.toDto(value));
	}

	@Override
	public List<ServerVO> finaAllAboutServerInfomation() {
		
		log.debug("Request to get about Server all infomation");
		List<ServerVO> slist = new ArrayList<ServerVO>();
		List<Object> result = serverCustomRepository.findAllInformation();
		for(Object obj : result) {
			
			Object[] rowArray = (Object[]) obj;
			ServerVO serverVo = new ServerVO();
			serverVo.setId(Long.valueOf(rowArray[0].toString()));
			serverVo.setIp((String) rowArray[1]);
			serverVo.setCreateTime(Long.valueOf(rowArray[2].toString()));
			serverVo.setModifyTime(Long.valueOf(rowArray[3].toString()));
			serverVo.setNodeName((String) rowArray[4]);
			serverVo.setProjectName((String) rowArray[5]);
			slist.add(serverVo);
		}
		return slist;
	}
	
}
