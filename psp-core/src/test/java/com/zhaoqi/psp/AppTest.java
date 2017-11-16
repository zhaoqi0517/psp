package com.zhaoqi.psp;

import com.zhaoqi.psp.upload.UploadFileService;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/beans.xml"})
public class AppTest
        extends TestCase
{
    /**
     * Rigourous Test :-)
     */

    @org.junit.Test
    public void testApp() {
        RestTemplate restTemplate = new RestTemplate();
        String rs = restTemplate.getForObject("http://localhost:8080/upload", String.class);
        assertTrue("OK".equals(rs));

        //UploadFileService ufs = new UploadFileService("/home/qi/Pictures/IMG_4280.JPG");
        //ufs.setMaxRead(5);
        //ufs.setMaxUpload(5);
        //ufs.setPartSize(100);
        //ufs.upload();
    }
}
