package com.ngnsoft.ngp.component.coc.service;

import com.ngnsoft.ngp.component.coc.model.CocIosOrder;
import com.ngnsoft.ngp.component.service.ComponentManager;

public interface CocManager extends ComponentManager {

    public boolean apsl(CocIosOrder order);
    

}
