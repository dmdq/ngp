package com.ngnsoft.ngp.protocol;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ngnsoft.ngp.Engine;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/applicationContext-*.xml", "classpath:/applicationContext-core.xml"})
@TransactionConfiguration(defaultRollback = true)
@Configurable
public abstract class ProtocolBaseTest {

	@Before
    @Transactional
    public void setup() {
        //init configuration params
		Engine.getInstance().init();
    }
	
	@Test
	public abstract void testProtocol() throws Exception;

}
