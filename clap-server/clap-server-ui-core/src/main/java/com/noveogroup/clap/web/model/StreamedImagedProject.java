package com.noveogroup.clap.web.model;

import com.noveogroup.clap.model.project.ImagedProject;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.ByteArrayInputStream;

/**
 * @author Andrey Sokolov
 */
public class StreamedImagedProject extends ImagedProject {

    private StreamedContent streamedIconFile;

    /**
     * soft copy constructor
     */
    public StreamedImagedProject(final ImagedProject imagedProject) {
        super(imagedProject);
    }

    public StreamedContent getStreamedIconFile() {
        if(streamedIconFile == null && getIconFile() != null){
            streamedIconFile = new DefaultStreamedContent(new ByteArrayInputStream(getIconFile()));
        }
        return streamedIconFile;
    }
}
