/**
 * created on 2018年4月23日 下午2:03:48
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

import com.utstar.uapollo.custom.repositry.ApplicationCustomRepository;
import com.utstar.uapollo.custom.service.ApplicationCustomService;
import com.utstar.uapollo.custom.view.ApplicationVO;
import com.utstar.uapollo.domain.Application;
import com.utstar.uapollo.service.dto.ApplicationDTO;
import com.utstar.uapollo.service.mapper.ApplicationMapper;

/**
 * @author UTSC0167
 * @date 2018年4月23日
 *
 */
@Service
@Transactional
public class ApplicationCustomServiceImpl implements ApplicationCustomService {

    private final Logger log = LoggerFactory.getLogger(ApplicationCustomServiceImpl.class);

    private final ApplicationCustomRepository applicationCustomRepository;

    private final ApplicationMapper applicationMapper;

    public ApplicationCustomServiceImpl(ApplicationCustomRepository applicationCustomRepository,
            ApplicationMapper applicationMapper) {
        this.applicationCustomRepository = applicationCustomRepository;
        this.applicationMapper = applicationMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDTO> findByProjectId(Long projectId) {

        log.debug("Request to get all Applications by projectId: {}", projectId);
        return applicationCustomRepository.findByProjectId(projectId).stream()
                .map(applicationMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDTO> findByApplicationMetaId(Long applicationMetaId) {

        log.debug("Request to get all Applications by applicationMetaId: {}", applicationMetaId);
        return applicationCustomRepository.findByApplicationMetaId(applicationMetaId).stream()
                .map(applicationMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApplicationDTO> findByApplicationMetaIdAndServerId(Long applicationMetaId,
            Long serverId) {

        log.debug("Request to get the Application by applicationMetaId: {} and serverId: {} ",
                applicationMetaId, serverId);
        Optional<Application> existingApplication = applicationCustomRepository
                .findByApplicationMetaIdAndServerId(applicationMetaId, serverId);
        return existingApplication.map((value) -> applicationMapper.toDto(value));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationDTO> findByApplicationMetaIdAndNodeId(Long applicationMetaId,
            Long nodeId) {

        log.debug("Request to get all Applications by applicationMetaId: {} and nodeId: {}",
                applicationMetaId, nodeId);
        return applicationCustomRepository
                .findByApplicationMetaIdAndNodeId(applicationMetaId, nodeId).stream()
                .map(applicationMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

	@Override
	public List<ApplicationVO> findAllAboutApplicationInfomation() {
		
		log.debug("Request to get all about Applications infomation");
		List<Object> result = applicationCustomRepository.findAllInfomation();
		List<ApplicationVO> alist = new ArrayList<ApplicationVO>();
		for(Object obj : result) {
			
			Object[] rowArray =(Object[]) obj;
			ApplicationVO applicationVo = new ApplicationVO();
			applicationVo.setId(Long.valueOf(rowArray[0].toString()));
			applicationVo.setCreateTime(Long.valueOf(rowArray[1].toString()));
			applicationVo.setModifyTime(Long.valueOf(rowArray[2].toString()));
			applicationVo.setApplicationMetaName((String) rowArray[3]);
			applicationVo.setServerIp((String) rowArray[4]);
			applicationVo.setNodeName((String) rowArray[5]);
			applicationVo.setProjectName((String) rowArray[6]);
			alist.add(applicationVo);
		}
		
		return alist;
	}
}
