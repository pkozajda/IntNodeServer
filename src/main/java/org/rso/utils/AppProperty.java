package org.rso.utils;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Radosław on 05.05.2016.
 */
public class AppProperty {
    private static AppProperty appProperty;
    private Date lastCoordinatorPresence;
    @Getter @Setter
    private NodeInfo selfNode;
    @Getter @Setter
    private NodeInfo coordinatorNode;

    private List<NodeInfo> listOfAvaiableNodes = new ArrayList<>();

    private AppProperty(){}

    public synchronized static AppProperty getInstance() {
        if(appProperty==null){
            appProperty = new AppProperty();
        }
        return appProperty;
    }

    public synchronized void setLastCoordinatorPresence(Date lastCoordinatorPresence) {
        this.lastCoordinatorPresence = lastCoordinatorPresence;
    }

    public synchronized Date getLastCoordinatorPresence() {
        return lastCoordinatorPresence;
    }

    public synchronized List<NodeInfo> getListOfAvaiableNodes() {
        return listOfAvaiableNodes;
    }

    public synchronized void setListOfAvaiableNodes(List<NodeInfo> listOfAvaiableNodes) {
        this.listOfAvaiableNodes = listOfAvaiableNodes;
    }

    public synchronized void addAvaiableNode(NodeInfo n){
        this.listOfAvaiableNodes.add(n);
    }

    public synchronized void removeUnAvaiableNode(int nodeId){
        this.listOfAvaiableNodes = listOfAvaiableNodes.stream().
                                    filter(p -> p.getNodeId() != nodeId).
                                    collect(Collectors.toCollection(ArrayList::new));
    }

    public List<String> getAvaiableNodesIpAddresses(){
        return this.listOfAvaiableNodes.stream().map(p->p.getNodeIPAddress()).collect(Collectors.toCollection(ArrayList::new));
    }
}

