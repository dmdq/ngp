package com.ngnsoft.ngp;

import com.ngnsoft.ngp.model.App;

public interface Executor {
    
    public Response exec(Request req, App app) throws Exception;
    
    
}
