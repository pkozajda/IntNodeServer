package org.rso.tasks;

import lombok.extern.java.Log;
import org.rso.dto.JobEntityDto;
import org.rso.utils.AppProperty;
import org.rso.utils.JobQueue;

/**
 * Created by Radosław on 23.05.2016.
 */
@Log
public class JobTask implements Runnable {
    private String name;
    private JobQueue jobQueue;

    private AppProperty appProperty = AppProperty.getInstance();


    public JobTask(String name, JobQueue jobQueue) {
        this.name = name;
        this.jobQueue = jobQueue;
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

    }
}
