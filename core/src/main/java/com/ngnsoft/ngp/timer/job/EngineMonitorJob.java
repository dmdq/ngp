package com.ngnsoft.ngp.timer.job;

import com.ngnsoft.ngp.model.EngineNode;
import com.ngnsoft.ngp.service.EngineNodeManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Only LSEN Engine try to check all engines health
 * 
 * @author fcy
 */
public class EngineMonitorJob {
    
    @Autowired
    private EngineNodeManager em;
    
    @Autowired
    private MailSender mailSender;
    
    @Autowired
    private SimpleMailMessage mailMessage;
    
    private boolean lsenEnable = false;
    
    private String engineId;

    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }

    public void setLsenEnable(boolean lsenEnable) {
        this.lsenEnable = lsenEnable;
    }
    
    public void execute() {
        if (!lsenEnable) return;
        EngineNode engineNode = new EngineNode();
        List<EngineNode> ens = em.findMulti(engineNode);
        for (EngineNode en : ens) {
            if (!en.getId().equals(engineId)) {
                int ret = em.checkHealth(en);
                if (ret == EngineNode.STATUS_DOWN) {
                    String subject = mailMessage.getSubject();
                    mailMessage.setSubject(en.getId()+subject);
                    mailSender.send(mailMessage);
                    mailMessage.setSubject(subject);
                }
            }
        }
    }
}