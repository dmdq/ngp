package com.ngnsoft.ngp.component.bingo.service;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.component.service.ComponentManager;
import com.ngnsoft.ngp.model.App;

/**
 *
 * @author fcy
 */
public interface BingoManager extends ComponentManager {
	
	public Response count(App app, Request req) throws Exception;

    public Response join(App app, String gid, Request req) throws Exception;

    public Response ready(App app, String gid, Request req) throws Exception;

    public Response bingo(App app, String gid, Request req) throws Exception;

    public Response quit(App app, String gid, Request req) throws Exception;

    public Response htbt(App app, String gid, Request req) throws Exception;

    public Response chat(App app, String gid, Request req) throws Exception;

    public void checkGame(App app, String gid) throws Exception;
    
    public void onGameOver(String gid) throws Exception;
    
    public void joinGame(Long rid, String gid, String data) throws Exception;
    
    public boolean quit(String gid, Long rid, String data);
    
    public Response over(String gid, Long rid);
    
}