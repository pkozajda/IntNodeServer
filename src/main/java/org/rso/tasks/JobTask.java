package org.rso.tasks;

import lombok.extern.java.Log;
import org.rso.dto.JobEntityDto;
import org.rso.service.JobService;
import org.rso.utils.AppProperty;
import org.rso.utils.JobQueue;
import org.rso.utils.JobType;

/**
 * Created by Radosław on 23.05.2016.
 */
@Log
public class JobTask implements Runnable {
    private String name;
    private JobQueue jobQueue;
    private JobService jobService;

    private AppProperty appProperty = AppProperty.getInstance();


    public JobTask(String name, JobQueue jobQueue, JobService jobService) {
        this.name = name;
        this.jobQueue = jobQueue;
        this.jobService = jobService;
    }

    @Override
    public void run() {
        while (true){
            while (jobQueue.isEmpty()){

            }

            JobEntityDto jobEntityDto = jobQueue.pool();
            if(jobEntityDto!=null){
                log.info("i have taska tralalalalala "+name);
                handleOrder(jobEntityDto);
            }
        }
    }

    private void handleOrder(JobEntityDto jobEntityDto) {
        JobType jobType = jobEntityDto.getJobType();
        switch (jobType){
            case GET_GRADUATES_FROM_ALL_COUNTRIES:
                jobService.getGraduatesFromAllCountriesJob(jobEntityDto);
                break;
            case GET_GRADUATES_FROM_ALL_UNIVERSITIES:
                jobService.getGraduatesFromAllUniversities(jobEntityDto);
                break;
            case GET_GRADUATES_FROM_ALL_FIELD_OF_STUDY:
                jobService.getGraduatesFromAllFieldOfStudies(jobEntityDto);
                break;
            default:
                break;
        }
    }
}
