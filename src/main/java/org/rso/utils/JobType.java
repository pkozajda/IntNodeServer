package org.rso.utils;

/**
 * Created by Radosław on 19.05.2016.
 */
public enum JobType {
    GET_GRADUATES_FROM_ALL_COUNTRIES("/resp/allCountries"),
    GET_GRADUATES_FROM_ALL_UNIVERSITIES("/resp/universities"),
    GET_GRADUATES_FROM_ALL_FIELD_OF_STUDY("/resp/fieldOfStudy"),
    GET_GRADUATES_MORE_THAN_ONE_FIELD_OF_STUDY_COUNTRIES("/resp/moreThanOneFieldOfStudy/country"),
    GET_GRADUATES_MORE_THAN_ONE_FIELD_OF_STUDY_UNIVERSITIES("/resp/moreThanOneFieldOfStudy/university"),
    GET_STATISTIC_ORGIN_FROM_LAND("/resp/orginFrom/land"),
    GET_STATISTIC_ORGIN_FROM_COUNTRIES("/resp/orginFrom/countries"),
    GET_STATISTIC_ORGIN_FROM_UNIVERSITIES("/resp/orginFrom/universities"),
    GET_STATISTIC_ORGIN_FROM_FIELD_OF_STUDY("/resp/orginFrom/fieldOfStudies"),
    GET_STATISTIC_WORKING_STUDENTS_FIELD_OF_STUDY("/resp/working/fieldOfStudies"),
    GET_STATISTIC_WORKING_STUDENTS_COUNTRIES("/resp/working/countries"),
    GET_STATISTIC_WORKING_STUDENTS_UNIVERSITIES("/resp/working/universities");

    private String url;
    private static final String BEGIN = "/jobs";

    JobType(String url) {
        this.url = BEGIN+url;
    }

    public String getUrl() {
        return url;
    }
}
