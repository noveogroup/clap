package com.noveogroup.clap.service.url;

import com.noveogroup.clap.config.ConfigBean;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

public class UrlServiceImpl implements UrlService {

    private static final String ID_TEMPLATE = "{id}";
    private static final String TYPE_TEMPLATE = "{type}";
    private static final String TOKEN_TEMPLATE = "{token}";

    @Inject
    private ConfigBean configBean;

    @Override
    public String createUrl(final Long revisionId, final boolean isMainPackage, final String token) {
        String url = StringUtils.replace(configBean.getDownloadApkUrl(), ID_TEMPLATE, Long.toString(revisionId));
        url = StringUtils.replace(url, TYPE_TEMPLATE, isMainPackage ? "0" : "1");
        url = StringUtils.replace(url, TOKEN_TEMPLATE, token);
        return url;
    }
}
