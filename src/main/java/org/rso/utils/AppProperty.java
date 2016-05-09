package org.rso.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.rso.exceptions.NodeNotFoundException;

import javax.validation.constraints.Max;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Log
public class AppProperty {
    private static AppProperty appProperty;
    private Date lastCoordinatorPresence;
    @Getter @Setter
    private NodeInfo selfNode;
    @Getter @Setter
    private NodeInfo coordinatorNode;

    private AtomicInteger idCounter = null;

    private List<NodeInfo> listOfAvaiableNodes = new ArrayList<>();

    private AppProperty(){
        this.lastCoordinatorPresence = new Date();
    }

    public synchronized static AppProperty getInstance() {
        if(appProperty==null){
            appProperty = new AppProperty();
        }
        return appProperty;
    }

    public synchronized NodeInfo allocateNewNode() {

        /* TODO:
               This is retarded. Change appProeprty impleemntation to Map
         */
        if(idCounter == null) {

            if(selfNode == null && coordinatorNode == null) {
                idCounter = new AtomicInteger(1);
            } else {

                if(!listOfAvaiableNodes.isEmpty()) {
                    final int availableNodesMax = listOfAvaiableNodes.stream().map(NodeInfo::getNodeId).mapToInt(Integer::intValue).max().getAsInt();

                    idCounter = new AtomicInteger(
                            Math.max(Math.max(availableNodesMax, selfNode.getNodeId()), coordinatorNode.getNodeId())
                    );
                } else {
                    idCounter = new AtomicInteger(
                            Math.max(selfNode.getNodeId(), coordinatorNode.getNodeId())
                    );
                }

            }
        }

        return NodeInfo.builder()
                .nodeId(idCounter.incrementAndGet())
                .nodeIPAddress("")
                .nodeType(NodeType.INTERNAL)
                .build();
    }

    public synchronized void setLastCoordinatorPresence(Date lastCoordinatorPresence) {
        log.info("aktualizujemy date obecnosci koordynatora "+DataTimeLogger.logTime(lastCoordinatorPresence));
        this.lastCoordinatorPresence = lastCoordinatorPresence;
    }

    public synchronized Date getLastCoordinatorPresence() {
        return lastCoordinatorPresence;
    }

    public synchronized List<NodeInfo> getAvailableNodes() {
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


    public NodeInfo getNodeById(final int nodeId) throws NodeNotFoundException {
        return getAvailableNodes().stream()
                .filter(node -> node.getNodeId() == nodeId)
                .findFirst()
                .orElse(null);
//                .orElseThrow(() -> new NodeNotFoundException(String.format("Node with id = %s does not exist", nodeId)));
    }

}

