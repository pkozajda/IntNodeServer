package org.rso.service;

import lombok.extern.java.Log;
import org.rso.dto.JobEntityDto;
import org.rso.utils.JobQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Radosław on 23.05.2016.
 */
@Service
@Transactional
@Log
public class JobServiceImpl implements JobService {

    @Autowired
    private JobQueue jobQueue;

    @Override
    public void registerJob(JobEntityDto jobEntityDto) {
        log.info("add to queue "+jobEntityDto);
        jobQueue.add(jobEntityDto);
    }
}
