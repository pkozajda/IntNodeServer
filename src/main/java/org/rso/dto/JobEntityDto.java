package org.rso.dto;

import lombok.*;
import org.rso.entities.resposnObjct.ResponsBody;
import org.rso.utils.JobStatus;
import org.rso.utils.JobType;
import org.rso.utils.NodeInfo;

import java.util.Date;

/**
 * Created by Radosław on 22.05.2016.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class JobEntityDto {
    private int id;
    private Date date;
    private JobType jobType;
    private JobStatus jobStatus;
    private NodeInfo orderOwner;
    private NodeInfo orderCustomer;
    private ResponsBody responsBody;
}
