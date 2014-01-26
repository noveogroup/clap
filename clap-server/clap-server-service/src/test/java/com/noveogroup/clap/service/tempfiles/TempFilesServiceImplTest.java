package com.noveogroup.clap.service.tempfiles;

import com.google.common.collect.Lists;
import com.noveogroup.clap.config.ConfigBean;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Andrey Sokolov
 */
public class TempFilesServiceImplTest {

    @Mock
    private ConfigBean configBean;

    @Mock
    private InputStream mockStream;

    @InjectMocks
    private TempFileServiceImpl service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testCreateTempFile() throws Exception {
        when(configBean.getTempFilesDirs()).thenReturn(Lists.newArrayList("blablablaToThrowIOException", "./src/test/resources/tempFiles"));
        final File testFile = new File("./src/test/resources/someFileToCopy.txt");
        final File tempFile = service.createTempFile(new FileInputStream(testFile));
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(tempFile)));
        assertEquals("blablabla", reader.readLine());
        assertEquals("qweqweqwe", reader.readLine());
        reader.close();
        tempFile.delete();
    }
}
