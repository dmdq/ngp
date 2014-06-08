package com.ngnsoft.ngp.service;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.Pagination;

public interface ClanManager {
    
    public static final String GET_CLAN_MEM_KEY = "cmData";
    public static final String GET_CLAN_DATA_KEY = "clanData";
    public static final String GET_CLAN_ID_KEY = "clanId";
    public static final String GET_CLAN_PARAM_KEY = "clan";
    public static final int GET_CLAN_TYPE_VAL = 0;
    public static final int GET_CLAN_MEM_TYPE_VAL = 1;

    public Response getClanByCondition(Request req, App app);

    public Response createClan(Request req, App app) throws Exception;

    public Response modifyClan(Request req, App app) throws Exception;

    public Response joinClan(Request req, App app);

    public Response getClans(Request req, App app, Pagination page);

    public Response exitClan(Request req, App app);

    public Response conferClanUser(Request req, App app);
}
