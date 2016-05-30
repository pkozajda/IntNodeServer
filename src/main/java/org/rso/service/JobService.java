package org.rso.service;

import org.rso.dto.JobEntityDto;

/**
 * Created by Radosław on 23.05.2016.
 */
public interface JobService {

    void registerJob(JobEntityDto jobEntityDto);

    void getGraduatesFromAllCountriesJob(JobEntityDto jobEntityDto);

    void getGraduatesFromAllUniversities(JobEntityDto jobEntityDto);

    void getGraduatesFromAllFieldOfStudies(JobEntityDto jobEntityDto);

    void getStatisticOrginFromLand(JobEntityDto jobEntityDto);

    void getStatisticOrginFromCountries(JobEntityDto jobEntityDto);
}
