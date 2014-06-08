package com.ngnsoft.ngp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.model.ResUpdate;
import com.ngnsoft.ngp.model.ResUpdateFile;
import com.ngnsoft.ngp.service.ResUpdateManager;

@SuppressWarnings("rawtypes")
@Service("resUpdateManager")
public class ResUpdateManagerImpl extends GenericManagerImpl implements
        ResUpdateManager {

    @SuppressWarnings("unchecked")
    @Override
    public Response checkResVersion(String appId, Integer resVersion) throws Exception {
        String imageServerUrlPrefix = Engine.getInstance().getConfig().getFileServer();
        JSONObject uf = new JSONObject();
        ResUpdate resUpdate = new ResUpdate();
        resUpdate.setAppId(appId);
        resUpdate.setOldVersion(resVersion);
        List<ResUpdate> objs = findByNamedQuery("find_res_update_by_version", resUpdate, ResUpdate.class);
        Integer maxVersion = null;
        List<Long> resIds = new ArrayList<Long>();
        if (objs != null) {
            for (int i = 0; i < objs.size(); i++) {
                Integer oldVersion = objs.get(i).getOldVersion();
                Integer newVersion = objs.get(i).getNewVersion();
                if (i == 0) {
                    if (resVersion.equals(oldVersion)) {
                        maxVersion = newVersion;
                    } else {
                        break;
                    }
                } else {
                    if (maxVersion.equals(oldVersion)) {
                        maxVersion = newVersion;
                    } else {
                        break;
                    }
                }
                resIds.add(objs.get(i).getId());
            }
        }
        String ruIds = resIds.toString();
        ruIds = ruIds.replace("[", "(");
        ruIds = ruIds.replace("]", ")");
        Map paramMap = new HashMap();
        paramMap.put("ruIds", ruIds);
        paramMap.put("tableName", new ResUpdateFile().getTableName());
        List<ResUpdateFile> resUpdateFiles = null;
        Map<String, List<ResUpdateFile>> resUpdateFileMap = new HashMap<String, List<ResUpdateFile>>();
        LinkedList<ResUpdateFile> sortResUpdateFiles = new LinkedList<ResUpdateFile>();
        if (resIds.size() > 0) {
            resUpdateFiles = findByNamedQuery("find_res_update_file_by_ruId", paramMap, ResUpdateFile.class);
        }
        if (resUpdateFiles != null) {
            for (ResUpdateFile obj : resUpdateFiles) {
                List<ResUpdateFile> subObjs;
                if (resUpdateFileMap.containsKey(obj.getRuId() + "")) {
                    subObjs = resUpdateFileMap.get(obj.getRuId() + "");
                    subObjs.add(obj);
                } else {
                    subObjs = new ArrayList<ResUpdateFile>();
                    subObjs.add(obj);
                }
                resUpdateFileMap.put(obj.getRuId() + "", subObjs);
            }
            for (Long id : resIds) {
                if (resUpdateFileMap.containsKey(String.valueOf(id))) {
                    sortResUpdateFiles.addAll(resUpdateFileMap.get(String.valueOf(id)));
                }
            }
        }
        if (maxVersion != null) {
            uf.put("newver", maxVersion);
            for (ResUpdateFile resUpdateFile : sortResUpdateFiles) {
                if (StringUtils.hasText(resUpdateFile.getFileUrn()) && !resUpdateFile.getFileUrn().startsWith("http")) {
                    uf.put(resUpdateFile.getFileName(), imageServerUrlPrefix + resUpdateFile.getFileUrn());
                } else {
                    uf.put(resUpdateFile.getFileName(), resUpdateFile.getFileUrn());
                }
            }
        }
        if (uf.has("newver")) {
            return new Response(Response.YES, uf);
        } else {
            return new Response(Response.NO, "NO_NEWVER");
        }
    }
}
