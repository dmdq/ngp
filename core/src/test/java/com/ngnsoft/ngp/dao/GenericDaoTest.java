package com.ngnsoft.ngp.dao;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ngnsoft.ngp.dao.ibatis.GenericDaoIbatis;
import com.ngnsoft.ngp.model.TestBean;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class GenericDaoTest extends BaseDaoTestCase {
    
    GenericDao genericDao;
    
    SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }

    @Before
    public void setUp() {
//        genericDao = new GenericDaoIbatis(sqlMapClient);
    }

    @Test
    public void getTestBean() {
        TestBean tb = new TestBean();
        tb.setId(10005L);
        tb = genericDao.findObject(tb);
        assertEquals("fcy", tb.getName());
    }
}
