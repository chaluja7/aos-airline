package cz.cvut.aos.airline.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Abstract service test.
 *
 * @author jakubchalupa
 * @since 22.10.16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-context.xml"})
//@Rollback
//@Transactional(transactionManager = "transactionManager")
public abstract class AbstractServiceTest {

}

