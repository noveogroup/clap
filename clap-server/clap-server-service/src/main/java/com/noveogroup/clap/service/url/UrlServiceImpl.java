package com.noveogroup.clap.service.url;

import com.noveogroup.clap.config.ConfigBean;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class UrlServiceImpl implements UrlService {

    private static final String ID_TEMPLATE = "{id}";
    private static final String TYPE_TEMPLATE = "{type}";

    private String urlTemplate;

    @Inject
    private ConfigBean configBean;

    @PostConstruct
    public void setup(){
        urlTemplate = configBean.getDownloadApkUrl();
    }

    @Override
    public String createUrl(final Long revisionId, final boolean isMainPackage) {
        String url = StringUtils.replace(urlTemplate,ID_TEMPLATE,Long.toString(revisionId));
        url = StringUtils.replace(url,TYPE_TEMPLATE, isMainPackage ? "0" : "1");
        return url;
    }
}
