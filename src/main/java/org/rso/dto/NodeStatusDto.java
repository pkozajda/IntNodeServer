package org.rso.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NodeStatusDto {
    private int nodeId;
    private String nodeIPAddress;
    private String nodeType;
}
