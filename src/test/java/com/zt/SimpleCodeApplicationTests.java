package com.zt;

import com.zt.entity.CommonModel;
import freemarker.template.Template;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 21:54
 * description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleCodeApplicationTests {

    @Autowired
    private CommonModel commonModel;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Test
    public void tableAll() throws IOException {
        System.out.println(commonModel.getEntityTemplate());
        Template templateEntity = freeMarkerConfigurer.getConfiguration().getTemplate(commonModel.getEntityTemplate());
    }
}
