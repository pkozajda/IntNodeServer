package org.rso.service;

import org.rso.dto.JobEntityDto;

/**
 * Created by Radosław on 23.05.2016.
 */
public interface JobService {

    void registerJob(JobEntityDto jobEntityDto);

    void getGraduatesFromAllCountriesJob(JobEntityDto jobEntityDto);
}
