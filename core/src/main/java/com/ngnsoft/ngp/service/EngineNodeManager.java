package com.ngnsoft.ngp.service;

import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.EngineNode;
import com.ngnsoft.ngp.model.Zone;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fcy
 */
public interface EngineNodeManager extends GenericManager {
    
    /**
     * 
     * @param en
     * @return false if engine's status is down, or true if on control
     */
    int checkHealth(EngineNode en);
    
    Map<Zone, List<EngineNode>> getEngineNodeByApp(App app);
    
}
