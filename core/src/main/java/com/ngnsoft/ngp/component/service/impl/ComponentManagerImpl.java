package com.ngnsoft.ngp.component.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.component.service.ComponentManager;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.EngineNode;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.UserSession;
import com.ngnsoft.ngp.model.Zone;
import com.ngnsoft.ngp.service.EngineNodeManager;
import com.ngnsoft.ngp.service.UserSessionManager;
import com.ngnsoft.ngp.service.impl.GenericManagerImpl;

/**
 *
 * @author fcy
 */
public class ComponentManagerImpl extends GenericManagerImpl implements ComponentManager {
    
    @Autowired
    private EngineNodeManager enm;
    
    @Autowired
    private UserSessionManager usm;
    
    @Override
    public Response lsenAll(App app, Map<Zone, List<EngineNode>> zoneEnginesMap, Request req) {
        Map<String, JSONObject> zonesMap = new HashMap<String, JSONObject>();
        List<String> zoneNameList = new ArrayList<String>();
        Iterator<Map.Entry<Zone, List<EngineNode>>> it = zoneEnginesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Zone, List<EngineNode>> me = it.next();
            Zone zone = me.getKey();
            List<EngineNode> zoneEngines = me.getValue();
            if (zoneEngines != null && !zoneEngines.isEmpty()) {
                EngineNode favEn = favEngineNode(req.getUserId(), zoneEngines);
                JSONObject zoneView = new JSONObject();
                try {
                    zoneView.put("zone", zone.getName());
                    zoneView.put("recommend", zone.getRecommend());
                    zoneView.put("host", favEn.getHost());
                    zoneView.put("status", favEn.getStatus());
                    zoneView.put("statusDesc", favEn.getStatusDesc());
                } catch (JSONException ex) {
                    //ignore
                }
                zonesMap.put(zone.getName(), zoneView);
                zoneNameList.add(zone.getName());
            }
        }
        JSONArray ret = new JSONArray();
        Collections.sort(zoneNameList);
        for(String zoneName:zoneNameList) {
            JSONObject zoneView = zonesMap.get(zoneName);
            ret.put(zoneView);
        }
        return new Response(Response.YES, ret);
    }
    
    @Override
    public Response lsenFav(App app, Map<Zone, List<EngineNode>> zoneEnginesMap, Request req) {
        JSONObject favZoneView = null;
        EngineNode favEn = null;
        Iterator<Map.Entry<Zone, List<EngineNode>>> it = zoneEnginesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Zone, List<EngineNode>> me = it.next();
            Zone zone = me.getKey();
            List<EngineNode> zoneEngines = me.getValue();
            if (zoneEngines != null && !zoneEngines.isEmpty()) {
                if (favEn != null) {
                    double favEnLoading = favEn.getLoading();
                    double favEnLoad = favEn.getLoad();
                    EngineNode en = favEngineNode(req.getUserId(), zoneEngines);
                    double enLoading = en.getLoading();
                    double enLoad = en.getLoad();
                    if (enLoading / enLoad < favEnLoading / favEnLoad) {
                        favEn = en;
                        favZoneView = new JSONObject();
                        try {
                            favZoneView.put("zone", zone.getName());
                            favZoneView.put("recommend", zone.getRecommend());
                            favZoneView.put("host", favEn.getHost());
                            favZoneView.put("status", favEn.getStatus());
                            favZoneView.put("statusDesc", favEn.getStatusDesc());
                        } catch (JSONException ex) {
                            //ignore
                        }
                    }
                } else {
                    favEn = favEngineNode(req.getUserId(), zoneEngines);
                    favZoneView = new JSONObject();
                    try {
                        favZoneView.put("zone", zone.getName());
                        favZoneView.put("recommend", zone.getRecommend());
                        favZoneView.put("host", favEn.getHost());
                        favZoneView.put("status", favEn.getStatus());
                        favZoneView.put("statusDesc", favEn.getStatusDesc());
                    } catch (JSONException ex) {
                        //ignore
                    }
                }
            }
        }
        JSONArray ret = new JSONArray();
        ret.put(favZoneView);
        return new Response(Response.YES, ret);
    }
    
    private EngineNode favEngineNode(Long userId, List<EngineNode> ens) {
        Map<String, EngineNode> ensValid = new HashMap<String, EngineNode>();
        Map<String, EngineNode> ensInvalid = new HashMap<String, EngineNode>();
        EngineNode enInvalid = null;
        for(EngineNode en:ens) {
            if (enm.checkHealth(en) >= EngineNode.STATUS_LIVE) {
                ensValid.put(en.getId(), en);
            } else {
                ensInvalid.put(en.getId(), en);
                if (enInvalid == null) {
                    enInvalid = en;
                }
            }
        }
        if (ensValid.isEmpty()) {
            return enInvalid;
        }
        EngineNode favEn = null;
        if (userId != null) {
            UserSession us = usm.matchValidSession(userId);
            if (us != null) {
                String engineId = us.getEngineId();
                if (ensValid.containsKey(engineId)) {
                    favEn = ensValid.get(engineId);
                }
            }
        }
        if (favEn == null) {
            Iterator<Map.Entry<String, EngineNode>> it = ensValid.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<String, EngineNode> me = it.next();
                EngineNode en = me.getValue();
                if (favEn != null) {
                    double favEnLoading = favEn.getLoading();
                    double favEnLoad = favEn.getLoad();
                    /* disabled
                    if (favEnLoading/favEnLoad * 100 < EngineNode.FINE_POINT_PERCENT) {
                        break;
                    }
                    * 
                    */
                    double enLoading = en.getLoading();
                    double enLoad = en.getLoad();
                    if (enLoading/enLoad < favEnLoading/favEnLoad) {
                        favEn = en;
                    }
                } else {
                    favEn = en;
                }
            }
        }
        return favEn;
    }

    @Override
    public Response getClanByCondition(Request req, App app) {
        return null;
    }

    @Override
    public Response createClan(Request req, App app) throws Exception {
        return null;
    }

    @Override
    public Response modifyClan(Request req, App app) throws Exception {
        return null;
    }

    @Override
    public Response joinClan(Request req, App app) {
        return null;
    }

    @Override
    public Response getClans(Request req, App app, Pagination page) {
        return null;
    }

    @Override
    public Response exitClan(Request req, App app) {
        return null;
    }

    @Override
    public Response conferClanUser(Request req, App app) {
        return null;
    }

    @Override
    public Response getMessageByCondition(Request req, App app) throws JSONException {
        return null;
    }

    @Override
    public Response rdms(Request req) throws JSONException {
        return null;
    }

    @Override
    public Response mtuf(Request req) throws JSONException {
        return null;
    }

    @Override
    public Response dlms(Request req) throws JSONException {
        return null;
    }
    
}
