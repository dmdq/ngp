package com.ngnsoft.ngp.timer.job;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.model.EngineNode;
import com.ngnsoft.ngp.service.EngineNodeManager;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Check engine health
 * 
 * @author fcy
 */
public class EngineHealthJob {
    
    @Autowired
    private EngineNodeManager em;
    
    private String engineId;

    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }
    
    public void execute() {
        EngineNode engineNode = new EngineNode();
        engineNode.setId(engineId);
        EngineNode en = em.findObject(engineNode);
        EngineNode enUpdate = new EngineNode(engineId);
        enUpdate.setTouchTime(new Date());
        enUpdate.setLoading(Engine.getInstance().getLoading());
        em.update(enUpdate);
        Engine.getInstance().setStatus(en.getStatus());
        Engine.getInstance().setStatusDesc(en.getStatusDesc());
    }
}