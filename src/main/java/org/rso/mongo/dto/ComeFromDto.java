package org.rso.mongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rso.utils.ComeFrom;

/**
 * Created by Radosław on 28.05.2016.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ComeFromDto {
    private ComeFrom comeFrom;
    private long val;
}
