package com.noveogroup.clap.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @author Andrey Sokolov
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigBean.class, ConfigurationUtils.class})
public class ConfigBeanTest {

    private static final String TEMP_FILES_DIR_PROPERTY_1 = "blablabla";
    private static final String TEMP_FILES_DIR_PROPERTY_2 = "qweqweqwe";

    @InjectMocks
    private ConfigBean configBean;

    @Mock
    private File mockFile;

    private Properties properties;

    @Before
    public void setUp() throws Exception {
        properties = new VariablableProperties();
        properties.load(ClassLoader.getSystemClassLoader().getResourceAsStream("clap.properties"));
        PowerMockito.mockStatic(ConfigurationUtils.class);
        PowerMockito.when(ConfigurationUtils.getPropertiesFromConfig("clap.properties")).thenReturn(properties);
    }

    @Test
    public void test() throws Exception {
        PowerMockito.when(mockFile.isDirectory()).thenReturn(true);
        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(mockFile);

        configBean.setup();

        assertEquals(properties, configBean.getProperties());

        assertEquals("DEFAULT", configBean.getAuthenticationSystemId());
        assertEquals("http://localhost:8080/clap-rest/v1/apk/{revId}/{varId}?token={token}", configBean.getDownloadApkUrl());
        assertEquals("http://localhost:8080/clap-rest/v1/message/screenshot/1/", configBean.getDownloadScreenshotUrl(1));
        assertEquals("http://localhost:8080/clap-rest/v1/project/icon/2/", configBean.getDownloadProjectIconUrl(2));
        assertEquals(30000000, configBean.getMaxApkSize());
        assertEquals(60000, configBean.getTempFilesCleanInterval());
        assertEquals(180000, configBean.getUpdateConfigInterval());
        assertEquals(3, configBean.getKeepDevRevisions());
        assertEquals(TEMP_FILES_DIR_PROPERTY_1, configBean.getTempFilesDirs().get(0));
        assertEquals(TEMP_FILES_DIR_PROPERTY_2, configBean.getTempFilesDirs().get(1));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testFail() throws Exception {
        PowerMockito.when(mockFile.isDirectory()).thenReturn(false);
        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(mockFile);
        configBean.setup();
    }

}
