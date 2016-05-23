package org.rso.utils;

/**
 * Created by Radosław on 19.05.2016.
 */
public enum JobType {
    GET_GRADUATES_FROM_ALL_COUNTRIES("/allCountries"),
    GET_GRADUATES_FROM_ALL_UNIVERSITIES("/universities"),
    GET_GRADUATES_FROM_ALL_FIELD_OF_STUDY("/fieldOfStudy"),
    GET_GRADUATES_MORE_THAN_ONE_FIELD_OF_STUDY_COUNTRIES("/moreThanOneFieldOfStudy/country"),
    GET_GRADUATES_MORE_THAN_ONE_FIELD_OF_STUDY_UNIVERSITIES("/moreThanOneFieldOfStudy/university"),
    GET_STATISTIC_ORGIN_FROM_LAND("/orginFrom/land"),
    GET_STATISTIC_ORGIN_FROM_COUNTRIES("/orginFrom/countries"),
    GET_STATISTIC_ORGIN_FROM_UNIVERSITIES("/orginFrom/universities"),
    GET_STATISTIC_ORGIN_FROM_FIELD_OF_STUDY("/orginFrom/fieldOfStudies"),
    GET_STATISTIC_WORKING_STUDENTS_FIELD_OF_STUDY("/working/fieldOfStudies"),
    GET_STATISTIC_WORKING_STUDENTS_COUNTRIES("/working/countries"),
    GET_STATISTIC_WORKING_STUDENTS_UNIVERSITIES("/working/universities");

    private String url;

    JobType(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
